/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.model;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import org.h2.tools.RunScript;
import org.javalite.activejdbc.DB;

/**
 *
 * @author fenriquez
 */
public class DatabaseManager {

    // <editor-fold defaultstate="collapsed" desc="Querys">
    private static final String CREATE_DOCUMENT_TABLE = "CREATE TABLE IF NOT EXISTS document (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
            + " title VARCHAR,"
            + " created_at VARCHAR,"
            + " updated_at VARCHAR,"
            + " creation_date VARCHAR,"
            + " updated_date VARCHAR,"
            + " abstract VARCHAR,"
            + " year VARCHAR,"
            + " key_words VARCHAR,"
            + " pages VARCHAR,"
            + " publisher VARCHAR,"
            + " volume VARCHAR,"
            + " source VARCHAR,"
            + " idiom VARCHAR,"
            + " editors VARCHAR,"
            + " edition VARCHAR,"
            + " city VARCHAR,"
            + " chapter VARCHAR,"
            + " department VARCHAR,"
            + " university VARCHAR,"
            + " thesis_type VARCHAR,"
            + " issue VARCHAR,"
            + " institution VARCHAR,"
            + " type VARCHAR,"
            + " needreview INT,"
            + " deleted INT,"
            + " reviewed INT,"
            + " uploaded INT,"
            + " collection_id INT,"
            + " FOREIGN KEY (collection_id) REFERENCES collection(id))";

    private static final String CREATE_IDENTIFIER_TABLE = "CREATE TABLE IF NOT EXISTS identifier (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
            + " type VARCHAR,"
            + " value VARCHAR,"
            + " document_id INT,"
            + " FOREIGN KEY (document_id) REFERENCES document(id));";

    private static final String CREATE_FILE_TABLE = "CREATE TABLE IF NOT EXISTS file (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
            + " name VARCHAR,"
            + " MIME_type VARCHAR,"
            + " path VARCHAR,"
            + " file_hash VARCHAR,"
            + " size INT,"
            + " document_id INT,"
            + " FOREIGN KEY (document_id) REFERENCES document(id));";

    private static final String CREATE_COLLECTION_TABLE = "CREATE TABLE IF NOT EXISTS collection (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
            + " threshold INT,"
            + " name VARCHAR,"
            + " description VARCHAR);";

    private static final String CREATE_REPOSITORY_TABLE = "CREATE TABLE IF NOT EXISTS repository (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
            + " url VARCHAR,"
            + " instance VARCHAR,"
            + " overwrite_duplicates BOOLEAN,"
            + " type VARCHAR,"
            + " config_file INT);";

    private static final String CREATE_PEOPLE_TABLE = "CREATE TABLE IF NOT EXISTS people (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
            + " first_name VARCHAR,"
            + " last_name VARCHAR);";

    private static final String CREATE_PEOPLE_DOCUMENT_TABLE = "CREATE TABLE IF NOT EXISTS people_document (people_id INT NOT NULL,"
            + " document_id INT NOT NULL,"
            + " type VARCHAR,"
            + " PRIMARY KEY (people_id, document_id),"
            + " FOREIGN KEY (people_id) REFERENCES people(id),"
            + " FOREIGN KEY (document_id) REFERENCES document(id));";

    private static final String CREATE_DOCUMENT_REPOSITORY_TABLE = "CREATE TABLE IF NOT EXISTS document_repository (document_id INT NOT NULL,"
            + " repository_id INT NOT NULL,"
            + " PRIMARY KEY (document_id, repository_id),"
            + " collection VARCHAR,"
            + " updated_at DATE,"
            + " FOREIGN KEY (document_id) REFERENCES document(id),"
            + " FOREIGN KEY (repository_id) REFERENCES repository(id));";
    // </editor-fold>

    //create an object of SingleObject
    private static DatabaseManager instance;
    private DB db;

    /*
     * make the constructor private so that this class cannot be
     * instantiated
     */
    private DatabaseManager() {
    }

    public DB getDb() {
        return db;
    }

    public void setDb(DB db) {
        this.db = db;
    }

    //Get the only object available
    public static DatabaseManager instance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }

        return instance;
    }

    public boolean open(String driver, String url, String user, String password) {
        db.open(driver, url, user, password);
        return true;
    }

    /**
     *
     * @param dbName
     * @param driver
     * @param url
     * @param user
     * @param password
     * @return makes the connection with the database
     */
    public boolean init(String dbName, String driver, String url, String user, String password) {

        db = new DB(dbName);
        db.open(driver, url, user, password);

        createDB();

        return true;
    }

    /**
     *
     * @return close the connection with the database
     */
    public boolean close() {

        db.close();
        return true;
    }

    /**
     *
     * @return execute sentence to create the database
     */
    public boolean createDB() {

        db.exec(CREATE_COLLECTION_TABLE);
        db.exec(CREATE_DOCUMENT_TABLE);
        db.exec(CREATE_IDENTIFIER_TABLE);
        db.exec(CREATE_FILE_TABLE);
        db.exec(CREATE_REPOSITORY_TABLE);
        db.exec(CREATE_PEOPLE_TABLE);
        db.exec(CREATE_PEOPLE_DOCUMENT_TABLE);
        db.exec(CREATE_DOCUMENT_REPOSITORY_TABLE);

        return true;
    }

    /**
     *
     * @param file_path
     * @param name
     * @return makes a backup text file with SQL sentence of the database
     * @throws SQLException
     */
    public boolean backup(String file_path, String name) throws SQLException {
        // url should pass through a parameter, at least a part
        String[] args = {
            "-url",
            "jdbc:h2:./darkaiv/repository",
            "-user",
            "sa",
            "-script",
            file_path + "/" + name + ".dkv"
        };

        org.h2.tools.Script.main(args);
        return true;
    }

    private void deleteActualDB() {
        db.exec("DROP ALL OBJECTS");
    }

    /**
     *
     * @param file_path
     * @return try to restore the database from a file with SQL sentences
     * @throws IOException
     * @throws java.sql.SQLException
     */
    public boolean restore(String file_path) throws IOException, SQLException {

        deleteActualDB();
        RunScript.execute(db.getConnection(), new FileReader(file_path));
        /*String sql = readFile(file_path);
         StringTokenizer st = new StringTokenizer(sql, ";");

         while (st.hasMoreElements()) {
         db.
         db.exec(st.nextToken().trim() + ';');
         }*/
        return true;
    }

    /*private String readFile(String path) throws FileNotFoundException, IOException {

     //System.out.println("Reading a file");
     java.io.File file = new java.io.File(path);
     String string;
     String sql = "";

     FileReader fr = new FileReader(file);
     BufferedReader br = new BufferedReader(fr);
     while ((string = br.readLine()) != null) {
     sql += string;
     }

     //System.out.println("Finish!!!");
     br.close();

     return sql;
     }*/
}
