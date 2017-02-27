/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.gui;

import java.awt.Color;
import java.awt.Component;
import java.net.URL;
import java.util.ResourceBundle;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import static org.uclv.darkaiv.gui.Main.mHoveredJTableIndex;
import org.uclv.darkaiv.model.Collection;

/**
 *
 * @author fenriquez
 */
public class DocumentsTableCellRenderer extends DefaultTableCellRenderer {

    private static Border mateLeft;
    private static Border mateRight;
    private static Border mateCenter;

    private static ResourceBundle resources;
    private final URL uploadURL;

    private static Color low;
    private static Color medium;
    private static Color high;

    public DocumentsTableCellRenderer() {
        mateLeft = new MatteBorder(1, 1, 1, 0, new Color(153, 209, 255));
        mateRight = new MatteBorder(1, 0, 1, 1, new Color(153, 209, 255));
        mateCenter = new MatteBorder(1, 0, 1, 0, new Color(153, 209, 255));

        resources = ResourceBundle.getBundle("org.uclv.darkaiv.gui.gui");
        uploadURL = getClass().getResource(resources.getString("gui.icon.16.upload"));

        low = new Color(196, 11, 11);
//        medium = new Color(255, 192, 4);
//        medium = new Color(240, 207, 76);
        medium = new Color(198, 162, 17);
        high = new Color(16, 124, 16);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        final JComponent c;
//        c = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//        c.setForeground(new Color(0, 0, 0));

        if (column == 0) {
            if (Integer.parseInt(value.toString()) != 0) {
                String html = String.format("<html><table><td><img src='%s'/><td>", uploadURL);
                c = (JComponent) super.getTableCellRendererComponent(table, html, isSelected, hasFocus, row, column);
                c.setBorder(new EmptyBorder(0, 7, 0, 1));
            } else {
                c = (JComponent) super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
            }
        } else {
            c = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setForeground(new Color(0, 0, 0));
        }
//        if (column == 0) {
//            c.setBorder(new EmptyBorder(0, 14, 0, 1));
//        }
        if (column == 1) {
            c.setBorder(new EmptyBorder(0, 5, 0, 1));
        }

        if (isSelected) {
            c.setBackground(row % 2 == 0 ? new Color(205, 232, 255) : new Color(198, 225, 248));
            if (mHoveredJTableIndex == row) {
                if (column == 0) {
                    c.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 0, 0), new CompoundBorder(mateLeft, new EmptyBorder(0, 6, 0, 1))));
                } else {
                    if (column == 1) {
                        c.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 0, 0), new CompoundBorder(mateCenter, new EmptyBorder(0, 5, 0, 1))));
                    } else {
                        if (column == table.getColumnCount() - 1) {
                            c.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 0, 0), new CompoundBorder(mateRight, new EmptyBorder(0, 1, 0, 1))));
                        } else {
                            c.setBorder(new CompoundBorder(new EmptyBorder(0, 0, 0, 0), new CompoundBorder(mateCenter, new EmptyBorder(0, 1, 0, 1))));
                        }
                    }
                }
            }
        } else {
            if (mHoveredJTableIndex == row) {
                c.setBackground(new Color(221, 235, 247));
            } else {
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(246, 246, 246));
            }
        }

        if (column == 1) {
            int collectionId = ((DocumentsTableModel) table.getModel()).getDocs().get(row).getInteger("collection_id");
            int threshold = Collection.findFirst("id = ?", collectionId).getInteger("threshold");
            double metric = Double.parseDouble(value.toString());

            if (metric >= threshold) {
                c.setForeground(high);
            } else {
                if (metric >= (int) threshold / 2) {
                    c.setForeground(medium);
                } else {
                    c.setForeground(low);
                }
            }
        }

        return c;
    }
}
