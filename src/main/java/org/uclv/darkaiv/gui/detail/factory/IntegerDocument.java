/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.gui.detail.factory;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author fenriquez
 */
public class IntegerDocument extends PlainDocument {

    @Override
    public void insertString(int offs, String str, AttributeSet a)
            throws BadLocationException {
        if (str != null) {
            try {
                Integer.decode(str);
                super.insertString(offs, str, a);
            } catch (NumberFormatException ex) {
//                Toolkit.getDefaultToolkit().beep();
            }
        }
    }
}
