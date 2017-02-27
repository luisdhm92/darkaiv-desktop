/*
 Copyright 2009-2010 Igor Polevoy

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package org.uclv.darkaiv.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Cached;
import org.javalite.activejdbc.annotations.DbName;
import org.javalite.activejdbc.annotations.Table;

/**
 *
 * @author fenriquez
 */
@DbName("repository")
@Cached
@Table("document")
public class Document extends Model {

    /**
     *
     * @return retrieves all document's metadata on database
     * @throws NullPointerException
     */
    public Map<String, Object> retrieveMetadata() throws NullPointerException {

        Map<String, Object> metadata = this.getAttributes();
        Set<String> keys = metadata.keySet();
        Map<String, Object> retrieve = new HashMap<String, Object>();

        for (String key : keys) {
//            System.out.println(key);
            retrieve.put(key, get(key));
        }

        // only for persistent document
        int id = -1;
        try {
            id = (Integer) get("id");
        } catch (NullPointerException e) {
            throw new NullPointerException("The document must exist in database");
        }

        if (id == -1) {
            throw new NullPointerException("The document can't be retrieved");
        }

        //List<Identifier> identifiers = Identifier.where("document_id = ?", id);
        // identifier table
        List<Identifier> identifiers = this.getAll(Identifier.class);
        for (Identifier identifier : identifiers) {
            retrieve.put((String) identifier.get("type"), identifier.get("value"));
        }
        // author table (problems with MtoM relationships )
        //List<People> authors = this.getAll(People.class);
        List<People> authors = new ArrayList();

        // add type=?, "auhtor"
        List<People_Document> author_doc = People_Document.find("document_id=?", this.getLong("id"));

        for (People_Document author_doc1 : author_doc) {
            List<People> auhtor = People.find("id = ?", author_doc1.get("people_id"));
            authors.addAll(auhtor);
        }

        String ath = "";
        for (People auhtor : authors) {
            if (!ath.equals("")) {
                ath += ", " + (String) auhtor.get("first_name") + " " + ((String) auhtor.get("last_name"));
            } else {
                ath += ((String) auhtor.get("first_name") + " " + (String) auhtor.get("last_name"));
            }
        }
        retrieve.put("author", ath);
        
         List<File> files = this.getAll(File.class);
        for (File file : files) {
            retrieve.put("mime_type", file.getString("mime_type"));
            retrieve.put("path", file.getString("path"));
            retrieve.put("file_hash", file.getString("file_hash"));
            retrieve.put("size", file.getString("size"));
        }

//        Set<String> keys = metadata.keySet();
        /*for (String key : keys) {
         System.out.println(key + " " + metadata.get(key));
         }*/
        return retrieve;
    }

    public boolean validateFile() {
        List<File> files = this.getAll(File.class);

        if (!files.isEmpty()) {
            File file = files.get(0);
            java.io.File physicalFile = new java.io.File(file.getString("path"));
            return physicalFile.exists();
        }

        return false;
    }
}
