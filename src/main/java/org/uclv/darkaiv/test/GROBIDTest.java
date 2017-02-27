/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.uclv.darkaiv.exceptions.OnlineConnectionFailsException;
import org.uclv.darkaiv.md.extractor.GrobidExtractor;

/**
 *
 * @author daniel
 */
public class GROBIDTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws OnlineConnectionFailsException {
        // TODO code application logic here
        System.out.println("Hello world");

        File file = new File("./Test_Resources/FILES/Reconciling Description Logics and Rules.pdf");

        HashMap<String, Object> metadata = new HashMap();
        GrobidExtractor ge = GrobidExtractor.instance();

        try {
            metadata = ge.getMetadata(file);
        } catch (IOException ex) {
            Logger.getLogger(GROBIDTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        Set<String> keys = metadata.keySet();

        for (String key : keys) {
            System.out.println(key + " " + metadata.get(key));
        }
    }
}
