/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.dspace;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;
import org.uclv.darkaiv.model.Document;

/**
 *
 * @author admin
 */
public class ItemFactory {
    
    private Map<String, String> itemMapper;

    public void setItemMapper(Map<String, String> itemMapper) {
        this.itemMapper = itemMapper;
    }
    
    public Map<String, String> getItemMapper() {
        return itemMapper;
    } 
    
    
    
//    public ItemFactory() throws MalformedURLException {
//        itemMapper = new HashMap();
//        
//        File file = new File("./config/publish/");
//        URL[] urls = {file.toURI().toURL()};
//        ClassLoader loader = new URLClassLoader(urls);
//        ResourceBundle resource = ResourceBundle.getBundle("metadata_labels", Locale.getDefault(), loader);
//        
//        Set<String> keys = resource.keySet();
//        System.out.println("Printing keys");
//        for (String key : keys) {
//            // redundant but...
//            if (resource.containsKey(key)) {
//                System.out.println(key);
//                itemMapper.put(key, resource.getString(key));                
//            }
//            
//        }
//        
//        
//
//        //<BD:Doublin Core>
////        itemMapper.put("title", "dc.title");
////        itemMapper.put("abstract", "dc.description.abstract");
////        itemMapper.put("publisher", "dc.publisher");
////        itemMapper.put("year", "dc.date");
////        itemMapper.put("type", "dc.type");
////        itemMapper.put("mime_type", "dc.format");
////        itemMapper.put("author", "dc.creator");
//    }

    
    
    public Item createItem(Document doc) {
        Item item = new Item();
        List<MetadataEntry> md = new ArrayList();
        Map<String, Object> metadata = doc.retrieveMetadata();
        
        Set<String> keys = metadata.keySet();
        
        if (metadata.containsKey("title") && (metadata.get("title") != null)) {
            item.setName((String) metadata.get("name"));
        } else {
            item.setName("default");
        }
        
        for (String key : keys) {
            if ((metadata.get(key) != null) && (itemMapper.get(key) != null)) {
                if (key.equals("author")) {
                    StringTokenizer st = new StringTokenizer((String) metadata.get(key), ",");
                    //System.out.println("this doc has " + st.countTokens());
                    String author = "";
                    while (st.hasMoreTokens()) {
                        author = st.nextToken();
                        if (author != null) {
                            md.add(new MetadataEntry(itemMapper.get(key), author, null));
                        }
                    }
                } else {
                    md.add(new MetadataEntry(itemMapper.get(key), (String) metadata.get(key), null));
                }
            }
        }
        
        item.setMetadata(md);
//        item.setMetadata(metadata);
        return item;
    }
}
