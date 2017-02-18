/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.gui;

import java.awt.Dimension;
import java.net.URL;
import java.util.ResourceBundle;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author fenriquez
 */
public class DocumentsTableHeaderRenderer implements TableCellRenderer {

    private ResourceBundle resources;

    private final URL cloud;

    public DocumentsTableHeaderRenderer() {
        this.resources = ResourceBundle.getBundle("org.uclv.darkaiv.gui.gui");

        cloud = getClass().getResource(resources.getString("gui.icon.16.uploadcloud"));
    }

    @Override
    public JComponent getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        TableCellRenderer r = table.getTableHeader().getDefaultRenderer();
        JComponent c;

        if (column == 0) {
            String html = String.format("<html><table><td><img src='%s'/><td>", cloud);
            c = (JComponent) r.getTableCellRendererComponent(table, html, isSelected, hasFocus, row, column);
            c.setPreferredSize(new Dimension(c.getWidth(), 25));
//            c.setToolTipText("Number of times have been published");
            c.setToolTipText(resources.getString("gui.tooltiptext.published"));
        } else {
            c = (JComponent) r.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (column == 1) {
                c.setToolTipText(resources.getString("gui.tooltiptext.qualityMetrics"));
            } else {
                c.setToolTipText(null);
            }
        }

        return c;
    }
}
