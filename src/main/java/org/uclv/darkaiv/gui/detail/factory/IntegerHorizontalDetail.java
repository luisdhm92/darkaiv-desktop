/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.gui.detail.factory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import org.uclv.darkaiv.gui.Main;
import org.uclv.darkaiv.gui.util.DefaultContextMenu;
import org.uclv.darkaiv.organizer.Organizer;

/**
 *
 * @author fenriquez
 */
public class IntegerHorizontalDetail extends Detail {

    private JLabel field;
    private JTextField textField;

    public IntegerHorizontalDetail(String key, String interfaceName, long doc_id, String value, int row) {
        super(key, doc_id, interfaceName, row);

        try {
            field = new JLabel(interfaceName, JLabel.LEFT);
            Border border = field.getBorder();
            field.setBorder(new CompoundBorder(border, new EmptyBorder(3, 5, 3, 5)));
            JPanel box = new JPanel(new BorderLayout());
            box.setPreferredSize(new Dimension(53, box.getPreferredSize().height));
            box.setBackground(Color.WHITE);
            box.add(field, BorderLayout.WEST);

            textField = new JTextField();
            textField.setBorder(new CompoundBorder(null, new EmptyBorder(3, 3, 3, 0)));
            //textField.setBorder(null);

            DefaultContextMenu contextMenu = new DefaultContextMenu();
            contextMenu.add(textField);

            textField.setFont(new java.awt.Font("Tahoma", Font.PLAIN, 11));
            IntegerDocument document = new IntegerDocument();
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

            textField.setDocument(document);

            textField.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    textField.setBackground(new java.awt.Color(225, 225, 225));
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    textField.setBackground(new java.awt.Color(255, 255, 255));
                }
            });

            textField.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent evt) {
                    textField.setBorder(new CompoundBorder(new MatteBorder(1, 1, 1, 1, new Color(112, 112, 112)), new EmptyBorder(2, 2, 2, 0)));
                }

                @Override
                public void focusLost(java.awt.event.FocusEvent evt) {
                    textField.setBorder(new CompoundBorder(null, new EmptyBorder(3, 3, 3, 0)));
                }
            });

            BorderLayout bl = new BorderLayout();
            this.setLayout(bl);
            this.add(box, BorderLayout.WEST);
            this.add(textField, BorderLayout.CENTER);
        } catch (BadLocationException ex) {
            Logger.getLogger(TitleDetail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void resize(int new_width) {
        textField.setColumns((int) (new_width - 54) / 8);
    }
}
