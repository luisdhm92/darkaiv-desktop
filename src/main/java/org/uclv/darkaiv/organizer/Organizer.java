/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.organizer;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.uclv.darkaiv.dspace.Community;
import org.uclv.darkaiv.exceptions.CollectionAlreadyExistException;
import org.uclv.darkaiv.exceptions.ConnectionCanNotBeEstablishException;
import org.uclv.darkaiv.exceptions.DocumentNotFoundException;
import org.uclv.darkaiv.exceptions.OnlineConnectionFailsException;
import org.uclv.darkaiv.md.extractor.GrobidExtractor;
import org.uclv.darkaiv.md.extractor.MDExtractorStrategy;
import org.uclv.darkaiv.model.*;
import org.uclv.darkaiv.md.extractor.TikaExtractor;
//import org.uclv.darkaiv.md.extractor.GrobidExtractor;
import org.uclv.darkaiv.publisher.DSPACE;
import org.uclv.darkaiv.qualifier.CompletenessMetrics;
import org.uclv.darkaiv.qualifier.Qualifier;
import org.uclv.darkaiv.reviewer.CrossRef;
import org.uclv.darkaiv.reviewer.Grobid;
import org.uclv.darkaiv.reviewer.ReviewStrategy;
import org.uclv.darkaiv.reviewer.Reviewer;
import org.uclv.darkaiv.reviewer.WorldCat;

/**
 *
 * @author daniel
 */
public class Organizer {

    // create an object of SingleObject
    private static Organizer instance;

    private ResourceBundle resources;

    private String name;
    private String driver;
    private String url;
    private String user;
    private String password;

    // implement mapper in separated classes
    private final DocumentFactory tikaDocumentFactory;
    private final DocumentFactory grobidDocumentFactory;
    private final DocumentFactory crossrefDocumentFactory;
    private final DocumentFactory worldcatDocumentFactory;
    
    private DSPACE dspace;

    private Qualifier qualifier;


    /*
     * make the constructor private so that this class cannot be
     * instantiated
     */
    private Organizer() {
        // open and close the database with a database manager
        // tika mapper could be a singleton
        AbstractApplicationContext context = new FileSystemXmlApplicationContext("config/mappers/tika/tika_bean.xml");
        tikaDocumentFactory = (TikaDocumentFactory) context.getBean("tika");
        //tikaDocumentFactory = new TikaDocumentFactory();
        context = new FileSystemXmlApplicationContext("config/mappers/grobid/grobid_bean.xml");
        grobidDocumentFactory = (GrobidDocumentFactory) context.getBean("grobid");
        //grobidDocumentFactory = new GrobidDocumentFactory();
        context = new FileSystemXmlApplicationContext("config/mappers/crossref/crossref_bean.xml");
        crossrefDocumentFactory = (CRDocumentFactory) context.getBean("crossref");
        //crossrefDocumentFactory = new CRDocumentFactory();
        context = new FileSystemXmlApplicationContext("config/mappers/worldcat/worldcat_bean.xml");
        worldcatDocumentFactory = (WCDocumentFactory) context.getBean("worldcat");
        //worldcatDocumentFactory = new WCDocumentFactory();
        
        context = new FileSystemXmlApplicationContext("config/publish/dspace_bean.xml");
        dspace = (DSPACE) context.getBean("dspace");

        qualifier = new Qualifier();
        qualifier.setQualifierStrategy(new CompletenessMetrics());
    }

    // Get the only object available
    public static Organizer instance() {
        if (instance == null) {
            instance = new Organizer();
        }
        return instance;
    }

    public DSPACE getDspace() {
        return dspace;
    }

    public void setDspace(DSPACE dspace) {
        this.dspace = dspace;
    }

    public DocumentFactory getTikaDocumentFactory() {
        return tikaDocumentFactory;
    }

    public DocumentFactory getCrossrefDocumentFactory() {
        return crossrefDocumentFactory;
    }

