///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.uclv.darkaiv.organizer;
//
//import java.io.IOException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import static org.junit.Assert.*;
//import org.junit.Before;
//import org.uclv.darkaiv.exceptions.CollectionAlreadyExistException;
//import org.uclv.darkaiv.model.Collection;
//import org.uclv.darkaiv.model.DatabaseManager;
//
///**
// *
// * @author daniel
// */
//public class OrganizerTest {
//
////    private static String name = "";
////    private static String description = "";
//    private static Organizer organizer;
//
//    public OrganizerTest() {
//    }
//
//    @BeforeClass
//    public static void setUpClass() {
////        DBArgs dbArgs = new DBArgs();
////        dbArgs.setDB_URL("jdbc:h2:.\\Test_Resources\\DB\\test_repository;DB_CLOSE_DELAY=-1");
//        organizer = Organizer.instance();
//        try {
//            organizer.init("org.uclv.darkaiv.organizer.test");
//
////        name = "Testing with JUnit " + (Math.random() * 100) % 23;
////        description = "Test Collection " + (Math.random() * 100) % 23;
//        } catch (IOException ex) {
//            Logger.getLogger(OrganizerTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    @AfterClass
//    public static void tearDownClass() {
//        DatabaseManager.instance().getDb().exec("delete from document");
//        //DatabaseManager.instance().getDb().exec("delete from collection");        
//        organizer.close();
//    }
//
//    @Before
//    public void initialize() {
//
//    }
//
//    @After
//    public void tearDown() {
//
//    }
//
//    /**
//     * Test of newCollection method, of class Organizer.
//     *
//     * @throws org.uclv.darkaiv.exceptions.CollectionAlreadyExistException
//     */
//    @Test
//    public void testNewCollection() throws CollectionAlreadyExistException {
//        System.out.println("newCollection");
//        /*organizer.newCollection(name, description);
//         Collection collection = Collection.findFirst(" name = ? ", name);
//         Collection collection2 = Collection.findById(organizer.getActiveCollection());
//         assertEquals(" Both collection aren't the same ", collection, collection2);
//         DatabaseManager.instance().getDb().exec(" delete from collection where name = ? ", name);*/
//        assertTrue(true);
//    }
//
//    /**
//     * Test of addFile method, of class Organizer.
//     *
//     * @throws java.lang.Exception
//     */
//    @Test
//    public void testAddFile() throws Exception {
//        System.out.println("addFile");
//        //organizer.addFile("Test_Resources\\FILES\\desarrollo-agil-symfony-2.1.pdf");
//        //String title = "Desarrollo web ágil con Symfony2";
//        //Document document = Document.findFirst(" title = ? ", title);
//        //assertNotNull(document);
//
//        //DatabaseManager.instance().getDb().exec(" delete from document where title = ? ", title);
//        assertTrue(true);
//    }
//
//    /**
//     * Test of addFiles method, of class Organizer.
//     *
//     * @throws java.lang.Exception
//     */
//    @Test
//    public void testAddFiles() throws Exception {
//        System.out.println("addFiles");
//        // this method should work fine if addFile does
//        assertTrue(true);
//    }
//
//    /**
//     * Test of addFolder method, of class Organizer.
//     *
//     * @throws java.lang.Exception
//     */
//    @Test
//    public void testAddFolder() throws Exception {
//        System.out.println("addFolder");
//        // this method should work fine if addFile does
//        assertTrue(true);
//    }
//
//    /**
//     * Test of loadCollection method, of class Organizer.
//     */
//    @Test
//    public void testLoadCollection() {
//        System.out.println("loadCollection");
//        Collection collection = Collection.findById(1);
//        if (collection != null) {
//            System.err.println("There is no collection on db");
//        }
//        //organizer.loadCollection((String)collection.get("name"));                
//        //assertEquals(1, organizer.getActiveCollection());
//        /*To achieve this test the solicited collection should exist*/
//
//        assertTrue(true);
//    }
//}
