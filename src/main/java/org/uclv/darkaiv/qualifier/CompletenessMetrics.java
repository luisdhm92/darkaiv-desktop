/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.qualifier;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import org.uclv.darkaiv.model.Document;

/**
 *
 * @author daniel
 */
public class CompletenessMetrics implements QualifyStrategy {
    /* Identify which are the identifiers */

    /**
     *
     * @param doc
     * @return get a completeness metric to the metadata passes as parameter
     */
    @Override
    public double getMetric(Document doc) {
        try {
            Map<String, Object> metadata = doc.retrieveMetadata();
            File file = new File("./config/completeness_metric/");
            URL[] urls = {file.toURI().toURL()};
            ClassLoader loader = new URLClassLoader(urls);
            ResourceBundle resource = ResourceBundle.getBundle(metadata.get("type").toString(), Locale.getDefault(), loader);
            double completeness = 0;
            double total = 0;

            for (String key : resource.keySet()) {
                total += Double.parseDouble(resource.getString(key));
            }

            Set<String> keys = metadata.keySet();
            for (String key : keys) {
                if (resource.containsKey(key) && (metadata.get(key) != null)) {
                    completeness += Double.parseDouble(resource.getString(key));
                }
            }

            return completeness / total;
        } catch (MalformedURLException ex) {
            return -1;
        } catch (NumberFormatException ex) {
            return -1;
        }
    }
}
