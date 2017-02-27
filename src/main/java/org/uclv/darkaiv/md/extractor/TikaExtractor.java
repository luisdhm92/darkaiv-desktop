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
import java.util.HashMap;
import org.apache.commons.lang.StringUtils;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 *
 * @author daniel
 */
public class TikaExtractor implements MDExtractorStrategy {

    // our's Singleton
    // review the staticness when we use multithread
    private static TikaExtractor instance;

    private TikaExtractor() {
    }

    public static TikaExtractor instance() {
        if (instance == null) {
            instance = new TikaExtractor();
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
     * using Tika-engine
     * @throws IOException
     */
    @Override
    public HashMap< String, Object> getMetadata(File file) throws IOException {
        Metadata metadata = new Metadata();
        ContentHandler handler = new BodyContentHandler();
        ParseContext context = new ParseContext();
        Parser parser = new AutoDetectParser();
        //Parser parser = new JournalParser();
        HashMap<String, Object> mdHashMap = new HashMap();

        InputStream stream = new FileInputStream(file);
        try {
            /* Obtain matadata of a stream using BodyContentHandler saving the information in metadata */
            parser.parse(stream, handler, metadata, context);
        } catch (TikaException e) {
            System.out.println("TikaException");
        } catch (SAXException e) {
            System.out.println("SAXException");
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

        return mdHashMap;
    }

}
