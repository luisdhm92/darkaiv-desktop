/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.gui;

import javax.swing.ImageIcon;

/**
 *
 * @author fenriquez
 */
public class ListEntry {

    private String value;
    private ImageIcon icon;

    public ListEntry(String value, ImageIcon icon) {
        this.value = value;
        this.icon = icon;
    }

    public String getValue() {
        return value;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return value;
    }
}
