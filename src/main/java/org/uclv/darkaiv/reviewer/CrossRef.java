/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.reviewer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.uclv.darkaiv.exceptions.ConnectionCanNotBeEstablishException;
import org.uclv.darkaiv.exceptions.OnlineConnectionFailsException;
import org.uclv.darkaiv.model.Document;

import org.uclv.darkaiv.reviewer.util.CRParser;

/**
 *
 * @author daniel
 */
public class CrossRef implements ReviewStrategy {

    public String getContent(String doi) throws IOException, OnlineConnectionFailsException {

        String xml = "";

        File file = new File("./config/crossref/");
        URL[] urls = {file.toURI().toURL()};
        ClassLoader loader = new URLClassLoader(urls);
        ResourceBundle resources = ResourceBundle.getBundle("crossref", Locale.getDefault(), loader);
        URL url = new URL(resources.getString("crossref.server.url") + doi);

        //System.out.println("Sending: " + url.toString());
        HttpURLConnection urlConn = null;
        try {
            urlConn = (HttpURLConnection) url.openConnection();
        } catch (Exception e) {
            // I don't know why
            try {
                urlConn = (HttpURLConnection) url.openConnection();
            } catch (Exception e2) {
//                e2.printStackTrace();
                urlConn = null;
            }
        }

        if (urlConn != null) {
            try {
                urlConn.setDoOutput(true);
                urlConn.setDoInput(true);
                urlConn.setRequestMethod("GET");

                urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                InputStream in = urlConn.getInputStream();
                xml = convertStreamToString(in);

//                System.out.println(xml);

                urlConn.disconnect();
            } catch (Exception e) {
                System.err.println("Warning: Consolidation set true, "
                        + "but the online connection to Crossref fails.");
                return null;
            }
        } else {
            throw new OnlineConnectionFailsException("OnlineConnectionFailsException");
        }
//        System.out.println("XML " + xml);
        return xml;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
//            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
//                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     *
     * @param doc
     * @return metadata extracted from CrossRef
     * @throws IOException
     * @throws org.uclv.darkaiv.exceptions.ConnectionCanNotBeEstablishException
     * @throws org.uclv.darkaiv.exceptions.OnlineConnectionFailsException
     */
    @Override
    public HashMap<String, Object> reviewMetadata(Document doc) throws IOException, ConnectionCanNotBeEstablishException, OnlineConnectionFailsException {

        Map<String, Object> metadata = doc.retrieveMetadata();
        HashMap<String, Object> md_reviewed = new HashMap();

        if (metadata.get("doi") != null) {
            //System.out.println("doi " + (String) metadata.get("doi"));
            String xml = getContent((String) metadata.get("doi"));

            if (xml != null) {
                if (!xml.equals("")) {
                    CRParser parser = new CRParser();
                    try {
                        md_reviewed = parser.parse(xml);
                    } catch (Exception ex) {
                        Logger.getLogger(CrossRef.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    throw new ConnectionCanNotBeEstablishException("XML could not be retrieved correctly");
                }
            } else {
                throw new OnlineConnectionFailsException("OnlineConnectionFailsException");
            }
        }

        return md_reviewed;
    }
}
