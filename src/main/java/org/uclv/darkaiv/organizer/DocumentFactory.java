/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.organizer;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.uclv.darkaiv.exceptions.DocumentNotFoundException;
import org.uclv.darkaiv.model.Document;
import org.uclv.darkaiv.model.Identifier;
import org.uclv.darkaiv.model.People;
import org.uclv.darkaiv.model.People_Document;

/**
 *
 * @author daniel
 */
public class DocumentFactory {

    protected Map< String, String> metadataMapperDocument;
    protected Map< String, String> metadataMapperIdentifier;
    private String authorKey;

    public DocumentFactory() {
        metadataMapperDocument = new HashMap<String, String>();
        metadataMapperIdentifier = new HashMap<String, String>();
        authorKey = "author";
    }

    public Map<String, String> getMetadataMapperDocument() {
        return metadataMapperDocument;
    }

    public void setMetadataMapperDocument(HashMap<String, String> metadataMapperDocument) {
        this.metadataMapperDocument = metadataMapperDocument;
    }

    public Map<String, String> getMetadataMapperIdentifier() {
        return metadataMapperIdentifier;
    }

    public void setMetadataMapperIdentifier(HashMap<String, String> metadataMapperIdentifier) {
        this.metadataMapperIdentifier = metadataMapperIdentifier;
    }

    /**
     *
     * @param collection
     * @param metadata
     * @return create a document with metadata HashMap and return the id in
     * database
     */
    public Document create(long collection, Map<String, Object> metadata) {
        Document doc = new Document();
        // to guarantee insert through relationships the document should be persistent
        doc.saveIt();

        doc.set("collection_id", collection);

        // saving data to document
        for (String field : metadataMapperDocument.keySet()) {

            // key comes from Tika and should be mapped to our bd
            if (metadata.get(field) != null) {
                //System.out.println("Inserting " + field + " in " + metadataMapperDocument.get(field));
                //System.out.println("Value " + metadata.get(field));
                doc.set(metadataMapperDocument.get(field), metadata.get(field));
            }
        }

        // saving into year the first 4 characters of creation-date
        if (metadata.containsKey("creation-date") && metadata.get("creation-date") != null) {
            try {
                Integer.decode(metadata.get("creation-date").toString().substring(0, 4));
                doc.set("year", metadata.get("creation-date").toString().substring(0, 4));
            } catch (NumberFormatException exception) {
//                Toolkit.getDefaultToolkit().beep();
            }
        }

        // setting generic as default document type
        if (!metadata.containsKey("type") || metadata.get("type") == null) {
            doc.set("type", "generic");
        }

        doc.set("needreview", 0);
        doc.set("deleted", 0);
        doc.set("reviewed", 0);
        doc.set("uploaded", 0);

        // saving data to identifier
        for (String field : metadataMapperIdentifier.keySet()) {

            // key comes from Tika and should be mapped to our bd
            if (metadata.get(field) != null) {
                //System.out.println("Inserting " + field + " in " + metadataMapperIdentifier.get(field));
                //System.out.println("Value " + metadata.get(field));
                Identifier ident = new Identifier();
                ident.set("type", metadataMapperIdentifier.get(field));
                ident.set("value", metadata.get(field));
                doc.add(ident);
            }
        }
        // change it to organizer
        //doc.saveIt();
        if (metadata.get(authorKey) != null) {
            // insert author into data layer

            // tika also splits author's name with comma (,)
            StringTokenizer authorsFT = new StringTokenizer((String) metadata.get(authorKey), ";");

            while (authorsFT.hasMoreElements()) {

                //System.out.println("Inserting into auhtor table");
                People author = new People();
                author.saveIt();

                String actualAuthor = authorsFT.nextToken().trim();
                StringTokenizer st = new StringTokenizer(actualAuthor, " ");

                //System.out.println("st has " + st.countTokens() + " tokens");
                int tokens = st.countTokens();
                String firstname = "";
                String lastname = "";
                for (int i = 0; i < tokens; i++) {
                    String value = st.nextToken();
                    if (i == 0) {
                        firstname = value;
                    } else if (i == tokens - 1) {
                        lastname += value;
                    } else {
                        firstname += " " + value;
                    }
                }
                author.set("first_name", firstname);
                author.set("last_name", lastname);
                author.saveIt();

                People_Document pd = new People_Document();

                pd.set("people_id", author.getLong("id"));
                pd.set("document_id", doc.getLong("id"));
                pd.set("type", "author");
                //System.out.println("People_Document");
                //System.out.println(pd);
                pd.saveIt();
            }

        }
        return doc;
    }

