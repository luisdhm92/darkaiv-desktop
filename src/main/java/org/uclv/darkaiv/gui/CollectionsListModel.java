/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import org.uclv.darkaiv.model.Collection;

/**
 *
 * @author fenriquez
 */
public class CollectionsListModel extends AbstractListModel {

    private ArrayList<ListEntry> data;
    private ResourceBundle resources;

    public CollectionsListModel() {
        data = new ArrayList<ListEntry>();
        resources = ResourceBundle.getBundle("org.uclv.darkaiv.gui.gui");
    }

    public ArrayList<ListEntry> getData() {
        return data;
    }

    @Override
    public int getSize() {
        return data.size();
    }

    @Override
    public Object getElementAt(int index) {
        return data.get(index);
    }

    public int getIndexOf(String name) {
        int index = -1;
        for (ListEntry entry : data) {
            index++;
            if (entry.getValue().equals(name)) {
                break;
            }
        }
        return index;
    }

    public void add(ListEntry element) {
        if (data.add(element)) {
            fireContentsChanged(this, 0, getSize());
        }
    }

    public void createList(List<Collection> collectionsList) {
        clear();
        String[] collectionsNames = new String[collectionsList.size()];

        int i = 0;
        for (Collection collection : collectionsList) {
            collectionsNames[i] = collection.getString("name");
            i++;
        }
        Arrays.sort(collectionsNames);
        createList(collectionsNames);
        fireContentsChanged(this, 0, getSize());
    }

    private void createList(String[] collectionsNames) {
        add(new ListEntry(resources.getString("gui.default.collection.default"), null));
        add(new ListEntry(resources.getString("gui.default.collection.alldocuments"), new ImageIcon(getClass().getResource(resources.getString("gui.icon.16.alldocuments")))));
        add(new ListEntry(resources.getString("gui.default.collection.needreview"), new ImageIcon(getClass().getResource(resources.getString("gui.icon.16.needreview")))));
        add(new ListEntry(resources.getString("gui.default.collection.recentlyadds"), new ImageIcon(getClass().getResource(resources.getString("gui.icon.16.recentlyadds")))));
        add(new ListEntry(resources.getString("gui.default.collection.unpublished"), new ImageIcon(getClass().getResource(resources.getString("gui.icon.16.unpublished")))));
        add(new ListEntry(resources.getString("gui.default.collection.unsorted"), new ImageIcon(getClass().getResource(resources.getString("gui.icon.16.unsorted")))));
        add(new ListEntry(resources.getString("gui.default.collection.separator"), null));
        add(new ListEntry(resources.getString("gui.default.collection.mine"), null));

        for (String s : collectionsNames) {
            if (!s.equals(resources.getString("gui.default.collection.unsorted")) && !s.equals(resources.getString("gui.default.collection.deleteddocuments"))) {
                add(new ListEntry(s, new ImageIcon(getClass().getResource(resources.getString("gui.icon.16.folder")))));
            }
        }
        add(new ListEntry(resources.getString("gui.default.collection.addnewcollection"), null));

        add(new ListEntry(resources.getString("gui.default.collection.separator"), null));
        add(new ListEntry(resources.getString("gui.default.collection.trash"), null));
        add(new ListEntry(resources.getString("gui.default.collection.deleteddocuments"), new ImageIcon(getClass().getResource(resources.getString("gui.icon.16.deleteddocuments")))));
    }

    public void clear() {
        data.clear();
        fireContentsChanged(this, 0, getSize());
    }
}
