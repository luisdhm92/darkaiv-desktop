/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.gui;

import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author fenriquez
 */
public class CollectionsTreeCellRenderer extends DefaultTreeCellRenderer {

    public CollectionsTreeCellRenderer() {
        setBackgroundNonSelectionColor(null);
    }

    @Override
    public Color getBackground() {
        return null;
    }

    @Override
    public JComponent getTreeCellRendererComponent(JTree tree,
            Object value, boolean selected, boolean expanded,
            boolean isLeaf, int row, boolean focused) {

        JComponent c = (JComponent) super.getTreeCellRendererComponent(tree, value,
                selected, expanded, isLeaf, row, focused);

        TreeEntry entry = (TreeEntry) ((DefaultMutableTreeNode) value).getUserObject();

        switch (entry.getType()) {
            case TreeEntry.ROOT: {
                setBorder(new EmptyBorder(0, 3, 0, 0));
                setIcon(null);
            }
            default: {
                setIcon(entry.getIcon());
            }
        }
        c.setForeground(Color.BLACK);

        return c;
    }
}