    // change document to long
    /**
     *
     * @param doc
     * @param metadata
     * @return update a document with metadata HashMap and return the id in
     * database
     * @throws org.uclv.darkaiv.exceptions.DocumentNotFoundException
     */
    public boolean update(Document doc, Map<String, Object> metadata) throws DocumentNotFoundException {
//        //Document doc = Document.findById(document);
//        if (!doc.exists()) {
//            throw new DocumentNotFoundException("The document to update should exist");
//        }
//
//        for (String field : metadataMapperDocument.keySet()) {
//
//            // key comes from Tika and should be mapped to our bd
//            if (metadata.get(field) != null) {
//                doc.set(metadataMapperDocument.get(field), metadata.get(field));
//            }
//        }
//
//        for (String field : metadataMapperIdentifier.keySet()) {
//
//            if (metadata.get(field) != null) {
//                //System.out.println("Inserting " + field + " in " + metadataMapperIdentifier.get(field));
//                //System.out.println("Value " + metadata.get(field));
//
//                List<Identifier> list = Identifier.where("document_id = ? and type = ?", doc.getId(), metadataMapperIdentifier.get(field));
//                if (list.size() > 0) {
//                    Identifier ident = list.get(0);
//                    ident.set("value", metadata.get(field));
//                    ident.saveIt();
//                } else {
//                    Identifier ident = new Identifier();
//                    ident.set("type", metadataMapperIdentifier.get(field));
//                    ident.set("value", metadata.get(field));
//                    doc.add(ident);
//                }
////                Identifier ident = new Identifier();
////                ident.set("type", metadataMapperIdentifier.get(field));
////                ident.set("value", metadata.get(field));
////                doc.add(ident);
//            }
//        }
//
//        doc.saveIt();
//
//        if (metadata.get(authorKey) != null) {
//            // insert author into data layer
//
//            // tika also splits author's name with comma (,)
//            StringTokenizer authorsFT = new StringTokenizer((String) metadata.get(authorKey), ",");
//
//            while (authorsFT.hasMoreElements()) {
//
//                //System.out.println("Inserting into auhtor table");
//                People author = new People();
//                author.saveIt();
//
//                String actualAuthor = authorsFT.nextToken().trim();
//                //System.out.println("actualAuthor");
//                //System.out.println(actualAuthor);
//                StringTokenizer st = new StringTokenizer(actualAuthor, " ");
//
////                System.out.println("st has " + st.countTokens() + " tokens");
//                int tokens = st.countTokens();
//                for (int i = 0; i < tokens; i++) {
//                    String value = st.nextToken();
//                    //System.out.println("value " + value);
//                    //System.out.println("token " + i + "th");
//                    switch (i) {
//                        case 0:
//                            author.set("first_name", value);
//                            break;
//                        case 1:
//                            author.set("last_name", value);
//                            break;
//                    }
//                }
//                // it should be done with activeJDBC but it does nothing
//                //doc.add(author);
//                //System.out.println("People");
//                //System.out.println(author);
//                author.saveIt();
//
//                People_Document pd = new People_Document();
//
//                pd.set("people_id", author.getLong("id"));
//                pd.set("document_id", doc.getLong("id"));
//                pd.set("type", "author");
//                //System.out.println("People_Document");
//                // System.out.println(pd);
//                pd.saveIt();
//            }
//        }
//
        return true;
    }
}
