/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.publisher;

import java.net.MalformedURLException;
import org.uclv.darkaiv.model.Document;

/**
 *
 * @author daniel
 */
public abstract class PublishStrategy {

    /**
     *
     * @param collectionId
     * @param doc
     * @return
     * @throws java.net.MalformedURLException
     */
    public abstract int publish(String collectionId, Document doc) throws MalformedURLException;

    /**
     *
     * @param collectionId
     * @param doc
     * @return
     * @throws java.net.MalformedURLException
     */
    public abstract boolean update(String collectionId, Document doc) throws MalformedURLException;
}