    public DocumentFactory getGrobidDocumentFactory() {
        return grobidDocumentFactory;
    }

    public DocumentFactory getWorldcatDocumentFactory() {
        return worldcatDocumentFactory;
    }
    
    
    
    

    /**
     * open database connection
     */
    public void open() {
        DatabaseManager.instance().open(driver, url, user, password);
    }

    /**
     * prepares the database connection and makes initial verifications
     *
     * @param bundle
     * @throws java.io.IOException
     */
    public void init(String bundle) throws IOException {
        if (bundle == null) {
            resources = ResourceBundle.getBundle("org.uclv.darkaiv.organizer.organizer");
        } else {
            resources = ResourceBundle.getBundle(bundle);
        }

        name = resources.getString("db.name");
        driver = resources.getString("db.driver");
        url = resources.getString("db.url");
        user = resources.getString("db.user");
        password = resources.getString("db.password");

        DatabaseManager.instance().init(name, driver, url, user, password);
        if (Collection.findAll().isEmpty()) {
            try {
                newCollection(resources.getString("db.default.collection.name1"), resources.getString("db.default.collection.description1"));
//                newCollection(resources.getString("db.default.collection.name2"), resources.getString("db.default.collection.description2"));
            } catch (CollectionAlreadyExistException ex) {
                Logger.getLogger(Organizer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * close database connection
     */
    public void close() {
        DatabaseManager.instance().close();
    }

    /**
     *
     * create a new collection
     *
     * @param name
     * @param description
     * @return
     * @throws CollectionAlreadyExistException
     */
    public boolean newCollection(String name, String description) throws CollectionAlreadyExistException {

        Collection collection = Collection.findFirst("name = ?", name);
        if (collection != null) {
            throw new CollectionAlreadyExistException("Collection " + name + " already exist");
        }

        Collection.createIt("threshold", 66, "name", name, "description", description);

        return true;
    }

    /**
     *
     * @param doc
     * @return review document using Crossref as source
     * @throws IOException
     * @throws org.uclv.darkaiv.exceptions.ConnectionCanNotBeEstablishException
     * @throws org.uclv.darkaiv.exceptions.DocumentNotFoundException
     * @throws org.uclv.darkaiv.exceptions.OnlineConnectionFailsException
     */
    public Document reviewDocumentCR(Document doc) throws IOException, ConnectionCanNotBeEstablishException, DocumentNotFoundException, OnlineConnectionFailsException {

        //HashMap<String, Object> metadata = (HashMap<String, Object>) doc.retrieveMetadata();
        // change to reviewer, do it with an interface
        Reviewer reviewer = new Reviewer();
        CrossRef cr = new CrossRef();
        reviewer.setReviwerStrategy(cr);
        //HashMap<String, Object> md_reviewed = cr.reviewMetadata(metadata);
        HashMap<String, Object> md_reviewed = reviewer.getReviwerStrategy().reviewMetadata(doc);
        crossrefDocumentFactory.update(doc, md_reviewed);

        return doc;
    }

    public Document reviewDocumentWC(Document doc) throws IOException, ConnectionCanNotBeEstablishException, DocumentNotFoundException, OnlineConnectionFailsException {

        //HashMap<String, Object> metadata = (HashMap<String, Object>) doc.retrieveMetadata();
        // change to reviewer, do it with an interface
        Reviewer reviewer = new Reviewer();
        WorldCat wc = new WorldCat();
        reviewer.setReviwerStrategy(wc);
        HashMap<String, Object> md_reviewed = reviewer.getReviwerStrategy().reviewMetadata(doc);
        worldcatDocumentFactory.update(doc, md_reviewed);

        return doc;
    }

    public Document extractMDGrobid(Document doc) throws IOException, DocumentNotFoundException, OnlineConnectionFailsException, ConnectionCanNotBeEstablishException {

        if (doc != null && doc.validateFile()) {
            MDExtractorStrategy grobid = GrobidExtractor.instance();
            Map<String, Object> firstMD = doc.retrieveMetadata();
            System.out.println("path " + firstMD.get("path"));
            java.io.File file = new java.io.File(""+firstMD.get("path"));
            HashMap<String, Object> metadata = grobid.getMetadata(file);
            if (!metadata.isEmpty()) {
                grobidDocumentFactory.update(doc, metadata);
                doc.saveIt();
            }
        } else {
            throw new DocumentNotFoundException("The file does not exist");
        }
        //ReviewStrategy grobid = Grobid.instance();
        //HashMap<String, Object> metadata = grobid.reviewMetadata(doc);

//        List<org.uclv.darkaiv.model.File> files = doc.getAll(org.uclv.darkaiv.model.File.class);
//
//        if (!files.isEmpty()) {
////            System.out.println("Entre al if de la salsa");
//            org.uclv.darkaiv.model.File file = files.get(0);
//            if (file.getString("mime_type").equals("application/pdf") && doc.validateFile()) {
////                System.out.println("Entre al if de la salsa 2.0");
//                java.io.File physicalFile = new java.io.File(file.getString("path"));
//                HashMap<String, Object> metadataGrobid = grobid.getMetadata(physicalFile);
//
//                if (!metadataGrobid.isEmpty()) {
//                    grobidDocumentFactory.update(doc, metadataGrobid);
//                    doc.saveIt();
//                }
//            }
//        }
        return doc;
    }

    /**
     *
     * @param path
     * @param collectionName
     * @return add metadata extracted from file to the database
     * @throws IOException
     * @throws Exception
     */
    public boolean addFile(String path, String collectionName) throws IOException, Exception {

        // use the abstraction instead the concret class
        MDExtractorStrategy tika = TikaExtractor.instance();
//        MDExtractorStrategy grobid = GrobidExtractor.instance();

        java.io.File file = new java.io.File(path);
        HashMap<String, Object> metadataTika = tika.getMetadata(file);
        //long docID = metadataMapperTika.insert(activeCollectionID, metadataTika);
//        Set<String> keys = metadataTika.keySet();

        long collectionId = Collection.findFirst("name = ?", collectionName).getInteger("id");
        Document doc = tikaDocumentFactory.create(collectionId, metadataTika);
        doc.saveIt();

        // insert path into File table
        org.uclv.darkaiv.model.File docFile = new File();

        System.out.println("File: " + file.getAbsolutePath());

        docFile.set("path", file.getAbsolutePath());
        // hash code to duplicated detection
        docFile.set("file_hash", System.identityHashCode(file));
        if (tika.getDocumentType(file) != null) {
            docFile.set("mime_type", tika.getDocumentType(file));
        }

        doc.add(docFile);

        return true;
    }

    /**
     *
     * @param files
     * @param collectionName
     * @return add metadata extracted from files to the database using addFile()
     * method
     * @throws IOException
     * @throws Exception
     */
    public boolean addFiles(String[] files, String collectionName) throws IOException, Exception {

        boolean result = true;
        for (String file : files) {
            result &= addFile(file, collectionName);
        }

        return result;
    }

    private boolean isPDF(String name) {
        StringTokenizer st = new StringTokenizer(name, ".");
        String token = "";
        while (st.hasMoreElements()) {
            token = st.nextToken();
        }
        return token.equals("pdf");
    }

    /**
     *
     * @param path
     * @param collectionName
     * @throws IOException
     * @throws Exception
     */
    public void addFolder(String path, String collectionName) throws IOException, Exception {

        java.io.File file = new java.io.File(path);
        java.io.File[] files = file.listFiles();

        for (java.io.File subFile : files) {
            if (subFile.isDirectory()) {
                //System.out.println(file1.getName() + " is a directory");
                addFolder(subFile.getAbsolutePath(), collectionName);
            } else {
                if (isPDF(subFile.getName())) {
                    addFile(subFile.getAbsolutePath(), collectionName);
                }
                //System.out.println(file.getAbsolutePath());
            }
        }
    }

    /**
     *
     * @return list of all collections
     */
    public List<Collection> getCollections() {
        return Collection.findAll();
    }

    /**
     *
     * @return list of all documents
     */
    public List<Document> getDocuments() {
        return Document.find("deleted = ?", 0);
    }

    /**
     *
     * @return list of needed review documents
     */
    public List<Document> needreview() {
        List<Document> docs = Document.find("needreview = ? and deleted = ?", 1, 0);

        return docs;
    }

    public List<Document> deleteddocuments() {
        List<Document> docs = Document.find("deleted = ?", 1);

        return docs;
    }

    /**
     *
     * @return list of unpublished documents
     */
    public List<Document> unpublished() {
        List<Document> docs = Document.find("deleted = ?", 0);
        List<Document> docsU = new ArrayList();
        for (Document d : docs) {
            if (Document_Repository.find("document_id = ?", d.get("id")).isEmpty()) {
                docsU.add(d);
            }
        }

        return docsU;
    }

    public List<Document> recentlyAdds() {
        List<Document> docs = Document.find("deleted = ?", 0);
        List<Document> recentlyAdds = new ArrayList();

        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -3);
        Date threeDaysAgo = cal.getTime();
        int threeDaysAgoInt = Integer.parseInt(df.format(threeDaysAgo));

        for (Document d : docs) {
            String created_at = d.get("created_at").toString();
            String formatDate = "";
            formatDate += created_at.substring(0, 4);
            formatDate += created_at.substring(5, 7);
            formatDate += created_at.substring(8, 10);
            int created_at_int = Integer.parseInt(formatDate);

            if (created_at_int >= threeDaysAgoInt) {
                recentlyAdds.add(d);
            }
        }

        return recentlyAdds;
    }

    /**
     *
     * @param collectionName
     * @return list of documents of collection=collectionName
     */
    public List<Document> getDocumentsByCollection(String collectionName) {
        if (collectionName.equals(resources.getString("gui.default.collection.alldocuments"))) {
            return getDocuments();
        }
        if (collectionName.equals(resources.getString("gui.default.collection.recentlyadds"))) {
            return recentlyAdds();
        }
        if (collectionName.equals(resources.getString("gui.default.collection.unpublished"))) {
            return unpublished();
        }
        if (collectionName.equals(resources.getString("gui.default.collection.needreview"))) {
            return needreview();
        }
        if (collectionName.equals(resources.getString("gui.default.collection.deleteddocuments"))) {
            return deleteddocuments();
        }
        int id = Collection.findFirst("name = ?", collectionName).getInteger("id");
        return Document.find("collection_id = ? and deleted = ?", id, 0);
    }

    /**
     *
     * @param doc
     * @return quality of document doc
     */
    public double getQuality(Document doc) {
        return qualifier.getQualifierStrategy().getMetric(doc);
    }

    /**
     *
     * @param path
     * @param name
     * @throws SQLException
     */
    public void backupDB(String path, String name) throws SQLException {
        DatabaseManager.instance().backup(path, name);
    }

    /**
     *
     * @param path
     * @throws IOException
     * @throws SQLException
     */
    public void restoreDB(String path) throws IOException, SQLException {
        DatabaseManager.instance().restore(path);
    }

    /**
     *
     * @param docId
     * @param field
     * @param value
     * @return document updated
     */
    public Document update(long docId, String field, String value) {
        Document doc = Document.findById(docId);

        if (doc != null) {
            if (value.equals("")) {
                doc.set(field, null);
            } else {
                doc.set(field, value);
            }
            doc.saveIt();
        }

        return doc;
    }

    /**
     *
     * @param collectionName
     * @return quality threshold of a given collection
     */
    public int getCollectionQualityThreshold(String collectionName) {
        Collection collection = Collection.findFirst("name = ?", collectionName);
        return Integer.parseInt(collection.getString("threshold"));
    }

    /**
     *
     * @param collectionName
     * @param threshold
     */
    public void setCollectionQualityThreshold(String collectionName, int threshold) {
        Collection collection = Collection.findFirst("name = ?", collectionName);

        if (collection != null) {
            collection.set("threshold", threshold);
            collection.saveIt();
        }
    }

    /**
     *
     * @param docId
     * @return
     */
    public String getAuthorsAsString(long docId) {
        List<People_Document> author_doc = People_Document.find("document_id = ?", docId);

        String ath = "";
        for (People_Document author_doc1 : author_doc) {
            People auhtor = People.findFirst("id = ?", author_doc1.get("people_id"));
            if (!ath.equals("")) {
                ath += ", " + (String) auhtor.get("last_name") + " " + ((String) auhtor.get("first_name"));
            } else {
                ath += (String) auhtor.get("last_name") + " " + ((String) auhtor.get("first_name"));
            }
        }
        return ath;
    }

    /**
     *
     * @param docId
     * @return
     */
    public String getAuthorsAsStringWNL(long docId) {
        List<People_Document> author_doc = People_Document.find("document_id = ?", docId);

        String ath = "";
        for (People_Document author_doc1 : author_doc) {
            People auhtor = People.findFirst("id = ?", author_doc1.get("people_id"));
            if (!ath.equals("")) {
                ath += "\n" + (String) auhtor.get("last_name") + ", " + ((String) auhtor.get("first_name"));
            } else {
                ath += (String) auhtor.get("last_name") + ", " + ((String) auhtor.get("first_name"));
            }
        }
        return ath;
    }

    /**
     *
     * @param docID
     * @param type
     * @return
     */
    public String loadOrCreateidentifier(long docID, String type) {

        List<Identifier> list = Identifier.where("document_id = ? and type = ?", docID, type);
        if (list.size() > 0) {
            if (list.get(0).getString("value") != null) {
                return list.get(0).getString("value");
            } else {
                return "";
            }
        } else {
            Identifier id = new Identifier();
            id.set("type", type);
            id.set("document_id", docID);
            id.saveIt();
            return "";
        }
    }

    /**
     *
     * @param docID
     * @param type
     * @param value
     */
    public void updateIdentifier(long docID, String type, String value) {
        List<Identifier> list = Identifier.where("document_id = ? and type = ?", docID, type);
        if (list.size() > 0) {
            Identifier id = list.get(0);
            id.set("value", value);
            id.saveIt();
        }
    }

    /**
     *
     * @param docID
     * @param authors
     * @throws java.io.IOException
     */
    public void updateAuthors(long docID, String authors) throws IOException {
        List<People_Document> people_Documents = People_Document.find("document_id = ?", docID);
        for (People_Document pd : people_Documents) {
            People people = People.findFirst("id = ?", pd.get("people_id"));
            People_Document.delete("people_id = ?", people.getId());
            people.deleteCascade();
        }

        InputStream is = new ByteArrayInputStream(authors.getBytes(StandardCharsets.UTF_8));
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        StringTokenizer st;

        String authorStr;
        while ((authorStr = br.readLine()) != null) {
            if (!authorStr.equals("")) {
                authorStr = authorStr.trim();

                People author = new People();
                author.saveIt();

                st = new StringTokenizer(authorStr, ",");

                int tokens = st.countTokens();
                for (int i = 0; i < tokens; i++) {
                    String value = st.nextToken();
                    switch (i) {
                        case 0:
                            author.set("last_name", value.trim());
                            break;
                        case 1:
                            author.set("first_name", value.trim());
                            break;
                    }
                }
                author.saveIt();

                People_Document pd = new People_Document();

                pd.set("people_id", author.getLong("id"));
                pd.set("document_id", docID);
                pd.set("type", "author");
                pd.saveIt();
            }
        }
    }

    /**
     *
     * @return top community's in DSPACE
     * @throws java.net.MalformedURLException
     */
    public Community[] getopCommunitys() throws MalformedURLException {
        DSPACE dspace = new DSPACE();
        return dspace.getTopCommunities();
    }

    /**
     *
     * @param collectionId
     * @param doc
     * @return item id
     * @throws java.net.MalformedURLException
     */
    public int publish(String collectionId, Document doc) throws MalformedURLException {
        DSPACE dspace = new DSPACE();
        int status = dspace.publish(collectionId, doc);

        if (status != -1) {
            if (doc != null) {
                doc.set("uploaded", 1);
                doc.saveIt();
            }
        }

        return status;
    }

    public int filterPublish(String collectionId, String type) throws MalformedURLException {
        List<Document> list = Document.where("type = ?", type);
        int status = 0;
        for (Document doc : list) {
            status &= publish(collectionId, doc);
        }
        return status;
    }

    /**
     *
     * @param item
     * @param path
     * @return if upload bit stream
     * @throws FileNotFoundException
     * @throws MalformedURLException
     */
    public boolean uploadBitstream(long item, String path) throws FileNotFoundException, MalformedURLException {
        DSPACE dspace = new DSPACE();
        return dspace.uploadBitstream(item, path);
    }

    /**
     *
     * @param docId
     */
    public void markAsNeedsReview(long docId) {
        Document doc = Document.findById(docId);

        if (doc != null) {
            doc.set("needreview", 1);
            doc.saveIt();
        }
    }

    /**
     *
     * @param docId
     */
    public void markAsReviewed(long docId) {
        Document doc = Document.findById(docId);

        if (doc != null) {
            doc.set("needreview", 0);
            doc.saveIt();
        }
    }

    /**
     *
     * @param docId
     */
    public void delete(long docId) {
        Document doc = Document.findById(docId);

        if (doc != null) {
            doc.set("deleted", 1);
            doc.saveIt();
        }
    }

    /**
     *
     * @param docId
     */
    public void restore(long docId) {
        Document doc = Document.findById(docId);

        if (doc != null) {
            doc.set("deleted", 0);
            doc.saveIt();
        }
    }

    /**
     *
     * @param docId
     */
    public void deletePermanently(long docId) {
        Document doc = Document.findById(docId);
        if (doc != null) {
            List<People_Document> people_Documents = People_Document.find("document_id = ?", docId);
            for (People_Document pd : people_Documents) {
                People people = People.findFirst("id = ?", pd.get("people_id"));
                People_Document.delete("people_id = ?", people.getId());
                people.deleteCascade();
            }

            List<Identifier> identifiers = Identifier.find("document_id = ?", docId);
            for (Identifier id : identifiers) {
                id.delete();
            }

            List<File> files = File.find("document_id = ?", docId);
            for (File file : files) {
                file.delete();
            }

            doc.deleteCascade();
        }
    }

    /**
     *
     * @param name
     */
    public void deleteCollection(String name) {
        Collection collection = Collection.findFirst("name = ?", name);

        if (collection != null) {
            long id = collection.getLongId();

            List<Document> docs = Document.find("collection_id = ?", id);
            for (Document document : docs) {
                this.deletePermanently(document.getLongId());
            }

            collection.deleteCascade();
        }
    }

    /**
     *
     * @param name
     */
    public void emptyTrash(String name) {
        List<Document> docs = Document.find("deleted = ?", 1);
        for (Document document : docs) {
            this.deletePermanently(document.getLongId());
        }
    }

    /**
     *
     * @return
     */
    public int countDeletedDocuments() {
        List<Document> docs = Document.find("deleted = ?", 1);
        return docs.size();
    }

    /**
     *
     * @param name
     * @return
     */
    public boolean existCollection(String name) {
        Collection collection = Collection.findFirst("name = ?", name);
        if (collection == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     *
     * @param actualName
     * @param newName
     */
    public void renameCollection(String actualName, String newName) {
        Collection collection = Collection.findFirst("name = ?", actualName);
        if (collection != null) {
            collection.set("name", newName);
            collection.saveIt();
        }
    }
}
