/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.md.extractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.uclv.darkaiv.reviewer.util.JournalParser;
import org.apache.tika.sax.BodyContentHandler;
import org.uclv.darkaiv.exceptions.OnlineConnectionFailsException;
import org.uclv.darkaiv.md.extractor.util.TEIParser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 *
 * @author daniel
 */
public class GrobidExtractor implements MDExtractorStrategy {

    // our's Singleton
    // review the staticness when we use multithread
    private static GrobidExtractor instance;

    public GrobidExtractor() {
    }

    public static GrobidExtractor instance() {
        if (instance == null) {
            instance = new GrobidExtractor();
        }
        return instance;
    }

    /**
     *
     * @param file
     * @return file's document type
     * @throws IOException
     */
    @Override
    public String getDocumentType(File file) throws IOException {
        Tika tika = new Tika();
        String type = tika.detect(file);
        return type;
    }

    /**
     *
     * @param file
     * @return A HashMap<String, Object> with the metadata retrieved from file
     * using Grobid
     * @throws IOException
     * @throws org.uclv.darkaiv.exceptions.OnlineConnectionFailsException
     */
    @Override
    public HashMap<String, Object> getMetadata(File file) throws IOException, OnlineConnectionFailsException {

        HashMap<String, Object> metadata2 = new HashMap();
        if (existConnection()) {
            Metadata metadata = new Metadata();
            ContentHandler handler = new BodyContentHandler();
            ParseContext context = new ParseContext();
            //Parser parser = new AutoDetectParser();
            Parser parser = new JournalParser();

            HashMap<String, Object> mdHashMap = new HashMap();

            InputStream stream = new FileInputStream(file);

            try {
                /* Obtain matadata of a stream using BodyContentHandler saving the information in metadata */
                parser.parse(stream, handler, metadata, context);
            } catch (TikaException e) {
                System.out.println("TikaException");
                //return metadata2;
            } catch (SAXException e) {
                System.out.println("SAXException");
                //return metadata2;
            } catch (Exception e) {
                System.out.println("Connection could not be established");
                return metadata2;
            } finally {
                stream.close();
            }

            for (String key : metadata.names()) {
                String name = key.toLowerCase();
                String value = metadata.get(key);

                if (StringUtils.isBlank(value)) {
                    // continue;
                }

                mdHashMap.put(name, value);
            }

            //return mdHashMap;
            // all field aren't needed
            Set<String> keys = mdHashMap.keySet();
            String source = "";
            for (String key : keys) {
                //System.out.println(key + " " + mdHashMap.get(key));
                if (key.equals("grobid:header_teixmlsource")) {
                    source += mdHashMap.get(key);
                }
            }

            TEIParser tei = new TEIParser();
            metadata2 = tei.parse(source);
            metadata2.put("keywords", metadata.get("Keywords"));
        } else {
            throw new OnlineConnectionFailsException("OnlineConnectionFailsException");
        }

        return metadata2;
    }

    /**
     *
     * @return true if Grobid.server.url is reachable
     * @throws IOException
     */
    public boolean existConnection() throws IOException {

        boolean isConn;

        File file = new File("./config/grobid_service/");
        URL[] urls = {file.toURI().toURL()};
        ClassLoader loader = new URLClassLoader(urls);
        ResourceBundle resources = ResourceBundle.getBundle("grobid", Locale.getDefault(), loader);

        String host = resources.getString("grobid.server.url");

        int port = 8080;

        final URL url = new URL(host);
        final URLConnection conn = url.openConnection();
        if (conn != null) {
            try {
                conn.getContent();
                isConn = true;
            } catch (Exception e) {
                isConn = false;
            }
        } else {
            isConn = false;
        }

        return isConn;
    }
}
