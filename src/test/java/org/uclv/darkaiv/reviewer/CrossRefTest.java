///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.uclv.darkaiv.reviewer;
//
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.util.HashMap;
//import org.eclipse.jetty.server.Server;
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import static org.junit.Assert.*;
//import org.junit.Before;
//import org.uclv.darkaiv.model.Document;
//
///**
// *
// * @author daniel
// */
//public class CrossRefTest {
//
//    private static Server server;
//
//    public CrossRefTest() {
//    }
//
//    @BeforeClass
//    public static void setUpClass() throws Exception {
//
////        server = new Server(8080);
////
////        server.setHandler(new GetContentHandler());
////        server.start();
//        //server.join();
//    }
//
//    /**
//     *
//     * @throws Exception
//     */
//    @AfterClass
//    public static void tearDownClass() throws Exception {
//        //server.stop();
//    }
//
//    @Before
//    public void setUp() throws Exception {
//    }
//
//    @After
//    public void tearDown() {
//    }
//
//    /**
//     * Test of getContent method, of class CrossRef.
//     *
//     * @throws java.net.MalformedURLException
//     */
//    @Test
//    public void testGetContent() throws MalformedURLException, IOException {
////        System.out.println("getContent");
////        String result = "";
////        String url = "http://localhost:8080";
////
////        InputStreamReader reader
////                = new InputStreamReader(new URL(url).openStream());
////        BufferedReader br = new BufferedReader(reader);
////        String inputLine = "";
////
////        System.out.println("Begin to read lines");
////        while ((inputLine = br.readLine()) != null) {
////            result += inputLine;
////            System.out.print(inputLine);
////        }
////        System.out.println("End to read lines");
////
////        assertEquals("It works", result);
////        assertTrue(true);
//    }
//
//    /**
//     * Test of reviewMetadata method, of class CrossRef.
//     *
//     * @throws java.lang.Exception
//     */
//    @Test
//    public void testReviewMetadata() throws Exception {
//        System.out.println("reviewMetadata");
//        MockCrossRef mock = new MockCrossRef();
//        Document doc = new Document();
//        HashMap<String, Object> metadata = mock.reviewMetadata(doc);
//        assertTrue(metadata.size() > 0);
//    }
//}
