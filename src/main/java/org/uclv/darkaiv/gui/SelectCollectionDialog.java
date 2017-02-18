/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.gui;

import com.sun.jersey.api.client.ClientHandlerException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.uclv.darkaiv.dspace.Collection;
import org.uclv.darkaiv.dspace.Community;
import org.uclv.darkaiv.gui.workers.LoadTopCommunitiesWorker;

/**
 *
 * @author fenriquez
 */
public class SelectCollectionDialog extends JDialog {

    private ResourceBundle resources;
    private DefaultMutableTreeNode root;
    private JTree tree;
    private JLabel progress;
    private ResourceBundle resource;
    private String instance;
    private String server;
    private JPanel treePanel;
    private JButton select;
    private JButton cancel;

    private Collection collectionSelected;

    public SelectCollectionDialog(Frame owner, boolean modal) throws MalformedURLException {
        super(owner, modal);
        resources = ResourceBundle.getBundle("org.uclv.darkaiv.gui.gui");
        setTitle(resources.getString("gui.dialog.select.text.title"));
        setPreferredSize(new Dimension(420, 500));
        setMinimumSize(new Dimension(420, 500));
        setResizable(false);
        setLayout(new BorderLayout());

        select = new JButton(resources.getString("gui.dialog.select.text.select"));
        cancel = new JButton(resources.getString("gui.dialog.select.text.cancel"));
        select.setEnabled(false);
        select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PublicationDialog.setCollection(collectionSelected);
                setVisible(false);
            }
        });
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(select);
        buttonPanel.add(cancel);

        File file = new File("./config/dspace/");
        URL[] urls = {file.toURI().toURL()};
        ClassLoader loader = new URLClassLoader(urls);
        resource = ResourceBundle.getBundle("dspace", Locale.getDefault(), loader);

        instance = resource.getString("dspace.server.instance");
        server = resource.getString("dspace.server.url");

        progress = new JLabel(resources.getString("gui.dialog.select.text.loading"), JLabel.CENTER);
        progress.setFont(new java.awt.Font("Tahoma", Font.PLAIN, 17));
        progress.setForeground(new Color(120, 120, 120));
        progress.setBorder(new CompoundBorder(progress.getBorder(), new EmptyBorder(0, 0, 0, 0)));

        treePanel = new JPanel(new BorderLayout());
        tree = new CollectionsTree();
        tree.setEditable(false);
        tree.setRowHeight(24);
        root = new DefaultMutableTreeNode(new TreeEntry(instance + " [" + server + "]", null, TreeEntry.ROOT, null, -1));
        tree.setModel(new DefaultTreeModel(root));

        LoadTopCommunitiesWorker loadTopCommunitiesWorker = new LoadTopCommunitiesWorker(instance, server, progress);
        loadTopCommunitiesWorker.addPropertyChangeListener(
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent e) {
                        // if the changed property is progress,
                        // update the progress bar
                        if (e.getPropertyName().equals("progress")) {
                            int newValue = (Integer) e.getNewValue();
                            if (newValue == 100) {
                                progress.setVisible(false);
                                tree.setModel(new DefaultTreeModel(LoadTopCommunitiesWorker.root));
                                tree.repaint();
                                JScrollPane scrollPane = new JScrollPane(tree);
                                treePanel.add(scrollPane, BorderLayout.CENTER);
                                add(treePanel, BorderLayout.CENTER);
                            }
                        } // end if
                    } // end method propertyChange
                } // end anonymous inner class
        ); // end call to addPropertyChangeListener
        loadTopCommunitiesWorker.execute(); // execute the PrimeCalculator object

        CollectionsTreeCellRenderer renderer = new CollectionsTreeCellRenderer();

        renderer.setBorderSelectionColor(null); // remove selection border
        renderer.setBackgroundSelectionColor(null); // remove selection background since we paint the selected row ourselves
        tree.setCellRenderer(renderer);

        // send repaint event when node selection changes; otherwise sometimes the border remained visible although the selected node changed 
        TreeSelectionListener treeSelListener = new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent evt) {
                tree.treeDidChange();
            }
        };
        tree.addTreeSelectionListener(treeSelListener);

        // handle mouse clicks outside of the node's label
        // mouse begin ----------------------------------------------------------------
        MouseListener ml = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selRow = tree.getClosestRowForLocation(e.getX(), e.getY());
                if (selRow != -1) {
                    Rectangle bounds = tree.getRowBounds(selRow);
                    boolean outside = e.getX() < bounds.x || e.getX() > bounds.x + bounds.width || e.getY() < bounds.y || e.getY() >= bounds.y + bounds.height;
                    if (outside) {
                        tree.setSelectionRow(selRow);
                        // handle doubleclick
                        if (e.getClickCount() == 2) {
                            if (tree.isCollapsed(selRow)) {
                                tree.expandRow(selRow);
                            } else if (tree.isExpanded(selRow)) {
                                tree.collapseRow(selRow);
                            }
                        }
                    }
                }
            }
        };
        tree.addMouseListener(ml);

        tree.addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                collectionTreeValuedChanged();
            }
        });

        treePanel.add(progress, BorderLayout.CENTER);

//        this.add(topPanel, BorderLayout.NORTH);
        this.add(treePanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void collectionTreeValuedChanged() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        Object value = ((TreeEntry) node.getUserObject()).getRaw();
        if (value instanceof Community) {
            select.setEnabled(false);
            tree.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            if (!((TreeEntry) node.getUserObject()).isVisited()) {
                try {
                    Community[] communitys = ((Community) value).getSubCommunities2();
                    Collection[] collections = ((Community) value).getCollections();

                    for (Community community : communitys) {
                        node.add(new DefaultMutableTreeNode(new TreeEntry(community.getName(), new ImageIcon(getClass().getResource("img/users.png")), TreeEntry.COLLECTION, community, community.getId())));
                    }
                    for (Collection collection : collections) {
                        node.add(new DefaultMutableTreeNode(new TreeEntry(collection.getName(), new ImageIcon(getClass().getResource("img/folder-horizontal.png")), TreeEntry.COLLECTION, collection, collection.getId())));
                    }
                    ((TreeEntry) node.getUserObject()).setVisited(true);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(SelectCollectionDialog.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClientHandlerException ex) {
                    setVisible(false);
                    JOptionPane.showMessageDialog(null, resources.getString("gui.dialog.select.text.close"), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            tree.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        } else {
            if (value instanceof org.uclv.darkaiv.dspace.Collection) {
                select.setEnabled(true);
                this.collectionSelected = (Collection) value;
            }
        }
    }
}
