/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author admin
 */
public class ReportDialog extends JDialog {

    private ResourceBundle resource;
    private ResourceBundle gui_resources;
    private String instance;
    private String server;
    private JLabel instanceLabel;
    private JLabel instanceValueLabel;
    private JLabel address;
    private JLabel addressValue;
    private JLabel collectionLabel;
    private JLabel img;
    private JLabel statusJLabel;
//    private JLabel dspace;
    private JLabel docsAdded;
    private JLabel docsAddedValue;
    private JLabel docsFail;
    private JLabel docsFailValue;
    private JLabel totalDocs;
    private JLabel totalDocsValue;
    private JTextArea textArea;
    private JButton close;

    public ReportDialog(Frame owner, boolean modal, String collection, List<String> published, List<String> bitstream, List<String> unpublished, List<String> url, int totalDocsInt) {
        super(owner, modal);

        try {
            setTitle("Report");
            setResizable(false);
            setLayout(new BorderLayout());

            File file = new File("./config/dspace/");
            URL[] urls = {file.toURI().toURL()};
            ClassLoader loader = new URLClassLoader(urls);
            resource = ResourceBundle.getBundle("dspace", Locale.getDefault(), loader);
            gui_resources = ResourceBundle.getBundle("org.uclv.darkaiv.gui.gui");

            instance = resource.getString("dspace.server.instance");
            server = resource.getString("dspace.server.url.gui");

            JPanel top = new JPanel(new BorderLayout());
            JPanel statusPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
            statusPane.setBorder(new CompoundBorder(statusPane.getBorder(), new EmptyBorder(0, 5, 0, 0)));

            if (totalDocsInt - published.size() == 0) {
                img = new JLabel(new ImageIcon(getClass().getResource(gui_resources.getString("gui.icon.16.tick"))));
                statusJLabel = new JLabel(gui_resources.getString("gui.dialog.report.text.success"));
                statusJLabel.setForeground(new Color(17, 155, 34));
            } else {
                if ((unpublished.size() + bitstream.size() + url.size()) > 0) {
                    statusJLabel = new JLabel(gui_resources.getString("gui.dialog.report.text.error"));
                    img = new JLabel(new ImageIcon(getClass().getResource(gui_resources.getString("gui.icon.16.crosscircle"))));
                } else {
                    statusJLabel = new JLabel(gui_resources.getString("gui.dialog.report.text.canceled"));
                    img = new JLabel(new ImageIcon(getClass().getResource(gui_resources.getString("gui.icon.16.exclamation"))));
                }
                statusJLabel.setForeground(new Color(188, 52, 52));
            }

            statusPane.add(img);
            statusPane.add(statusJLabel);

            JPanel serverPanel = new JPanel(new BorderLayout());
            serverPanel.setBorder(new CompoundBorder(serverPanel.getBorder(), new EmptyBorder(0, 5, 0, 0)));

            JPanel instancePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            instanceLabel = new JLabel(gui_resources.getString("gui.dialog.report.text.instance"));
            instanceValueLabel = new JLabel(instance);
            instancePanel.add(instanceLabel);
            instancePanel.add(instanceValueLabel);

            JPanel addressPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            address = new JLabel(gui_resources.getString("gui.dialog.report.text.address"));
            addressValue = new JLabel();
            addressValue.setText("<html><a href=\"\">" + server + "</a></html>");
            addressValue.setCursor(new Cursor(Cursor.HAND_CURSOR));
            addressValue.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        Desktop.getDesktop().browse(new URI(server));
                    } catch (URISyntaxException exception) {
                    } catch (IOException exception) {
                    }
                }
            });
            addressPanel.add(address);
            addressPanel.add(addressValue);

            JPanel collectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            collectionLabel = new JLabel(gui_resources.getString("gui.dialog.report.text.collection"));
            textArea = new JTextArea(collection);
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);
            textArea.setBackground(new Color(240, 240, 240));
            textArea.setEditable(false);
            textArea.setFocusable(false);
            textArea.setBorder(new CompoundBorder(null, new EmptyBorder(3, 3, 3, 3)));
            textArea.setFont(new java.awt.Font("Tahoma", Font.PLAIN, 11));
            Dimension oldPrefSize = textArea.getPreferredSize();
            Dimension newPrefSize = new Dimension(250, oldPrefSize.height);
            textArea.setSize(newPrefSize);

            collectionPanel.add(collectionLabel);
            collectionPanel.add(textArea);

            serverPanel.add(instancePanel, BorderLayout.NORTH);
            serverPanel.add(addressPanel, BorderLayout.CENTER);
            serverPanel.add(collectionPanel, BorderLayout.SOUTH);

            top.add(statusPane, BorderLayout.NORTH);
            top.add(serverPanel, BorderLayout.SOUTH);

            JPanel details = new JPanel(new BorderLayout());
            details.setBorder(new CompoundBorder(details.getBorder(), new EmptyBorder(0, 5, 0, 0)));

            JPanel total = new JPanel(new FlowLayout(FlowLayout.LEFT));
            totalDocs = new JLabel(gui_resources.getString("gui.dialog.report.text.total"));
            totalDocsValue = new JLabel(totalDocsInt + "");
            total.add(totalDocs);
            total.add(totalDocsValue);

            JPanel added = new JPanel(new FlowLayout(FlowLayout.LEFT));
            docsAdded = new JLabel(gui_resources.getString("gui.dialog.report.text.added"));
            docsAddedValue = new JLabel(published.size() + "");
            added.add(docsAdded);
            added.add(docsAddedValue);

            JPanel fail = new JPanel(new FlowLayout(FlowLayout.LEFT));
            docsFail = new JLabel(gui_resources.getString("gui.dialog.report.text.fails"));
            docsFailValue = new JLabel(unpublished.size() + bitstream.size() + url.size() + "");
            fail.add(docsFail);
            fail.add(docsFailValue);

            details.add(total, BorderLayout.NORTH);
            details.add(added, BorderLayout.CENTER);
            details.add(fail, BorderLayout.SOUTH);

            JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
            close = new JButton(gui_resources.getString("gui.dialog.report.text.close"));
            close.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                }
            });
            buttonPane.add(close);

            this.add(top, BorderLayout.NORTH);
            this.add(details, BorderLayout.CENTER);
            this.add(buttonPane, BorderLayout.SOUTH);

            setPreferredSize(new Dimension(370, textArea.getPreferredSize().height + 235));
            setMinimumSize(new Dimension(370, textArea.getPreferredSize().height + 235));
        } catch (MalformedURLException ex) {
            Logger.getLogger(ReportDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
