///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.uclv.darkaiv.reviewer;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.uclv.darkaiv.exceptions.ConnectionCanNotBeEstablishException;
//import org.uclv.darkaiv.exceptions.OnlineConnectionFailsException;
//import org.uclv.darkaiv.model.Document;
//import org.uclv.darkaiv.reviewer.util.CRParser;
//
///**
// *
// * @author admin
// */
//public class MockCrossRef extends CrossRef {
//
//    private String json = "{\"status\":\"ok\",\"message-type\":\"work\",\"message-version\":\"1.0.0\",\"message\":{\"indexed\":{\"date-parts\":[[2016,3,6]],\"date-time\":\"2016-03-06T10:05:04Z\",\"timestamp\":1457258704181},\"reference-count\":105,\"publisher\":\"American Psychological Association (APA)\",\"issue\":\"1\",\"DOI\":\"10.1037\\/0003-066x.59.1.29\",\"type\":\"journal-article\",\"created\":{\"date-parts\":[[2004,1,21]],\"date-time\":\"2004-01-21T14:31:19Z\",\"timestamp\":1074695479000},\"page\":\"29-40\",\"source\":\"CrossRef\",\"title\":[\"How the Mind Hurts and Heals the Body.\"],\"prefix\":\"http:\\/\\/id.crossref.org\\/prefix\\/10.1037\",\"volume\":\"59\",\"author\":[{\"affiliation\":[],\"family\":\"Ray\",\"given\":\"Oakley\"}],\"member\":\"http:\\/\\/id.crossref.org\\/member\\/15\",\"published-online\":{\"date-parts\":[[2004]]},\"container-title\":[\"American Psychologist\"],\"deposited\":{\"date-parts\":[[2016,3,3]],\"date-time\":\"2016-03-03T16:19:22Z\",\"timestamp\":1457021962000},\"score\":1.0,\"subtitle\":[],\"issued\":{\"date-parts\":[[2004]]},\"alternative-id\":[\"2004-10043-004\"],\"URL\":\"http:\\/\\/dx.doi.org\\/10.1037\\/0003-066x.59.1.29\",\"ISSN\":[\"1935-990X\",\"0003-066X\"],\"subject\":[\"Psychology(all)\"]}}";
//
//    @Override
//    public String getContent(String doi) throws IOException, OnlineConnectionFailsException {
//        return json;
//    }
//
//    @Override
//    public HashMap<String, Object> reviewMetadata(Document doc) throws IOException, ConnectionCanNotBeEstablishException, OnlineConnectionFailsException {
//
//        HashMap<String, Object> md_reviewed = new HashMap();
//
//        if (json != null) {
//            if (!json.equals("")) {
//                CRParser parser = new CRParser();
//                try {
//                    md_reviewed = parser.parse(json);
//                } catch (Exception ex) {
//                    Logger.getLogger(CrossRef.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            } else {
//                throw new ConnectionCanNotBeEstablishException("XML could not be retrieved correctly");
//            }
//        }
//
//        return md_reviewed;
//    }
//}
