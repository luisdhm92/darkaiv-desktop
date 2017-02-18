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
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import org.uclv.darkaiv.dspace.Collection;
import org.uclv.darkaiv.model.Document;

/**
 *
 * @author admin
 */
public class PublicationDialog extends JDialog {

    private ResourceBundle resource;
    private ResourceBundle gui_resources;
    private String instance;
    private String server;
    private JLabel instanceLabel;
    private JLabel instanceValueLabel;
    private JLabel address;
    private JLabel addressValue;
    private JLabel collectionLabel;
    private static JTextField dspace;
    private JButton select;
    private static JButton publish;
    private JButton cancel;

    private static Collection collection;

    public PublicationDialog(final Frame owner, boolean modal, final String title, final int x, final int whith, final int y, final int heigth, final List<Document> docs, final int[] indexes) throws MalformedURLException {
        super(owner, modal);
        setTitle(title);
        setPreferredSize(new Dimension(460, 160));
        setMinimumSize(new Dimension(450, 160));
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel outPanel = new JPanel(new BorderLayout());
        outPanel.setBorder(new CompoundBorder(outPanel.getBorder(), new EmptyBorder(5, 5, 0, 5)));

        File file = new File("./config/dspace/");
        URL[] urls = {file.toURI().toURL()};
        ClassLoader loader = new URLClassLoader(urls);
        resource = ResourceBundle.getBundle("dspace", Locale.getDefault(), loader);
        gui_resources = ResourceBundle.getBundle("org.uclv.darkaiv.gui.gui");

        instance = resource.getString("dspace.server.instance");
        server = resource.getString("dspace.server.url.gui");

        JPanel topJPanel = new JPanel(new BorderLayout());
        JPanel instancePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        instanceLabel = new JLabel(gui_resources.getString("gui.dialog.publication.text.instance"));
        instanceLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        instanceValueLabel = new JLabel(instance);
        instanceValueLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        instancePanel.add(instanceLabel);
        instancePanel.add(instanceValueLabel);

        JPanel addressPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        address = new JLabel(gui_resources.getString("gui.dialog.publication.text.address"));
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

        topJPanel.add(instancePanel, BorderLayout.NORTH);
        topJPanel.add(addressPanel, BorderLayout.SOUTH);

        collectionLabel = new JLabel(gui_resources.getString("gui.dialog.publication.text.collection"));
        collectionLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
        dspace = new JTextField(gui_resources.getString("gui.dialog.publication.text.select"), 30);
        dspace.setEditable(false);
        dspace.setForeground(Color.GRAY);
        select = new JButton(gui_resources.getString("gui.dialog.publication.text.selectb"));
        select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SelectCollectionDialog jd_select = new SelectCollectionDialog(owner, true);
                    jd_select.setLocation((x + (whith / 2)) - (jd_select.getWidth() / 2), (y + (heigth / 2)) - (jd_select.getHeight() / 2));
                    jd_select.pack();
                    jd_select.setVisible(true);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(PublicationDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        JPanel collectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        collectionPanel.setBorder(new CompoundBorder(collectionPanel.getBorder(), new EmptyBorder(6, 0, 0, 0)));
        collectionPanel.add(collectionLabel);
        collectionPanel.add(dspace);
        collectionPanel.add(select);

        publish = new JButton(gui_resources.getString("gui.dialog.publication.text.publish"));
        publish.setEnabled(false);
        publish.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PublishingDialog publishingDialog = new PublishingDialog(owner, true, title, collection, docs, indexes);
                publishingDialog.setLocation((x + (whith / 2)) - (publishingDialog.getWidth() / 2), (y + (heigth / 2)) - (publishingDialog.getHeight() / 2));
                publishingDialog.pack();
                publishingDialog.setVisible(true);

                ReportDialog reportDialog = new ReportDialog(owner, true, collection.getName(), PublishingDialog.published, PublishingDialog.bitstream, PublishingDialog.unpublished, PublishingDialog.url, indexes.length);
                reportDialog.setLocation((x + (whith / 2)) - (reportDialog.getWidth() / 2), (y + (heigth / 2)) - (reportDialog.getHeight() / 2));
                reportDialog.pack();
                reportDialog.setVisible(true);

                Main.instance().collectionsListValueChanged();
                setVisible(false);
            }
        });
        cancel = new JButton(gui_resources.getString("gui.dialog.publication.text.cancel"));
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(publish);
        bottomPanel.add(cancel);

        outPanel.add(topJPanel, BorderLayout.NORTH);
        outPanel.add(collectionPanel, BorderLayout.CENTER);
        outPanel.add(bottomPanel, BorderLayout.SOUTH);

        this.add(outPanel, BorderLayout.CENTER);
    }

    public static void setCollection(Collection collection) {
        PublicationDialog.collection = collection;
        publish.setEnabled(true);
        dspace.setText(PublicationDialog.collection.getName());
        dspace.setCaretPosition(0);
    }
}
