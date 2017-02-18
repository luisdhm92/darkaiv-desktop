/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JTree;

/**
 *
 * @author fenriquez
 */
public class CollectionsTree extends JTree {

    @Override
    protected void paintComponent(Graphics g) {

        // paint background
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());

        // paint selected node's background and border
        int fromRow = getRowForPath(getSelectionPath());
        if (fromRow != -1) {
            int toRow = fromRow + 1;
            Rectangle fromBounds = getRowBounds(fromRow);
            Rectangle toBounds = getRowBounds(toRow - 1);
            if (fromBounds != null && toBounds != null) {
                g.setColor(new Color(198, 225, 248));
                g.fillRect(0, fromBounds.y, getWidth(), toBounds.y - fromBounds.y + toBounds.height);
                g.setColor(new Color(153, 209, 255));
                g.drawRect(0, fromBounds.y, getWidth() - 1, toBounds.y - fromBounds.y + toBounds.height);
            }
        }
        // perform operation of superclass
        setOpaque(false); // trick not to paint background
        super.paintComponent(g);
        setOpaque(false);
    }
}
