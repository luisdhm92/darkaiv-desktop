/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.test;

import java.io.IOException;
import org.uclv.darkaiv.exceptions.ConnectionCanNotBeEstablishException;
import org.uclv.darkaiv.exceptions.OnlineConnectionFailsException;
import org.uclv.darkaiv.model.Document;
import org.uclv.darkaiv.reviewer.WorldCat;

/**
 *
 * @author admin
 */
public class WorldCatTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ConnectionCanNotBeEstablishException, OnlineConnectionFailsException {
        // TODO code application logic here
        WorldCat wc = new WorldCat();
        String json = wc.getContent("0596002815");
        
        System.out.println(json);
    }
    
}
