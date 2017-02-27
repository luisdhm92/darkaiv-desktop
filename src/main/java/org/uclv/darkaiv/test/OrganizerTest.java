/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.test;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.uclv.darkaiv.dspace.Item;
import org.uclv.darkaiv.dspace.ItemFactory;
import org.uclv.darkaiv.md.extractor.GrobidExtractor;
import org.uclv.darkaiv.model.Document;
import org.uclv.darkaiv.organizer.*;
import org.uclv.darkaiv.publisher.DSPACE;
import org.uclv.darkaiv.reviewer.WorldCat;

/**
 *
 * @author daniel
 */
public class OrganizerTest {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
//         TODO code application logic here
        DBArgs dbArgs = new DBArgs();
        dbArgs.setDB_URL("jdbc:h2:.\\darkaiv\\repository;DB_CLOSE_DELAY=-1");
        GrobidExtractor grobid = GrobidExtractor.instance();
        GrobidDocumentFactory grobidMapper = new GrobidDocumentFactory();
        Organizer organizer = Organizer.instance();
        organizer.init("org.uclv.darkaiv.organizer.organizer");

        Map mapper = organizer.getWorldcatDocumentFactory().getMetadataMapperDocument();
        System.out.println(mapper);

        organizer.close();
    }
}
