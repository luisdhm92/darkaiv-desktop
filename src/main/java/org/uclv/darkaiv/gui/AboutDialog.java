/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author fenriquez
 */
public class AboutDialog extends JDialog {

    ResourceBundle resources;

    private JLabel appName;
    private JLabel icon;
    private JLabel appVersion;
    private JLabel right;
    private JButton close;

    public AboutDialog(Frame owner, boolean modal) {
        super(owner, modal);

        resources = ResourceBundle.getBundle("org.uclv.darkaiv.gui.gui");
        setTitle(resources.getString("gui.dialog.aboutdialog.text.title"));
        setResizable(false);
        setResizable(false);
        setPreferredSize(new Dimension(270, 135));
        setMinimumSize(new Dimension(270, 135));
        setLayout(new BorderLayout(5, 5));

        icon = new JLabel(new ImageIcon(getClass().getResource(resources.getString("gui.icon.16.information"))));
        icon.setBorder(new CompoundBorder(icon.getBorder(), new EmptyBorder(0, 10, 0, 0)));
        appName = new JLabel(resources.getString("gui.dialog.aboutdialog.text.name"));
        appName.setFont(new Font("Tahoma", Font.BOLD, 11));
        appVersion = new JLabel(resources.getString("gui.dialog.aboutdialog.text.version"));
        right = new JLabel(resources.getString("gui.dialog.aboutdialog.text.rights"));

        JPanel info = new JPanel(new BorderLayout(2, 5));
        info.setBorder(new CompoundBorder(info.getBorder(), new EmptyBorder(10, 10, 10, 10)));
        info.add(appName, BorderLayout.NORTH);
        info.add(appVersion, BorderLayout.CENTER);
        info.add(right, BorderLayout.SOUTH);

        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
        close = new JButton(resources.getString("gui.dialog.aboutdialog.text.close"));
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        buttonPane.add(close);

        this.add(icon, BorderLayout.WEST);
        this.add(info, BorderLayout.CENTER);
        this.add(buttonPane, BorderLayout.SOUTH);
    }
}
