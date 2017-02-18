///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.uclv.darkaiv.reviewer;
//
//import java.io.IOException;
//import java.util.HashMap;
//import org.uclv.darkaiv.exceptions.OnlineConnectionFailsException;
//import org.uclv.darkaiv.model.Document;
//import org.uclv.darkaiv.reviewer.util.TEIParser;
//
///**
// *
// * @author admin
// */
//public class MockGrobidExtractor extends Grobid {
//
//    private String source = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
//            + "<?xml-model href=\"file:///opt/grobid/grobid-home/schemas/rng/Grobid.rng\" schematypens=\"http://relaxng.org/ns/structure/1.0\"?>\n"
//            + "<TEI\n"
//            + "    xmlns=\"http://www.tei-c.org/ns/1.0\">\n"
//            + "    <teiHeader xml:lang=\"en\">\n"
//            + "        <fileDesc>\n"
//            + "            <titleStmt>\n"
//            + "                <title level=\"a\" type=\"main\">A comparison of student satisfaction and value of academic community between blended and online sections of a university-level educational foundations course</title>\n"
//            + "            </titleStmt>\n"
//            + "            <publicationStmt>\n"
//            + "                <publisher/>\n"
//            + "                <availability status=\"unknown\">\n"
//            + "                    <licence/>\n"
//            + "                </availability>\n"
//            + "                <date type=\"published\" when=\"2010-12-13\">Available online 13 December 2010</date>\n"
//            + "            </publicationStmt>\n"
//            + "            <sourceDesc>\n"
//            + "                <biblStruct>\n"
//            + "                    <analytic>\n"
//            + "                        <author>\n"
//            + "                            <persName>\n"
//            + "                                <forename type=\"first\">Richard</forename>\n"
//            + "                                <forename type=\"middle\">C</forename>\n"
//            + "                                <surname>Overbaugh</surname>\n"
//            + "                            </persName>\n"
//            + "                            <affiliation>\n"
//            + "                                <orgName type=\"department\">Darden School of Education</orgName>\n"
//            + "                                <orgName type=\"institution\">Old Dominion University</orgName>\n"
//            + "                                <address>\n"
//            + "                                    <postCode>23529</postCode>\n"
//            + "                                    <settlement>Norfolk</settlement>\n"
//            + "                                    <region>VA</region>\n"
//            + "                                    <country key=\"US\">United States</country>\n"
//            + "                                </address>\n"
//            + "                            </affiliation>\n"
//            + "                        </author>\n"
//            + "                        <author>\n"
//            + "                            <persName>\n"
//            + "                                <forename type=\"first\">Christine</forename>\n"
//            + "                                <forename type=\"middle\">E</forename>\n"
//            + "                                <surname>Nickel</surname>\n"
//            + "                            </persName>\n"
//            + "                            <affiliation>\n"
//            + "                                <orgName type=\"department\">Darden School of Education</orgName>\n"
//            + "                                <orgName type=\"institution\">Old Dominion University</orgName>\n"
//            + "                                <address>\n"
//            + "                                    <postCode>23529</postCode>\n"
//            + "                                    <settlement>Norfolk</settlement>\n"
//            + "                                    <region>VA</region>\n"
//            + "                                    <country key=\"US\">United States</country>\n"
//            + "                                </address>\n"
//            + "                            </affiliation>\n"
//            + "                            <affiliation>\n"
//            + "                                <orgName type=\"department\">Center for Teaching &amp; Learning</orgName>\n"
//            + "                                <orgName type=\"institution\">Regent University</orgName>\n"
//            + "                                <address>\n"
//            + "                                    <addrLine>1000 Regent University Drive</addrLine>\n"
//            + "                                </address>\n"
//            + "                            </affiliation>\n"
//            + "                            <affiliation>\n"
//            + "                                <orgName type=\"institution\">Virginia Beach</orgName>\n"
//            + "                                <address>\n"
//            + "                                    <postCode>23464</postCode>\n"
//            + "                                    <region>VA</region>\n"
//            + "                                    <country key=\"US\">United States</country>\n"
//            + "                                </address>\n"
//            + "                            </affiliation>\n"
//            + "                            <affiliation>\n"
//            + "                                <orgName type=\"institution\" key=\"instit1\">Regent University Drive</orgName>\n"
//            + "                                <orgName type=\"institution\" key=\"instit2\">Virginia Beach</orgName>\n"
//            + "                                <address>\n"
//            + "                                    <postCode>23464</postCode>\n"
//            + "                                    <region>VA</region>\n"
//            + "                                    <country key=\"US\">United States</country>\n"
//            + "                                </address>\n"
//            + "                            </affiliation>\n"
//            + "                        </author>\n"
//            + "                        <title level=\"a\" type=\"main\">A comparison of student satisfaction and value of academic community between blended and online sections of a university-level educational foundations course</title>\n"
//            + "                    </analytic>\n"
//            + "                    <monogr>\n"
//            + "                        <imprint>\n"
//            + "                            <date type=\"published\" when=\"2010-12-13\">Available online 13 December 2010</date>\n"
//            + "                        </imprint>\n"
//            + "                    </monogr>\n"
//            + "                    <idno type=\"DOI\">10.1016/j.iheduc.2010.12.001</idno>\n"
//            + "                    <note type=\"submission\">Article history: Accepted 2 December 2010</note>\n"
//            + "                    <note>a b s t r a c t a r t i c l e i n f o</note>\n"
//            + "                </biblStruct>\n"
//            + "            </sourceDesc>\n"
//            + "        </fileDesc>\n"
//            + "        <profileDesc>\n"
//            + "            <textClass>\n"
//            + "                <keywords>\n"
//            + "                    <term>Academic community</term>\n"
//            + "                    <term>Sense of community</term>\n"
//            + "                    <term>Student satisfaction</term>\n"
//            + "                    <term>Perceived learning</term>\n"
//            + "                    <term>Online learning</term>\n"
//            + "                    <term>Blended learning</term>\n"
//            + "                </keywords>\n"
//            + "            </textClass>\n"
//            + "            <abstract>\n"
//            + "                <p>This pre-test/post-test study explores students&apos; (n = 262) sense of academic community, including their perspectives of the value of academic community, plus course satisfaction and perceived learning in nearly identical blended and online sections of an educational foundations course. Students in both delivery modes were generally satisfied with the course, had equally high perceived learning scores, but had neutral sense of community and value of connectedness scores. The findings in this study raise questions about whether the students would benefit from the infusion of time-intensive community-building strategies.</p>\n"
//            + "            </abstract>\n"
//            + "        </profileDesc>\n"
//            + "    </teiHeader>\n"
//            + "    <text xml:lang=\"en\"></text>\n"
//            + "</TEI>";
//
//    /**
//     *
//     * @param doc
//     * @return A HashMap<String, Object> with the metadata retrieved from file
//     * using Grobid
//     * @throws IOException
//     * @throws org.uclv.darkaiv.exceptions.OnlineConnectionFailsException
//     */
//    @Override
//    public HashMap<String, Object> reviewMetadata(Document doc) throws IOException, OnlineConnectionFailsException {
//
//        HashMap<String, Object> metadata2 = new HashMap();
//
//        TEIParser tei = new TEIParser();
//        metadata2 = tei.parse(source);
//
//        return metadata2;
//    }
//
//}
