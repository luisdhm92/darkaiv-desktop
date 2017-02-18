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
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import org.uclv.darkaiv.gui.Main;
import org.uclv.darkaiv.organizer.Organizer;

/**
 *
 * @author fenriquez
 */
public class TypeDetail extends Detail {

    private JLabel field;
    private JComboBox comboBox;
    private static final String[] typeNames = {"Generic", "Journal Article", "Book", "Conference Proceedings"};
    private static final String[] types = {"generic", "journal-article", "book", "proceedings"};
    private static final ArrayList<String> typesArray = new ArrayList(Arrays.asList(types));
    private static Font defaultFont = new Font("Tahoma", Font.PLAIN, 11);
    private static FontMetrics fontMetrics;
    private static int charWidth;

    public TypeDetail(String key, String interfaceName, long doc_id, String value, int row) {
        super(key, doc_id, interfaceName, row);

        field = new JLabel(interfaceName, JLabel.LEFT);
        Border border = field.getBorder();
        field.setBorder(new CompoundBorder(border, new EmptyBorder(3, 5, 3, 5)));
        JPanel box = new JPanel(new BorderLayout());
        box.setPreferredSize(new Dimension(50, box.getPreferredSize().height));
        box.setBackground(Color.WHITE);
        box.add(field, BorderLayout.WEST);

        comboBox = new JComboBox(typeNames);
        comboBox.setFont(defaultFont);
        comboBox.setSelectedIndex(typesArray.indexOf(value));

        comboBox.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Main.instance().update(Organizer.instance().update(getDoc_id(), getKey(), types[comboBox.getSelectedIndex()]), getRow(), true);
            }
        });

        this.setLayout(new BorderLayout());
        this.add(box, BorderLayout.WEST);
        this.add(comboBox, BorderLayout.CENTER);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        fontMetrics = g.getFontMetrics(defaultFont);
        charWidth = fontMetrics.charWidth('#');
    }

    @Override
    public void resize(int new_width) {
        if (charWidth != 0) {
            int charCount = (int) (new_width - 72) / charWidth;
            String tmp = "";
            for (int i = 0; i < charCount; i++) {
                tmp += "#";
            }
            comboBox.setPrototypeDisplayValue(tmp);
        }
    }
}
