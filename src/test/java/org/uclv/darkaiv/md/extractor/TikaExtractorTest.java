///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.uclv.darkaiv.md.extractor;
//
//import java.io.File;
//import java.util.HashMap;
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
//public class TikaExtractorTest {
//
//    private TikaExtractor tkE = null;
//
//    public TikaExtractorTest() {
//    }
//
//    @BeforeClass
//    public static void setUpClass() {
//    }
//
//    @AfterClass
//    public static void tearDownClass() {
//    }
//
//    @Before
//    public void initialize() {
//        //TikaExtractor tkE = null;
//        tkE = TikaExtractor.instance();
//    }
//
//    @After
//    public void tearDown() {
//    }
//
//    /**
//     * Test of instance method, of class TikaExtractor.
//     */
//    @Test
//    public void testInstance() {
//        System.out.println("instance");
//        assertNotNull(" Should exist an instance of a TikaExtractor ", tkE);
//    }
//
//    /**
//     * Test of getMetadata method, of class TikaExtractor.
//     *
//     * @throws java.lang.Exception
//     */
//    @Test
//    public void testGetMetadata() throws Exception {
//        System.out.println("getMetadata");
//        // commented until use AutoDetectParser
//        //File file = new File("Test_Resources\\FILES\\desarrollo-agil-symfony-2.1.pdf");
//        File file = new File(getClass().getClassLoader().getResource("desarrollo-agil-symfony-2.1.pdf").getPath());
//        System.out.println(file.getAbsolutePath());
//        HashMap<String, Object> result = tkE.getMetadata(file);
//        assertTrue(result.size() > 0);
//
//    }
//
//    /**
//     * Test of getContent method, of class TikaExtractor.
//     *
//     * @throws java.lang.Exception
//     */
//    @Test
//    public void testGetContent() throws Exception {
//        System.out.println("getContent");
//        assertEquals(true, true);
//    }
//
//    /**
//     * Test of getDocumentType method, of class TikaExtractor.
//     *
//     * @throws java.lang.Exception
//     */
//    @Test
//    public void testGetDocumentType() throws Exception {
//        System.out.println("getDocumentType");
//        assertEquals(true, true);
//    }
//}
