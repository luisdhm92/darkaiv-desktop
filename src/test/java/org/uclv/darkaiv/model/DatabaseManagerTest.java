///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.uclv.darkaiv.model;
//
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import static org.junit.Assert.*;
//import org.junit.Before;
//
///**
// *
// * @author daniel
// */
//public class DatabaseManagerTest {
//
//    private static DatabaseManager dm = null;
//
//    private static final String DB_NAME = "repository";
//    private static final String DB_DRIVER = "org.h2.Driver";
//    private static final String DB_URL_TEST = "jdbc:h2:.\\Test_Resources\\DB\\test_repository;DB_CLOSE_DELAY=-1";
//    private static final String DB_USER = "sa";
//    private static final String DB_PASSWORD = "";
//
//    public DatabaseManagerTest() {
//    }
//
//    @BeforeClass
//    public static void setUpClass() {
//        dm = DatabaseManager.instance();
//        dm.init(DB_NAME, DB_DRIVER, DB_URL_TEST, DB_USER, DB_PASSWORD);
//    }
//
//    @AfterClass
//    public static void tearDownClass() {
//        dm.close();
//    }
//
//    @Before
//    public void initialize() {
//    }
//
//    @After
//    public void tearDown() {
//    }
//
//    /**
//     * Test of instance method, of class DatabaseManager.
//     */
//    @Test
//    public void testGetInstance() {
//        System.out.println("getInstance");
//        assertNotNull(" Should exist an instance of a DatabaseManager ", dm);
//    }
//
//    /**
//     * Test of init method, of class DatabaseManager.
//     */
//    @Test
//    public void testInit() {
//        System.out.println("init");
//        //dm.init(DB_NAME, DB_DRIVER, DB_URL, DB_USER, DB_PASSWORD);
//        assertTrue(" At this point should exist a opened connection ", dm.getDb().hasConnection());
//    }
//
//    /**
//     * Test of createDB method, of class DatabaseManager.
//     */
//    @Test
//    public void testCreateDB() {
//        System.out.println("createDB");
//        assertTrue(true);
//    }
//
//    /**
//     * Test of backup method, of class DatabaseManager.
//     */
//    @Test
//    public void testBackup() throws Exception {
//        System.out.println("backup");
//        // verify the file was created
//        assertTrue(true);
//    }
//
//    /**
//     * Test of restore method, of class DatabaseManager.
//     */
//    @Test
//    public void testRestore() throws Exception {
//        System.out.println("restore");
//        assertTrue(true);
//    }
//}
