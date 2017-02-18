/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ResourceBundle;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import static org.uclv.darkaiv.gui.Main.mHoveredJListIndex;

/**
 *
 * @author fenriquez
 */
public class ListEntryCellRenderer extends JLabel implements ListCellRenderer {

    private ResourceBundle resources;
    private Border def;
    private Border iconed;
    private Border iconedMouseOver;
    private Border mate;
    private Border text;
    private Border separator;
    private Border new_collection;

    public ListEntryCellRenderer() {
        resources = ResourceBundle.getBundle("org.uclv.darkaiv.gui.gui");

        def = getBorder();
        text = new EmptyBorder(3, 15, 3, 0);
        mate = new MatteBorder(1, 1, 1, 1, new Color(153, 209, 255));
        iconed = new EmptyBorder(3, 30, 3, 0);
        iconedMouseOver = new CompoundBorder(mate, new EmptyBorder(2, 29, 2, 0));
        new_collection = new EmptyBorder(3, 50, 3, 0);
        separator = new EmptyBorder(20, 0, 0, 0);
        setBorder(new CompoundBorder(def, text));
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected,
            boolean cellHasFocus) {
        ListEntry entry = (ListEntry) value;

        Color backgroundColor = mHoveredJListIndex == index ? new Color(221, 235, 247) : Color.white;

        if (!value.toString().equals(resources.getString("gui.default.collection.separator"))) {
            setText(value.toString());
        } else {
            setText(null);
        }
        setIcon(entry.getIcon());

        if (entry.getValue().equals(resources.getString("gui.default.collection.mine")) || entry.getValue().equals(resources.getString("gui.default.collection.trash")) || entry.getValue().equals(resources.getString("gui.default.collection.default"))) {
            setBorder(new CompoundBorder(def, text));
            setFont(new Font("Tahoma", 1, 11));
            setForeground(new Color(60, 60, 60));
            if (isSelected) {
                setBackground(new Color(205, 232, 255));
            } else {
                setBackground(backgroundColor);
            }
        } else {
            setFont(new Font("Tahoma", 0, 11));
            setForeground(new Color(0, 0, 0));

            if (value.toString().equals(resources.getString("gui.default.collection.separator"))) {
                setBorder(new CompoundBorder(def, separator));
                setBackground(list.getBackground());
            } else {
                if (isSelected) {
                    setBackground(new Color(205, 232, 255));
                    if (mHoveredJListIndex == index) {
                        setBorder(new CompoundBorder(def, iconedMouseOver));
                    } else {
                        setBorder(new CompoundBorder(def, iconed));
                    }
                } else {
                    setBorder(new CompoundBorder(def, iconed));
                    setBackground(backgroundColor);
                }
                if (value.toString().equals(resources.getString("gui.default.collection.addnewcollection"))) {
                    setBorder(new CompoundBorder(def, new_collection));
                }
            }
        }

        setEnabled(list.isEnabled());
        setOpaque(true);

        return this;
    }
}
