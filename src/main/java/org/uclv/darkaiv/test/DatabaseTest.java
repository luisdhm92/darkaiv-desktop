/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.test;

/*import java.io.IOException;
 import java.sql.SQLException;

 import org.javalite.activejdbc.DB;
 import org.uclv.darkaiv.model.*;*/
/**
 *
 * @author fenriquez
 */
public class DatabaseTest {

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    /*public static void main(String[] args) throws SQLException, IOException {
     // TODO code application logic here
     //DatabaseManager dm = DatabaseManager.getInstance();

     DB db = new DB("repository");
     try {
     db.open("org.h2.Driver", "jdbc:h2:./darkaiv/repository;DB_CLOSE_DELAY=-1", "sa", "");

     Collection.createIt("name", "All", "description", "All Documents");
     Collection.createIt("name", "Favorites", "description", "Favorites Documents");

     Document.createIt("title", "Java for ...", "collection_id", 1);
     Document.createIt("title", "PHP for ...", "collection_id", 2);

     Identifier.createIt("type", "ISBN", "value", "978-1-4302-2850-9", "document_id", 1);
     Identifier.createIt("type", "ISBN", "value", "978-1-4302-2851-6", "document_id", 2);

     File.createIt("name", "book1.pdf", "document_id", 1);
     File.createIt("name", "book2.pdf", "document_id", 2);

     Repository.createIt("url", "http://libros.uclv.edu.cu/");
     Repository.createIt("url", "http://libros.uclv.edu.cu/");

     People.createIt("first_name", "John", "last_name", "Smith");
     People.createIt("first_name", "Mark", "last_name", "Wytney");

     People_Document.createIt("document_id", 1, "people_id", 2);
     People_Document.createIt("document_id", 2, "people_id", 1);

     Document_Repository.createIt("document_id", 1, "repository_id", 1);

     //dm.backup();
     System.out.println("*** Collections ***");
     Collection.findAll().dump();
     System.out.println("*** Documents ***");
     Document.findAll().dump();
     System.out.println("*** Identifiers ***");
     Identifier.findAll().dump();
     System.out.println("*** Files ***");
     File.findAll().dump();
     System.out.println("*** Repositories ***");
     Repository.findAll().dump();
     System.out.println("*** People ***");
     People.findAll().dump();
     System.out.println("*** People_Document ***");
     People_Document.findAll().dump();
     System.out.println("*** Document_Repository ***");
     Document_Repository.findAll().dump();

     } finally {
     db.close();
     }
     }*/
}
