/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.gui.detail.factory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.LinkedList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import org.uclv.darkaiv.gui.util.WrapLayout;
import org.uclv.darkaiv.organizer.Organizer;

/**
 *
 * @author fenriquez
 */
public class CatalogIDsDetail extends Detail {

    private JLabel field;
    private JPanel pane;
    private static DetailFactory df = new DetailFactory();

    public CatalogIDsDetail(String key, String interfaceName, long doc_id, Object value, int row) {
        super(key, doc_id, interfaceName, row);

        field = new JLabel(interfaceName, JLabel.LEFT);
        field.setFont(new java.awt.Font("Tahoma", 1, 13));
        Border border = field.getBorder();
        field.setBorder(new CompoundBorder(border, new EmptyBorder(5, 0, 5, 0)));

        pane = new JPanel(new WrapLayout());
        pane.setBackground(Color.WHITE);

        LinkedList<String> ids = (LinkedList<String>) value;
        for (String id : ids) {
            pane.add(df.getDetail(DetailFactory.ID_DETAIL, id, id + ":", doc_id, Organizer.instance().loadOrCreateidentifier(doc_id, id), row));
        }

        BorderLayout bl = new BorderLayout();
        this.setLayout(bl);
        this.add(field, BorderLayout.NORTH);
        this.add(pane, BorderLayout.CENTER);
    }

    @Override
    public void resize(int new_width) {
        for (Component c : pane.getComponents()) {
            if (c instanceof Detail) {
                ((Detail) c).resize(new_width - 4);
            }
        }
    }
}
