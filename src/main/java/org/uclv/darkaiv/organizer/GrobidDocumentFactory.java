/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.organizer;

import java.util.List;
import java.util.Map;
import java.util.Set;
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
public class GrobidDocumentFactory extends DocumentFactory {

//    public GrobidDocumentFactory() {
//        super();
//        // <Grobid:DB>
//        // only in Document
//        metadataMapperDocument.put("abstract", "abstract");
//        metadataMapperDocument.put("title", "title");
//        metadataMapperDocument.put("issue", "issue");
//        //metadataMapperGrobid.put("date", "year");
//        metadataMapperDocument.put("page", "pages");
//        metadataMapperDocument.put("volume", "volume");
//        metadataMapperDocument.put("source", "source");
//        metadataMapperDocument.put("type", "type");
//        
//        // this should be parsed better
//        //metadataMapperGrobid.put("affiliation", "university");
//        metadataMapperDocument.put("keyword", "key_words");
//        // only in Identifier
//        metadataMapperIdentifier.put("doi", "doi");
//        metadataMapperIdentifier.put("arXiv", "arXiv");
//    }

    /**
     *
     * @param doc
     * @param metadata
     * @return update a document with metadata HashMap and return the id in
     * database
     * @throws org.uclv.darkaiv.exceptions.DocumentNotFoundException
     */
    @Override
    public boolean update(Document doc, Map<String, Object> metadata) throws DocumentNotFoundException {
        //Document doc = Document.findById(document);
        Set<String> keys = metadata.keySet();
        for (String key : keys) {
            System.out.println(key + " " + metadata.get(key) );
        }
        if (!doc.exists()) {
            throw new DocumentNotFoundException("The document to update should exist");
        }

        if (doc.getInteger("reviewed") == 1) {
            //Only exist three fields we don't take from Crossref: abstract, keywords, arXiv
            if (metadata.get("abstract") != null) {
                doc.set(metadataMapperDocument.get("abstract"), metadata.get("abstract"));
            }
            if (metadata.get("keyword") != null) {
                doc.set(metadataMapperDocument.get("keyword"), metadata.get("keyword"));
            }
            if (metadata.get("arXiv") != null) {

                List<Identifier> list = Identifier.where("document_id = ? and type = ?", doc.getId(), metadataMapperIdentifier.get("arXiv"));
                if (list.size() > 0) {
                    Identifier ident = list.get(0);
                    ident.set("value", metadata.get("arXiv"));
                    ident.saveIt();
                } else {
                    Identifier ident = new Identifier();
                    ident.set("type", metadataMapperIdentifier.get("arXiv"));
                    ident.set("value", metadata.get("arXiv"));
                    doc.add(ident);
                }
            }
        } else {
            //obtain document type
            if (metadata.get("volume") != null || metadata.get("source") != null
                    || metadata.get("issn") != null) {
                metadata.put("type", "journal-article");
            }
            if (metadata.get("isbn") != null) {
                metadata.put("type", "book");
            }
            for (String field : metadataMapperDocument.keySet()) {

                // key comes from Tika and should be mapped to our bd
                if (metadata.get(field) != null) {
                    doc.set(metadataMapperDocument.get(field), metadata.get(field));
                }
            }

            for (String field : metadataMapperIdentifier.keySet()) {

                if (metadata.get(field) != null) {

                    List<Identifier> list = Identifier.where("document_id = ? and type = ?", doc.getId(), metadataMapperIdentifier.get(field));
                    if (list.size() > 0) {
                        Identifier ident = list.get(0);
                        ident.set("value", metadata.get(field));
                        ident.saveIt();
                    } else {
                        Identifier ident = new Identifier();
                        ident.set("type", metadataMapperIdentifier.get(field));
                        ident.set("value", metadata.get(field));
                        doc.add(ident);
                    }
                }
            }

            doc.saveIt();

            if (metadata.get("author") != null) {
                //delete all authors
                List<People_Document> people_Documents = People_Document.find("document_id = ?", doc.getLong("id"));
                for (People_Document pd : people_Documents) {
                    People people = People.findFirst("id = ?", pd.get("people_id"));
                    People_Document.delete("people_id = ?", people.getId());
                    people.deleteCascade();
                }

                // insert author into data layer
                // tika also splits author's name with comma (,)
                StringTokenizer authorsFT = new StringTokenizer((String) metadata.get("author"), ",");

                while (authorsFT.hasMoreElements()) {

                    People author = new People();
                    author.saveIt();

                    String actualAuthor = authorsFT.nextToken().trim();
                    StringTokenizer st = new StringTokenizer(actualAuthor, " ");

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
                    pd.saveIt();
                }
            }
        }
        return true;
    }
}
