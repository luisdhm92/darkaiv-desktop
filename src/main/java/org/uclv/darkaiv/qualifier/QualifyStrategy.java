/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.qualifier;

import org.uclv.darkaiv.model.Document;

/**
 *
 * @author daniel
 */
public interface QualifyStrategy {

    public double getMetric(Document doc);
}
