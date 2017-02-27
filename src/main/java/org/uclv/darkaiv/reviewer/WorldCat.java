/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.reviewer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;
import org.uclv.darkaiv.exceptions.ConnectionCanNotBeEstablishException;
import org.uclv.darkaiv.exceptions.OnlineConnectionFailsException;
import org.uclv.darkaiv.model.Document;

/**
 *
 * @author admin
 */
public class WorldCat implements ReviewStrategy{
    
    public String getContent(String isbn) throws IOException, OnlineConnectionFailsException {

        String json = "";
        System.err.println("1");

        File file = new File("./config/worldcat/");
        URL[] urls = {file.toURI().toURL()};
        ClassLoader loader = new URLClassLoader(urls);
        ResourceBundle resources = ResourceBundle.getBundle("worldcat", Locale.getDefault(), loader);
        
        URL url = new URL(resources.getString("worldcat.server.url") + isbn + resources.getString("worldcat.server.url.config"));

        System.out.println("Sending: " + url.toString());
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
                json = convertStreamToString(in);

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
        return json;
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

    @Override
    public HashMap<String, Object> reviewMetadata(Document doc) throws IOException, ConnectionCanNotBeEstablishException, OnlineConnectionFailsException {
        Map<String, Object> metadata = doc.retrieveMetadata();
        HashMap<String, Object> mdReviewed = null;
        if (metadata.containsKey("isbn")) {
            try {
            ObjectMapper mapper = new ObjectMapper();
            String json = getContent((String)metadata.get("isbn"));
            JSONObject jsonObj = new JSONObject(json);
                JSONArray json2;
                String js2st = "";
                if (jsonObj.has("list")) {
                    json2 = jsonObj.getJSONArray("list");
                    js2st += json2.toString().substring(1, json2.toString().length() - 1);
//                    System.out.println(js2st.substring(1, js2st.length() - 2));
                }
            mdReviewed = mapper.readValue(js2st, new TypeReference<Map<String, Object>>() {
            });
            
            
 
            Set<String> keys = mdReviewed.keySet();
                System.out.println("Printing metadata!!!");
            for (String key : keys) {
                System.out.println(key + " " + mdReviewed.get(key));
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        }
        
        return mdReviewed;
    }
    
}
