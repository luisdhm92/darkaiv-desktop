/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.gui.detail.factory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import org.uclv.darkaiv.gui.Main;
import org.uclv.darkaiv.gui.util.DefaultContextMenu;
import org.uclv.darkaiv.organizer.Organizer;

/**
 *
 * @author fenriquez
 */
public class TitleDetail extends Detail {

    private JTextArea textArea;

    public TitleDetail(String key, String interfaceName, long doc_id, String value, int row) {
        super(key, doc_id, interfaceName, row);

        try {
            textArea = new JTextArea();
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);
            textArea.setBorder(new CompoundBorder(null, new EmptyBorder(3, 3, 3, 3)));

            DefaultContextMenu contextMenu = new DefaultContextMenu();
            contextMenu.add(textArea);

            Document document = new PlainDocument();
            if (value == null) {
                document.insertString(0, "", null);
            } else {
                document.insertString(0, value, null);
            }
            document.addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent de) {
                    try {
                        Main.instance().update(Organizer.instance().update(getDoc_id(), getKey(), de.getDocument().getText(0, de.getDocument().getLength())), getRow(), false);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(TitleDetail.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent de) {
                    try {
                        Main.instance().update(Organizer.instance().update(getDoc_id(), getKey(), de.getDocument().getText(0, de.getDocument().getLength())), getRow(), false);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(TitleDetail.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                @Override
                public void changedUpdate(DocumentEvent de) {
                    try {
                        Main.instance().update(Organizer.instance().update(getDoc_id(), getKey(), de.getDocument().getText(0, de.getDocument().getLength())), getRow(), false);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(TitleDetail.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            textArea.setFont(new java.awt.Font("Tahoma", 1, 15));
            textArea.setDocument(document);

            textArea.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    textArea.setBackground(new java.awt.Color(225, 225, 225));
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    textArea.setBackground(new java.awt.Color(255, 255, 255));
                }
            });

            textArea.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent evt) {
                    textArea.setBorder(new CompoundBorder(new MatteBorder(1, 1, 1, 1, new Color(112, 112, 112)), new EmptyBorder(2, 2, 2, 2)));
                }

                @Override
                public void focusLost(java.awt.event.FocusEvent evt) {
                    textArea.setBorder(new CompoundBorder(null, new EmptyBorder(3, 3, 3, 3)));
                }
            });

            BorderLayout bl = new BorderLayout();
            this.setLayout(bl);
            this.add(textArea, BorderLayout.CENTER);
        } catch (BadLocationException ex) {
            Logger.getLogger(TitleDetail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void resize(int new_width) {
        Dimension oldPrefSize = textArea.getPreferredSize();
        Dimension newPrefSize = new Dimension(new_width, oldPrefSize.height);
        textArea.setSize(newPrefSize);
    }
}
