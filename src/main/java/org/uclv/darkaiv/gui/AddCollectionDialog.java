/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import org.uclv.darkaiv.gui.util.DefaultContextMenu;

/**
 *
 * @author fenriquez
 */
public class AddCollectionDialog extends JDialog {

    private ResourceBundle resources;

    private JTextField nameField;
    private JTextField descriptionField;
    private JButton create;
    private JButton cancel;

    public AddCollectionDialog(Frame owner, boolean modal) {
        super(owner, modal);

        resources = ResourceBundle.getBundle("org.uclv.darkaiv.gui.gui");
        setTitle(resources.getString("gui.dialog.addcollection.text.title"));
        setResizable(false);

        setIconImage(new ImageIcon(getClass().getResource(resources.getString("gui.icon.16.folder"))).getImage());

        JPanel out_panel = new JPanel();
        BorderLayout out_layout = new BorderLayout();
        out_panel.setLayout(out_layout);

        JPanel inside_panel = new JPanel();
        BorderLayout in_layout = new BorderLayout();
        inside_panel.setLayout(in_layout);

        Border border = out_panel.getBorder();
        inside_panel.setBorder(new CompoundBorder(border, new EmptyBorder(15, 20, 0, 20)));

        JPanel labelsPanel = new JPanel();

        GridLayout labelsLayout = new GridLayout(2, 1);
        labelsPanel.setLayout(labelsLayout);

        JLabel name = new JLabel(resources.getString("gui.dialog.addcollection.text.name"), JLabel.RIGHT);
        JLabel description = new JLabel(resources.getString("gui.dialog.addcollection.text.description"), JLabel.RIGHT);
        Border labels_border = name.getBorder();
        Border labels_margin = new EmptyBorder(4, 0, 0, 0);
        name.setBorder(new CompoundBorder(labels_border, labels_margin));
        description.setBorder(new CompoundBorder(labels_border, labels_margin));

        labelsPanel.add(name);
        labelsPanel.add(description);

        JPanel fieldsPanel = new JPanel();
        JPanel namePanel = new JPanel();
        JPanel descriptionPanel = new JPanel();
        fieldsPanel.setPreferredSize(new Dimension(300, 50));
        namePanel.setPreferredSize(new Dimension(300, 50));
        descriptionPanel.setPreferredSize(new Dimension(300, 50));

        GridLayout fieldsLayout = new GridLayout(2, 1);
        fieldsPanel.setLayout(fieldsLayout);

        nameField = new JTextField(36);
        descriptionField = new JTextField(36);

        DefaultContextMenu nameContextMenu = new DefaultContextMenu();
        nameContextMenu.add(nameField);
        DefaultContextMenu descriptionContextMenu = new DefaultContextMenu();
        descriptionContextMenu.add(descriptionField);

        namePanel.add(nameField);
        descriptionPanel.add(descriptionField);
        fieldsPanel.add(namePanel);
        fieldsPanel.add(descriptionPanel);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBorder(new CompoundBorder(border, new EmptyBorder(5, 0, 0, 18)));
        buttonsPanel.setPreferredSize(new Dimension(400, 40));

        FlowLayout buttonsLayout = new FlowLayout();
        buttonsLayout.setAlignment(2);
        buttonsPanel.setLayout(buttonsLayout);

        create = new JButton(resources.getString("gui.dialog.addcollection.text.create"));
        cancel = new JButton(resources.getString("gui.dialog.addcollection.text.cancel"));

        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.instance().createCollectionActionPerformed();
            }
        });

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                Main.instance().jlist_collections.setSelectedIndex(Main.collectionListIndexSelected);
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
                Main.instance().jlist_collections.setSelectedIndex(Main.collectionListIndexSelected);
            }
        });

        buttonsPanel.add(create);
        buttonsPanel.add(cancel);

        inside_panel.add(labelsPanel, BorderLayout.WEST);
        inside_panel.add(fieldsPanel, BorderLayout.CENTER);
        out_panel.add(buttonsPanel, BorderLayout.SOUTH);
        out_panel.add(inside_panel, BorderLayout.CENTER);
        nameField.requestFocus();

        add(out_panel);

        pack();
    }

    public JTextField getNameField() {
        return nameField;
    }

    public JTextField getDescriptionField() {
        return descriptionField;
    }
}
