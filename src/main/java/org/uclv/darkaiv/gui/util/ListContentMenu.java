/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.gui.util;

/**
 *
 * @author fenriquez
 */
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import org.uclv.darkaiv.gui.ListEntry;
import org.uclv.darkaiv.gui.Main;
import org.uclv.darkaiv.gui.QualityMetricsDialog;
import org.uclv.darkaiv.gui.RenameCollectionDialog;
import org.uclv.darkaiv.organizer.Organizer;

@SuppressWarnings("serial")
public class ListContentMenu extends JPopupMenu {

    private JMenuItem rename;
    private JMenuItem remove;
    private JMenuItem edit;
    private JMenuItem emptyTrash;

    private JList list;

    public ListContentMenu(final Frame owner) {

        rename = new JMenuItem("Rename Collection...");
        rename.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                RenameCollectionDialog collectionDialog = new RenameCollectionDialog(owner, true, ((ListEntry)list.getSelectedValue()).getValue());
                collectionDialog.setLocation((owner.getLocation().x + (owner.getWidth() / 2)) - (collectionDialog.getWidth() / 2), (owner.getLocation().y + (owner.getHeight() / 2)) - (collectionDialog.getHeight() / 2));
                collectionDialog.setVisible(true);
            }
        });

        remove = new JMenuItem("Remove Collection");
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (JOptionPane.showConfirmDialog(owner, "Remove folder '" + ((ListEntry) list.getSelectedValue()).getValue() + "'? This action can not be reversed.", "Warning", JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
                    ListEntry entry = (ListEntry) list.getSelectedValue();
                    Organizer.instance().deleteCollection(entry.getValue());
                    list.setSelectedIndex(1);
                    Main.instance().reloadCollections();
                    Main.instance().collectionsListValueChanged();
                }
            }
        });

        edit = new JMenuItem("Edit Quality Metrics");
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                ListEntry tmp = (ListEntry) list.getSelectedValue();
                QualityMetricsDialog jd_qualityMetrics = new QualityMetricsDialog(owner, true, tmp.getValue());
                jd_qualityMetrics.setLocation((owner.getLocation().x + (owner.getWidth() / 2)) - (jd_qualityMetrics.getWidth() / 2), (owner.getLocation().y + (owner.getHeight() / 2)) - (jd_qualityMetrics.getHeight() / 2));
                jd_qualityMetrics.setVisible(true);
            }
        });

        emptyTrash = new JMenuItem("Empty Trash");
        emptyTrash.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (Organizer.instance().countDeletedDocuments() > 0) {
                    if (JOptionPane.showConfirmDialog(owner, "Delete " + Organizer.instance().countDeletedDocuments() + " documents from your library?", "Warning", JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
                        ListEntry entry = (ListEntry) list.getSelectedValue();
                        Organizer.instance().emptyTrash(entry.getValue());
                        Main.instance().collectionsListValueChanged();
                    }
                }
            }
        });

        add(rename);
        add(remove);
        addSeparator();
        add(edit);
        add(emptyTrash);
    }

    public void add(JList list) {
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent releasedEvent) {
                if (releasedEvent.getButton() == MouseEvent.BUTTON3) {
                    processClick(releasedEvent);
                }
            }
        });
    }

    private void processClick(MouseEvent event) {
        list = (JList) event.getSource();

        if (list.getSelectedIndex() != Main.mHoveredJListIndex) {
            list.setSelectedIndex(Main.mHoveredJListIndex);
            Main.instance().collectionsListValueChanged();
        }

        rename.setVisible(false);
        remove.setVisible(false);
        edit.setVisible(false);
        emptyTrash.setVisible(false);

        if ((list.getSelectedIndex() > 7) && (list.getSelectedIndex() < list.getModel().getSize() - 4) && list.getModel().getSize() > 11) {
            rename.setVisible(true);
            remove.setVisible(true);
            edit.setVisible(true);
            ((JSeparator) getComponent(2)).setVisible(true);
            show(list, event.getX(), event.getY());
        } else {
            if (list.getSelectedIndex() == list.getModel().getSize() - 1) {
                ((JSeparator) getComponent(2)).setVisible(false);
                emptyTrash.setVisible(true);
                show(list, event.getX(), event.getY());
            }
        }
    }
}
