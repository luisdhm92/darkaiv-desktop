/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.organizer;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.uclv.darkaiv.exceptions.DocumentNotFoundException;
//import java.util.Set;
import org.uclv.darkaiv.model.Document;
import org.uclv.darkaiv.model.Identifier;
import org.uclv.darkaiv.model.People;
import org.uclv.darkaiv.model.People_Document;

/**
 *
 * @author daniel
 */
public class CRDocumentFactory extends DocumentFactory {
    // separate document's hashmap to identifier's hashmap (...)

//    public CRDocumentFactory() {
//        super();
//        // <CR:DB>
//        // only in Document
//        metadataMapperDocument.put("publisher", "publisher");
//        metadataMapperDocument.put("title", "title");
//        metadataMapperDocument.put("issue", "issue");
//        metadataMapperDocument.put("page", "pages");
//        metadataMapperDocument.put("type", "type");
//        metadataMapperDocument.put("volume", "volume");
//        // only in Identifier
//        metadataMapperIdentifier.put("doi", "doi");
//        metadataMapperIdentifier.put("issn", "issn");
//    }

//    @Override
//    public boolean update(Document doc, Map<String, Object> metadata) throws DocumentNotFoundException {
//
//        super.update(doc, metadata);
//        /*System.out.println("After to do an update with crossref");
//
//         Map<String, Object> metadata2 = doc.retrieveMetadata();
//
//         Set<String> keys = metadata2.keySet();
//         for (String key : keys) {
//         System.out.println(key + " " + metadata2.get(key));
//         }*/
//        return true;
//    }
    // change document to long
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
        if (!doc.exists()) {
            throw new DocumentNotFoundException("The document to update should exist");
        }

        for (String field : metadataMapperDocument.keySet()) {

            // key comes from Tika and should be mapped to our bd
            if (metadata.get(field) != null) {
                
                if (field.equals("type")) {
                    if (!(metadata.get(field).equals("journal-article") || metadata.get(field).equals("book") ||
                           metadata.get(field).equals("proceedings") )) {
                        metadata.put("type", "generic");
                    }
                }
                doc.set(metadataMapperDocument.get(field), metadata.get(field));
            }
        }

        for (String field : metadataMapperIdentifier.keySet()) {

            if (metadata.get(field) != null) {
                //Ajustar a los 4 tipos que existen
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
                        lastname = value;
                    } else if (i == 1) {
                        firstname += value;
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
            doc.set("reviewed", 1);
        }

        return true;
    }
}
