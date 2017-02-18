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
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;

@SuppressWarnings("serial")
public class DefaultContextMenu extends JPopupMenu {

    private Clipboard clipboard;

    private JMenuItem cut;
    private JMenuItem copy;
    private JMenuItem paste;
    private JMenuItem delete;
    private JMenuItem selectAll;

    private JTextComponent jTextComponent;

    public DefaultContextMenu() {
        clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        cut = new JMenuItem("Cut");
        cut.setEnabled(false);
        cut.setAccelerator(KeyStroke.getKeyStroke("control X"));
        cut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                jTextComponent.cut();
            }
        });

        add(cut);

        copy = new JMenuItem("Copy");
        copy.setEnabled(false);
        copy.setAccelerator(KeyStroke.getKeyStroke("control C"));
        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                jTextComponent.copy();
            }
        });

        add(copy);

        paste = new JMenuItem("Paste");
        paste.setEnabled(false);
        paste.setAccelerator(KeyStroke.getKeyStroke("control V"));
        paste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                jTextComponent.paste();
            }
        });

        add(paste);

        delete = new JMenuItem("Delete");
        delete.setEnabled(false);
        delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                jTextComponent.replaceSelection("");
            }
        });

        add(delete);

        addSeparator();

        selectAll = new JMenuItem("Select All");
        selectAll.setEnabled(false);
        selectAll.setAccelerator(KeyStroke.getKeyStroke("control A"));
        selectAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                jTextComponent.selectAll();
            }
        });

        add(selectAll);
    }

    public void add(JTextComponent jTextComponent) {

        jTextComponent.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent releasedEvent) {
                if (releasedEvent.getButton() == MouseEvent.BUTTON3) {
                    processClick(releasedEvent);
                }
            }
        });
    }

    private void processClick(MouseEvent event) {
        jTextComponent = (JTextComponent) event.getSource();

//        if (jTextComponent.hasFocus()) {
        boolean enableCut = false;
        boolean enableCopy = false;
        boolean enablePaste = false;
        boolean enableDelete = false;
        boolean enableSelectAll = false;

        String selectedText = jTextComponent.getSelectedText();
        String text = jTextComponent.getText();

        if (text != null) {
            if (text.length() > 0) {
                enableSelectAll = true;
            }
        }

        if (selectedText != null) {
            if (selectedText.length() > 0) {
                enableCut = true;
                enableCopy = true;
                enableDelete = true;
            }
        }

        try {
            if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                enablePaste = true;
            }
        } catch (Exception exception) {
        }

        cut.setEnabled(enableCut);
        copy.setEnabled(enableCopy);
        paste.setEnabled(enablePaste);
        delete.setEnabled(enableDelete);
        selectAll.setEnabled(enableSelectAll);

        jTextComponent.requestFocus();
        show(jTextComponent, event.getX(), event.getY());
//        }
    }
}
