/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.reviewer;

import java.io.IOException;
import java.util.HashMap;
import org.uclv.darkaiv.exceptions.ConnectionCanNotBeEstablishException;
import org.uclv.darkaiv.exceptions.OnlineConnectionFailsException;
import org.uclv.darkaiv.model.Document;

/**
 *
 * @author daniel
 */
public interface ReviewStrategy {

    public HashMap<String, Object> reviewMetadata(Document doc) throws IOException, ConnectionCanNotBeEstablishException, OnlineConnectionFailsException;
}
