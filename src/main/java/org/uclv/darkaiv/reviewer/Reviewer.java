/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.reviewer;

/**
 *
 * @author daniel
 */
public class Reviewer {

    private ReviewStrategy reviwerStrategy;

    public Reviewer() {
    }

    public ReviewStrategy getReviwerStrategy() {
        return reviwerStrategy;
    }

    public void setReviwerStrategy(ReviewStrategy reviwerStrategy) {
        this.reviwerStrategy = reviwerStrategy;
    }
}
