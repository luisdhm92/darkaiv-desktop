/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import org.uclv.darkaiv.dspace.Collection;
import org.uclv.darkaiv.gui.workers.PublishingDocumentsWorker;
import org.uclv.darkaiv.model.Document;

/**
 *
 * @author fenriquez
 */
public class PublishingDialog extends JDialog {

    private JProgressBar progressBar;
    private JLabel status;

    public static List<String> published;
    public static List<String> bitstream;
    public static List<String> unpublished;
    public static List<String> url;

    PublishingDocumentsWorker publishingDocumentsWorker;

    public PublishingDialog(Frame owner, boolean modal, final String title, Collection collection, List<Document> docs, int[] indexes) {
        super(owner, modal);
        setTitle(title);
        setPreferredSize(new Dimension(680, 80));
        setMinimumSize(new Dimension(680, 80));
        setResizable(false);
        setLayout(new BorderLayout());

        progressBar = new JProgressBar();
        progressBar.setPreferredSize(new Dimension(640, 18));

        status = new JLabel(title, JLabel.LEFT);
        status.setBorder(new CompoundBorder(status.getBorder(), new EmptyBorder(10, 10, 10, 10)));

        published = new LinkedList<String>();
        unpublished = new LinkedList<String>();
        url = new LinkedList<String>();
        bitstream = new LinkedList<String>();

        publishingDocumentsWorker = new PublishingDocumentsWorker(indexes, docs, status, progressBar, collection, published, bitstream, unpublished, url);
        publishingDocumentsWorker.addPropertyChangeListener(
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent e) {
                        // if the changed property is progress,
                        // update the progress bar
                        if (e.getPropertyName().equals("progress")) {
                            int newValue = (Integer) e.getNewValue();
                            if (newValue == 100) {
                                setVisible(false);
                            } else {
                                progressBar.setValue(newValue);
                            }
                        } // end if
                    } // end method propertyChange
                } // end anonymous inner class
        ); // end call to addPropertyChangeListener
        publishingDocumentsWorker.execute(); // execute the PrimeCalculator object

        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                publishingDocumentsWorker.cancel(true);
            }
        });

        this.add(status, BorderLayout.NORTH);
        this.add(progressBar, BorderLayout.CENTER);
    }
}
