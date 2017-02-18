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
public class TreeEntry {

    public static final int ROOT = 0;
    public static final int COMMUNITY = 1;
    public static final int COLLECTION = 2;

    private String value;
    private ImageIcon icon;
    private int type;
    private Object raw;
    private int id;
    private boolean visited;

    public TreeEntry(String value, ImageIcon icon, int type, Object raw, int id) {
        this.value = value;
        this.icon = icon;
        this.type = type;
        this.raw = raw;
        this.id = id;
        this.visited = false;
    }

    public String getValue() {
        return value;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public int getType() {
        return type;
    }

    public Object getRaw() {
        return raw;
    }

    public int getId() {
        return id;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isVisited() {
        return visited;
    }

    @Override
    public String toString() {
        return value;
    }
}
