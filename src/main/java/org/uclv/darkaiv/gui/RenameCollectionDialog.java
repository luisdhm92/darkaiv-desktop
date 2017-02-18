/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import org.uclv.darkaiv.gui.util.DefaultContextMenu;
import org.uclv.darkaiv.organizer.Organizer;

/**
 *
 * @author fenriquez
 */
public class RenameCollectionDialog extends JDialog {

    private ResourceBundle gui_resources;
    private JTextField nameField;
    private JLabel info;
    private JButton save;
    private JButton cancel;

    public RenameCollectionDialog(final Frame owner, boolean modal, final String actualName) {
        super(owner, modal);

        gui_resources = ResourceBundle.getBundle("org.uclv.darkaiv.gui.gui");
        setTitle(gui_resources.getString("gui.dialog.rename.text.title"));
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel namePanel = new JPanel();
        namePanel.setBorder(new CompoundBorder(namePanel.getBorder(), new EmptyBorder(5, 5, 5, 5)));

        JLabel name = new JLabel(gui_resources.getString("gui.dialog.rename.text.new"), JLabel.RIGHT);
        Border labels_border = name.getBorder();
        Border labels_margin = new EmptyBorder(4, 0, 0, 0);
        name.setBorder(new CompoundBorder(labels_border, labels_margin));

        info = new JLabel(gui_resources.getString("gui.dialog.rename.text.unique"));
        info.setBorder(new CompoundBorder(info.getBorder(), new EmptyBorder(0, 9, 0, 0)));
        info.setForeground(new Color(188, 52, 52));

        Document document = new PlainDocument();
        try {
            if (actualName == null) {
                document.insertString(0, "", null);
            } else {
                document.insertString(0, actualName, null);
            }
        } catch (BadLocationException ex) {
            Logger.getLogger(RenameCollectionDialog.class.getName()).log(Level.SEVERE, null, ex);
        }

        document.addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent de) {
                try {
                    if (de.getDocument().getText(0, de.getDocument().getLength()).equals(actualName)) {
                        info.setVisible(false);
                        save.setEnabled(true);
                    } else {
                        if (Organizer.instance().existCollection(de.getDocument().getText(0, de.getDocument().getLength()))) {
                            info.setVisible(true);
                            save.setEnabled(false);
                        } else {
                            info.setVisible(false);
                            save.setEnabled(true);
                        }
                    }
                } catch (BadLocationException ex) {
                    Logger.getLogger(RenameCollectionDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                try {
                    if (de.getDocument().getText(0, de.getDocument().getLength()).equals(actualName)) {
                        info.setVisible(false);
                        save.setEnabled(true);
                    } else {
                        if (Organizer.instance().existCollection(de.getDocument().getText(0, de.getDocument().getLength()))) {
                            info.setVisible(true);
                            save.setEnabled(false);
                        } else {
                            info.setVisible(false);
                            save.setEnabled(true);
                        }
                    }
                } catch (BadLocationException ex) {
                    Logger.getLogger(RenameCollectionDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                try {
                    if (de.getDocument().getText(0, de.getDocument().getLength()).equals(actualName)) {
                        info.setVisible(false);
                        save.setEnabled(true);
                    } else {
                        if (Organizer.instance().existCollection(de.getDocument().getText(0, de.getDocument().getLength()))) {
                            info.setVisible(true);
                            save.setEnabled(false);
                        } else {
                            info.setVisible(false);
                            save.setEnabled(true);
                        }
                    }
                } catch (BadLocationException ex) {
                    Logger.getLogger(RenameCollectionDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        nameField = new JTextField(44);
        nameField.setDocument(document);
        nameField.setCaretPosition(nameField.getDocument().getLength());

        DefaultContextMenu contextMenu = new DefaultContextMenu();
        contextMenu.add(nameField);

        namePanel.add(name);
        namePanel.add(nameField);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBorder(new CompoundBorder(buttonsPanel.getBorder(), new EmptyBorder(5, 5, 5, 5)));

        save = new JButton(gui_resources.getString("gui.dialog.rename.text.save"));
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nameField.getText() == null || nameField.getText().equals("")) {
                    JOptionPane.showMessageDialog(owner, gui_resources.getString("gui.dialog.rename.text.empty"), "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    Organizer.instance().renameCollection(actualName, nameField.getText());
                    setVisible(false);
                    Main.instance().reloadCollections();
                    Main.instance().setSelectedCollection(nameField.getText());
                    Main.instance().collectionsListValueChanged();
                }
            }
        });
        cancel = new JButton(gui_resources.getString("gui.dialog.rename.text.cancel"));
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        buttonsPanel.add(save);
        buttonsPanel.add(cancel);

        add(namePanel, BorderLayout.NORTH);
        add(info, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
        pack();
        info.setVisible(false);
    }
}
