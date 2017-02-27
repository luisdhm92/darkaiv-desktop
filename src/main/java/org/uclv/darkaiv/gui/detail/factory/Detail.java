/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.gui.detail.factory;

import java.awt.Color;
import javax.swing.JPanel;

/**
 *
 * @author fenriquez
 */
public abstract class Detail extends JPanel {

    protected String key;
    protected String interfaceName;
    protected long doc_id;
    protected int row;

    public Detail(String key, long doc_id, String interfaceName, int row) {
        this.key = key;
        this.doc_id = doc_id;
        this.interfaceName = interfaceName;
        this.row = row;
        this.setBackground(Color.WHITE);
    }

    public String getKey() {
        return key;
    }

    public long getDoc_id() {
        return doc_id;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public int getRow() {
        return row;
    }
    
    public abstract void resize(int new_with);
}
