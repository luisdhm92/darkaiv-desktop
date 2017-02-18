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
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import org.uclv.darkaiv.gui.DocumentsTableModel;
import org.uclv.darkaiv.gui.ListEntry;
import org.uclv.darkaiv.gui.Main;
import org.uclv.darkaiv.organizer.Organizer;

@SuppressWarnings("serial")
public class TableContentMenu extends JPopupMenu {

    private JMenuItem selectAll;
    private JMenu marcAs;
    private JMenuItem delete;
    private JMenuItem restore;

    private JMenuItem needReview;
    private JMenuItem reviewed;

    private JMenuItem openFile;
    private JMenuItem openFolder;

    private JTable table;

    public TableContentMenu(final JFrame owner) {
        openFile = new JMenuItem("Open File");
        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                org.uclv.darkaiv.model.Document document = ((DocumentsTableModel) table.getModel()).getDocs().get(table.getSelectedRow());
                List<org.uclv.darkaiv.model.File> files = document.getAll(org.uclv.darkaiv.model.File.class);
                if (document.validateFile()) {
                    if (!files.isEmpty()) {
                        java.io.File file = new java.io.File(files.get(0).getString("path"));
                        try {
                            Desktop.getDesktop().open(file);
                        } catch (IOException ex) {
                            Logger.getLogger(TableContentMenu.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(owner, "The file was not found at: " + files.get(0).getString("path"), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        openFolder = new JMenuItem("Open Containing Folder");
        openFolder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                org.uclv.darkaiv.model.Document document = ((DocumentsTableModel) table.getModel()).getDocs().get(table.getSelectedRow());
                List<org.uclv.darkaiv.model.File> files = document.getAll(org.uclv.darkaiv.model.File.class);
                if (document.validateFile()) {
                    if (!files.isEmpty()) {
                        java.io.File file = new java.io.File(files.get(0).getString("path"));
                        try {
                            Desktop.getDesktop().open(file.getParentFile());
                        } catch (IOException ex) {
                            Logger.getLogger(TableContentMenu.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(owner, "The file was not found at: " + files.get(0).getString("path"), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        selectAll = new JMenuItem("Select all");
        selectAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                table.selectAll();
                Main.instance().tableDocumentsMouseReleased();
            }
        });

        marcAs = new JMenu("Marc As");

        needReview = new JMenuItem("Needs Review");
        needReview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                Organizer.instance().markAsNeedsReview(((DocumentsTableModel) table.getModel()).getDocs().get(table.getSelectedRow()).getLongId());
            }
        });

        reviewed = new JMenuItem("Reviewed");
        reviewed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                Organizer.instance().markAsReviewed(((DocumentsTableModel) table.getModel()).getDocs().get(table.getSelectedRow()).getLongId());
                Main.instance().collectionsListValueChanged();
            }
        });

        marcAs.add(needReview);
        marcAs.add(reviewed);

        delete = new JMenuItem("Delete Documents");
        delete.setAccelerator(KeyStroke.getKeyStroke("DELETE"));
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                ListEntry entry = Main.instance().getListEntryAt(Main.collectionListIndexSelected);
                if (entry.getValue().equals("Deleted Documents")) {
                    if (JOptionPane.showConfirmDialog(owner, "Delete " + table.getSelectedRows().length + " documents from your library?", "Warning", JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
                        for (int index : table.getSelectedRows()) {
                            Organizer.instance().deletePermanently(((DocumentsTableModel) table.getModel()).getDocs().get(index).getLongId());
                        }
                    }
                    Main.instance().collectionsListValueChanged();
                } else {
                    for (int index : table.getSelectedRows()) {
                        Organizer.instance().delete(((DocumentsTableModel) table.getModel()).getDocs().get(index).getLongId());
                    }
                    Main.instance().collectionsListValueChanged();
                }
            }
        });

        restore = new JMenuItem("Restore Documents");
        restore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                for (int index : table.getSelectedRows()) {
                    Organizer.instance().restore(((DocumentsTableModel) table.getModel()).getDocs().get(index).getLongId());
                }
                Main.instance().collectionsListValueChanged();
            }
        });

        add(openFile);
        add(openFolder);
        addSeparator();
        add(marcAs);
        addSeparator();
        add(selectAll);
        addSeparator();
        add(delete);
        add(restore);
    }

    public void add(final JTable table) {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent releasedEvent) {
                if (releasedEvent.getButton() == MouseEvent.BUTTON3) {
                    processClick(releasedEvent);
                }
            }
        });
    }

    private void processClick(MouseEvent event) {
        table = (JTable) event.getSource();

        ListEntry entry = Main.instance().getListEntryAt(Main.collectionListIndexSelected);

        boolean enableOpenFile = true;
        boolean enableOpenContainingFolder = true;
        boolean enableMarkAs = true;
        boolean enableDelete = true;
        boolean enableRestore = true;
        boolean enableSelectAll = true;

        switch (table.getSelectedRowCount()) {
            case 0: {
                enableOpenFile = false;
                enableOpenContainingFolder = false;
                enableMarkAs = false;
                enableDelete = false;
                enableRestore = false;
                enableSelectAll = true;
                break;
            }
            case 1: {
                enableOpenFile = true;
                enableOpenContainingFolder = true;
                enableMarkAs = true;
                enableDelete = true;
                enableRestore = true;
                enableSelectAll = true;
                break;
            }
            default: {
                enableOpenFile = false;
                enableOpenContainingFolder = false;
                enableMarkAs = false;
                enableDelete = true;
                enableRestore = true;
                enableSelectAll = true;
                break;
            }
        }
        if (entry.getValue().equals("Deleted Documents")) {
            enableMarkAs = false;
        } else {
            enableRestore = false;
        }

        openFile.setEnabled(enableOpenFile);
        openFolder.setEnabled(enableOpenContainingFolder);
        selectAll.setEnabled(enableSelectAll);
        marcAs.setEnabled(enableMarkAs);
        delete.setEnabled(enableDelete);
        restore.setEnabled(enableRestore);

        show(table, event.getX(), event.getY());
    }
}
