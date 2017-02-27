/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.qualifier;

/**
 *
 * @author daniel
 */
public class Qualifier {

    private QualifyStrategy qualifierStrategy;

    public Qualifier() {
    }

    public QualifyStrategy getQualifierStrategy() {
        return qualifierStrategy;
    }

    public void setQualifierStrategy(QualifyStrategy qualifierStrategy) {
        this.qualifierStrategy = qualifierStrategy;
    }
}
