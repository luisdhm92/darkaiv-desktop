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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import org.uclv.darkaiv.gui.Main;
import org.uclv.darkaiv.gui.util.WrapLayout;
import org.uclv.darkaiv.organizer.Organizer;

/**
 *
 * @author fenriquez
 */
public class AuthorsDetail extends Detail {

    private JLabel field;
    private JTextArea textArea;
    private JTextField textField;
    Document document;

    public AuthorsDetail(String key, String interfaceName, final long doc_id, String value, int row) {
        super(key, doc_id, interfaceName, row);
        try {
            field = new JLabel(interfaceName, JLabel.LEFT);
            field.setFont(new java.awt.Font("Tahoma", 1, 11));
            field.setBorder(new CompoundBorder(field.getBorder(), new EmptyBorder(5, 0, 5, 0)));

            textField = new JTextField();
            textField.setBorder(new CompoundBorder(null, new EmptyBorder(3, 3, 3, 0)));
            textField.setText(Organizer.instance().getAuthorsAsString(doc_id));
            textField.setCaretPosition(0);

            textArea = new JTextArea();
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);
            textArea.setToolTipText("Last Name, First Name");

            textArea.setFont(new java.awt.Font("Tahoma", Font.PLAIN, 11));

            document = new PlainDocument();
            document.insertString(0, Organizer.instance().getAuthorsAsStringWNL(doc_id), null);

            document.addDocumentListener(new DocumentListener() {

                @Override
                public void insertUpdate(DocumentEvent de) {
                    try {
                        Organizer.instance().updateAuthors(getDoc_id(), de.getDocument().getText(0, de.getDocument().getLength()));
                        Main.instance().update((org.uclv.darkaiv.model.Document) org.uclv.darkaiv.model.Document.findById(getDoc_id()), getRow(), false);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(AuthorsDetail.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(AuthorsDetail.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent de) {
                    try {
                        Organizer.instance().updateAuthors(getDoc_id(), de.getDocument().getText(0, de.getDocument().getLength()));
                        Main.instance().update((org.uclv.darkaiv.model.Document) org.uclv.darkaiv.model.Document.findById(getDoc_id()), getRow(), false);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(AuthorsDetail.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(AuthorsDetail.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                @Override
                public void changedUpdate(DocumentEvent de) {
                    try {
                        Organizer.instance().updateAuthors(getDoc_id(), de.getDocument().getText(0, de.getDocument().getLength()));
                        Main.instance().update((org.uclv.darkaiv.model.Document) org.uclv.darkaiv.model.Document.findById(getDoc_id()), getRow(), false);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(AuthorsDetail.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(AuthorsDetail.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            textArea.setDocument(document);
            textArea.setVisible(false);

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
                    try {
                        KeyEvent ke = new KeyEvent(evt.getComponent(), KeyEvent.KEY_PRESSED,
                                System.currentTimeMillis(), InputEvent.CTRL_MASK,
                                KeyEvent.VK_F1, KeyEvent.CHAR_UNDEFINED);
                        evt.getComponent().dispatchEvent(ke);
                    } catch (Throwable e1) {
//                        e1.printStackTrace();
                    }
                }

                @Override
                public void focusLost(java.awt.event.FocusEvent evt) {
//                    textArea.setBorder(new CompoundBorder(null, new EmptyBorder(3, 3, 3, 3)));
                    evt.getComponent().setVisible(false);
                    textField.setText(Organizer.instance().getAuthorsAsString(doc_id));
                    textField.setCaretPosition(0);
                    textField.setVisible(true);
                }
            });

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
//                    textField.setBorder(new CompoundBorder(new MatteBorder(1, 1, 1, 1, new Color(112, 112, 112)), new EmptyBorder(2, 2, 2, 0)));
                    evt.getComponent().setVisible(false);
                    textArea.setVisible(true);
                    textArea.requestFocus();
                }
            });

            BorderLayout bl = new BorderLayout();
            JPanel pane = new JPanel(new WrapLayout());
            pane.setBackground(Color.WHITE);
            pane.add(textField);
            pane.add(textArea);
            this.setLayout(bl);
            this.add(field, BorderLayout.NORTH);
            this.add(pane, BorderLayout.CENTER);
        } catch (BadLocationException ex) {
            Logger.getLogger(TitleDetail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void resize(int new_width) {
        textField.setColumns((int) (new_width - 10) / 8);
        Dimension oldPrefSize = textArea.getPreferredSize();
        Dimension newPrefSize = new Dimension((new_width - 10), oldPrefSize.height);
        textArea.setSize(newPrefSize);
    }
}
