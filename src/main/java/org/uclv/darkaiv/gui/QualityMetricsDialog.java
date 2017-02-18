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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.uclv.darkaiv.organizer.Organizer;

/**
 *
 * @author fenriquez
 */
public class QualityMetricsDialog extends JDialog {

    private ResourceBundle resources;

    private JLabel completenees;
    private JLabel completeneesValue;
    private JLabel collection;
    private JLabel collectionName;
    private JLabel info;
    JSlider slider;

    private JButton save;
    private JButton cancel;

    public QualityMetricsDialog(Frame owner, boolean modal, final String collection_name) {
        super(owner, modal);
        setTitle("Quality Metrics");
        setResizable(false);

        resources = ResourceBundle.getBundle("org.uclv.darkaiv.gui.gui");

        JPanel out_panel = new JPanel(new BorderLayout());
        JPanel inside_panel = new JPanel(new GridLayout(3, 1));

        inside_panel.setBorder(new CompoundBorder(out_panel.getBorder(), new EmptyBorder(15, 20, 0, 20)));

        JPanel collectionInfo = new JPanel(new BorderLayout());
        JPanel threshold = new JPanel(new BorderLayout());
        JPanel information = new JPanel(new BorderLayout());

        collection = new JLabel(resources.getString("gui.dialog.qualitymetrics.text.collection"));
        collectionName = new JLabel(collection_name);
        collectionName.setBorder(new CompoundBorder(out_panel.getBorder(), new EmptyBorder(0, 5, 0, 0)));
        collectionInfo.add(collection, BorderLayout.WEST);
        collectionInfo.add(collectionName, BorderLayout.CENTER);

        completenees = new JLabel(resources.getString("gui.dialog.qualitymetrics.text.completenees"));
        completenees.setFont(new Font("Tahoma", Font.BOLD, 12));

        completeneesValue = new JLabel(Organizer.instance().getCollectionQualityThreshold(collection_name) + "%");
        completeneesValue.setPreferredSize(new Dimension(40, completeneesValue.getPreferredSize().height));
        completeneesValue.setFont(new Font("Tahoma", Font.BOLD, 12));

        slider = new JSlider(0, 100);
        slider.setBorder(new CompoundBorder(slider.getBorder(), new EmptyBorder(0, 5, 0, 5)));
        slider.setValue(Organizer.instance().getCollectionQualityThreshold(collection_name));

        threshold.add(completenees, BorderLayout.WEST);
        threshold.add(slider, BorderLayout.CENTER);
        threshold.add(completeneesValue, BorderLayout.EAST);

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider slider = (JSlider) e.getSource();
                completeneesValue.setText(String.valueOf(slider.getValue()) + "%");
            }
        });

        info = new JLabel(resources.getString("gui.dialog.qualitymetrics.text.info"));
        information.add(info, BorderLayout.WEST);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBorder(new CompoundBorder(buttonsPanel.getBorder(), new EmptyBorder(5, 0, 0, 18)));
        buttonsPanel.setPreferredSize(new Dimension(400, 40));

        save = new JButton(resources.getString("gui.dialog.qualitymetrics.text.save"));
        cancel = new JButton(resources.getString("gui.dialog.qualitymetrics.text.cancel"));
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Organizer.instance().setCollectionQualityThreshold(collection_name, slider.getValue());
                setVisible(false);
            }
        });

        buttonsPanel.add(save);
        buttonsPanel.add(cancel);

        inside_panel.add(collectionInfo);
        inside_panel.add(threshold);
        inside_panel.add(information);
        out_panel.add(buttonsPanel, BorderLayout.SOUTH);
        out_panel.add(inside_panel, BorderLayout.CENTER);

        add(out_panel);

        pack();
    }
}
