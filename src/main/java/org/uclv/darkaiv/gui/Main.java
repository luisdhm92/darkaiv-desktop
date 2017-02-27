/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.gui;

import org.uclv.darkaiv.gui.workers.AddFilesWorker;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import static javax.swing.JSplitPane.HORIZONTAL_SPLIT;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.uclv.darkaiv.exceptions.CollectionAlreadyExistException;
import org.uclv.darkaiv.gui.detail.factory.Detail;
import org.uclv.darkaiv.gui.detail.factory.DetailFactory;
import org.uclv.darkaiv.gui.util.ListContentMenu;
import org.uclv.darkaiv.gui.util.TableContentMenu;
import org.uclv.darkaiv.gui.util.WrapLayout;
import org.uclv.darkaiv.gui.workers.AddFolderWorker;
import org.uclv.darkaiv.gui.workers.CrossrefReviewWorker;
import org.uclv.darkaiv.gui.workers.GrobidReviewWorker;
import org.uclv.darkaiv.gui.workers.WorldCatReviewWorker;
import org.uclv.darkaiv.model.Document;
import org.uclv.darkaiv.organizer.Organizer;

/**
 *
 * @author fenriquez
 */
public class Main extends JFrame {

    private static Main instance;

    // <editor-fold defaultstate="collapsed" desc="Variables declaration">
    private static ResourceBundle gui_resources;
    private ResourceBundle field_names_resources;
    private ResourceBundle field_type_resources;
    private ResourceBundle field_detail_type_resources;
    private String[] fields;
    private String[] identifiers;
    private static final int PORT = 9999;
    private static ServerSocket socket;

    private BorderLayout bl_jFrame;

    private JMenuBar mb_menu;
    private JMenu m_file;
    private JMenu m_extract;
    private JMenu m_review;
    private JMenu m_publish;
    private JMenu m_tools;
    private JMenu m_help;

//    private JMenuItem mi_newCollection;
    private JMenuItem mi_addFiles;
    private JMenuItem mi_addFolder;
//    private JMenuItem mi_collectionProperties;
//    private JMenuItem mi_qualityMetrics;
    private JMenuItem mi_exit;
    private JMenuItem mi_crossRef;
    private JMenuItem mi_worldCat;
    private JMenuItem mi_grobid;
    private JMenuItem mi_dSpaceCollection;
//    private JMenuItem mi_MarkAllUnpublished;
    private JMenuItem mi_backup;
    private JMenuItem mi_restore;
//    private JMenuItem mi_help;
    private JMenuItem mi_about;

    private List<String> nonPhysicsCollections;
    private CollectionsListModel clm_listcollections;
    public JList jlist_collections;
    public static int mHoveredJListIndex = -1;
    public static int collectionListIndexSelected = 1;
    private JScrollPane jscrollp_collections;

    private JTable jtable_documents;
    public static int mHoveredJTableIndex = -1;
    private JScrollPane jscrollp_table;
    public DocumentsTableModel tm_documents;
    public static int docsTableIndexSelected = -1;
    private JLabel l_noDocs;
    private JTabbedPane jtp_details;
    private JPanel p_details;
    private JSplitPane jsp_doccumentsDetails;
    private JPanel p_documentsDetails;
    private JSplitPane jsp_collectionsDocuments;

    private JLabel l_info;
    private JPanel p_status;
    private JPanel p_top;
    private BorderLayout bl_top;
    private JLabel l_collectionName;
    private JProgressBar jpb_addfiles;

    private AddCollectionDialog jd_addCollection;

    private AddFilesWorker addFilesWorker;
    private AddFolderWorker addFolderWorker;
    private GrobidReviewWorker grobidReviewWorker;
    private WorldCatReviewWorker worldCatReviewWorker;
    private CrossrefReviewWorker crossrefReviewWorker;
    private DetailFactory df = new DetailFactory();
//    private JLabel info;

    private PublicationDialog jd_publication;
    private int detailsWidth = 0;
    private JButton cancel;
    // </editor-fold>

    public Main() {
        super();
        fields = new String[]{"type", "title", "author", "source", "year", "pages", "issue", "volume", "abstract", "key_words", "idiom", "chapter", "publisher", "editors", "edition", "city", "department", "university", "thesis_type", "institution", "creation_date", "updated_date"};
        identifiers = new String[]{"doi", "isbn", "issn"};

        try {
            Organizer.instance().init(null);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.setMinimumSize(new Dimension(640, 480));
        this.setPreferredSize(new Dimension(1000, 600));
        this.setLocation(50, 50);
        this.setResizable(true);
        this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);

        gui_resources = ResourceBundle.getBundle("org.uclv.darkaiv.gui.gui");
        field_names_resources = ResourceBundle.getBundle("org.uclv.darkaiv.gui.field");
        field_detail_type_resources = ResourceBundle.getBundle("org.uclv.darkaiv.gui.field_type");

        this.setTitle(gui_resources.getString("gui.program.name"));

        nonPhysicsCollections = new ArrayList<String>();
        nonPhysicsCollections.add(gui_resources.getString("gui.default.collection.separator"));
        nonPhysicsCollections.add(gui_resources.getString("gui.default.collection.default"));
        nonPhysicsCollections.add(gui_resources.getString("gui.default.collection.mine"));
        nonPhysicsCollections.add(gui_resources.getString("gui.default.collection.trash"));
        nonPhysicsCollections.add(gui_resources.getString("gui.default.collection.alldocuments"));
        nonPhysicsCollections.add(gui_resources.getString("gui.default.collection.recentlyadds"));
        nonPhysicsCollections.add(gui_resources.getString("gui.default.collection.unpublished"));
        nonPhysicsCollections.add(gui_resources.getString("gui.default.collection.needreview"));
        nonPhysicsCollections.add(gui_resources.getString("gui.default.collection.addnewcollection"));
        nonPhysicsCollections.add(gui_resources.getString("gui.default.collection.deleteddocuments"));

        // <editor-fold defaultstate="collapsed" desc="Frame Resizing">
        this.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                jsp_doccumentsDetails.setDividerLocation(getSize().width / 2);
                jsp_collectionsDocuments.setDividerLocation(getSize().width / 5);
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void componentShown(ComponentEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        // </editor-fold>

        bl_jFrame = new BorderLayout(0, 1);
        setLayout(bl_jFrame);

        cancel = new JButton(new ImageIcon(getClass().getResource(gui_resources.getString("gui.icon.16.crosscircle-frame"))));
        cancel.setVisible(false);
        cancel.setPreferredSize(new Dimension(20, 20));

        // <editor-fold defaultstate="collapsed" desc="Menu Bar Definition">
        mb_menu = new JMenuBar();

        m_file = new JMenu(gui_resources.getString("gui.menubar.menu.file"));
        m_extract = new JMenu(gui_resources.getString("gui.menubar.menu.extract"));
        m_review = new JMenu(gui_resources.getString("gui.menubar.menu.review"));
        m_publish = new JMenu(gui_resources.getString("gui.menubar.menu.publish"));
        m_tools = new JMenu(gui_resources.getString("gui.menubar.menu.tools"));
        m_help = new JMenu(gui_resources.getString("gui.menubar.menu.help"));

//        mi_newCollection = new JMenuItem(gui_resources.getString("gui.action.name.newcollection"));
//        mi_newCollection.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
//        mi_newCollection.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                addCollectionctionPerformed();
//            }
//        });
        mi_addFiles = new JMenuItem(gui_resources.getString("gui.action.name.addfile"), new ImageIcon(getClass().getResource(gui_resources.getString("gui.icon.16.addfiles"))));
        mi_addFiles.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        mi_addFiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFilesActionPerformed();
            }
        });
        mi_addFolder = new JMenuItem(gui_resources.getString("gui.action.name.addfolder"), new ImageIcon(getClass().getResource(gui_resources.getString("gui.icon.16.addfilesfromfolder"))));
        mi_addFolder.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        mi_addFolder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFolderActionPerformed();
            }
        });
//        mi_collectionProperties = new JMenuItem(gui_resources.getString("gui.action.name.collectionproperties"));
//        mi_collectionProperties.setEnabled(false);
//        mi_qualityMetrics = new JMenuItem(gui_resources.getString("gui.action.name.qualitymetrics"));
//        mi_qualityMetrics.setEnabled(false);
//        mi_qualityMetrics.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                openQualityMetrics();
//            }
//        });

        mi_exit = new JMenuItem(gui_resources.getString("gui.action.name.exit"));
        mi_exit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        mi_exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

//        m_file.add(mi_newCollection);
//        m_file.addSeparator();
        m_file.add(mi_addFiles);
        m_file.add(mi_addFolder);
        m_file.addSeparator();
//        m_file.add(mi_collectionProperties);
//        m_file.add(mi_qualityMetrics);
//        m_file.addSeparator();
        m_file.add(mi_exit);

        mi_grobid = new JMenuItem(gui_resources.getString("gui.action.name.grobid"));
        mi_grobid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grobidReview();
            }
        });
        m_extract.add(mi_grobid);

        mi_crossRef = new JMenuItem(gui_resources.getString("gui.action.name.crosref"));
        mi_crossRef.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crossrefReview();
            }
        });

        mi_worldCat = new JMenuItem(gui_resources.getString("gui.action.name.worldcat"));
        mi_worldCat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                worldCatReview();
            }
        });

        m_review.add(mi_crossRef);
        m_review.add(mi_worldCat);

        mi_dSpaceCollection = new JMenuItem(gui_resources.getString("gui.action.name.dspace"));
        mi_dSpaceCollection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPublishDialog();
            }
        });
//        mi_MarkAllUnpublished = new JMenuItem(gui_resources.getString("gui.action.name.unpublished"), new ImageIcon(getClass().getResource(gui_resources.getString("gui.icon.16.unpublished"))));
//        mi_MarkAllUnpublished.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_MASK));

        m_publish.add(mi_dSpaceCollection);
//        m_publish.addSeparator();
//        m_publish.add(mi_MarkAllUnpublished);

        mi_backup = new JMenuItem(gui_resources.getString("gui.action.name.backup"), new ImageIcon(getClass().getResource(gui_resources.getString("gui.icon.16.backup"))));
        mi_backup.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
        mi_backup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backup();
            }
        });
        mi_restore = new JMenuItem(gui_resources.getString("gui.action.name.restore"), new ImageIcon(getClass().getResource(gui_resources.getString("gui.icon.16.restore"))));
        mi_restore.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        mi_restore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restore();
            }
        });

        m_tools.add(mi_backup);
        m_tools.add(mi_restore);

//        mi_help = new JMenuItem(gui_resources.getString("gui.action.name.help"), new ImageIcon(getClass().getResource(gui_resources.getString("gui.icon.16.help"))));
//        mi_help.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        mi_about = new JMenuItem(gui_resources.getString("gui.action.name.about"), new ImageIcon(getClass().getResource(gui_resources.getString("gui.icon.16.information-frame"))));
//        mi_about = new JMenuItem(gui_resources.getString("gui.action.name.about"));
        mi_about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutDialog aboutDialog = new AboutDialog(null, true);
                aboutDialog.setLocation((getLocation().x + (getWidth() / 2)) - (aboutDialog.getWidth() / 2), (getLocation().y + getHeight() / 2) - (aboutDialog.getHeight() / 2));
                aboutDialog.pack();
                aboutDialog.setVisible(true);
            }
        });

//        m_help.add(mi_help);
        m_help.add(mi_about);

        mb_menu.add(m_file);
        mb_menu.add(m_extract);
        mb_menu.add(m_review);
        mb_menu.add(m_publish);
        mb_menu.add(m_tools);
        mb_menu.add(m_help);

        setJMenuBar(mb_menu);
        // </editor-fold>

        // Left part content (CollectionList)
        clm_listcollections = new CollectionsListModel();
        clm_listcollections.createList(Organizer.instance().getCollections());
        jlist_collections = new JList(clm_listcollections);
        jlist_collections.setCellRenderer(new ListEntryCellRenderer());
        jlist_collections.setSelectedIndex(1);
        jlist_collections.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jlist_collections.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent me) {
                Point p = new Point(me.getX(), me.getY());
                int index = jlist_collections.locationToIndex(p);
                if (index != mHoveredJListIndex) {
                    mHoveredJListIndex = index;
                    jlist_collections.repaint();
                }
            }
        });
        jlist_collections.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent me) {
                mHoveredJListIndex = -1;
                jlist_collections.repaint();
            }
        });
        jlist_collections.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                collectionsListValueChanged();
            }
        });

        ListContentMenu listContentMenu = new ListContentMenu(this);
        listContentMenu.add(jlist_collections);

        jscrollp_collections = new JScrollPane(jlist_collections);

        // Top part content
        l_collectionName = new JLabel(gui_resources.getString("gui.default.collection.alldocuments"), new ImageIcon(getClass().getResource(gui_resources.getString("gui.icon.16.alldocuments"))), JLabel.LEFT);
        l_collectionName.setFont(new java.awt.Font("Tahoma", 1, 13));
        l_collectionName.setForeground(new Color(60, 60, 60));
        l_collectionName.setBorder(new CompoundBorder(l_collectionName.getBorder(), new EmptyBorder(10, 10, 10, 10)));

        p_top = new JPanel();
        bl_top = new BorderLayout(10, 0);
        p_top.setLayout(bl_top);
        p_top.add(l_collectionName, BorderLayout.CENTER);
        p_top.setPreferredSize(new Dimension(this.getWidth(), 40));

        // Center part content (DocumentsTable)
        tm_documents = new DocumentsTableModel();
        jtable_documents = new JTable(tm_documents);
        jtable_documents.setFocusable(true);
        jtable_documents.setFillsViewportHeight(true);
        jtable_documents.setRowHeight(30);
        jtable_documents.setShowGrid(false);
        jtable_documents.setIntercellSpacing(new Dimension(0, 0));
        jtable_documents.getTableHeader().setReorderingAllowed(false);
        jtable_documents.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent me) {
                Point p = new Point(me.getX(), me.getY());
                int index = jtable_documents.rowAtPoint(p);
                if (index != mHoveredJTableIndex) {
                    mHoveredJTableIndex = index;
                    jtable_documents.repaint();
                }
            }
        });
        jtable_documents.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent me) {
                mHoveredJTableIndex = -1;
                jtable_documents.repaint();
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getButton() == MouseEvent.BUTTON1) {
                    tableDocumentsMouseClicked();
                }
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                if (evt.getButton() == MouseEvent.BUTTON1) {
                    tableDocumentsMouseReleased();
                }
            }
        });

        InputMap inputMap = jtable_documents.getInputMap(JTable.WHEN_FOCUSED);
        ActionMap actionMap = jtable_documents.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "delete");
        actionMap.put("delete", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                ListEntry entry = Main.instance().getListEntryAt(Main.collectionListIndexSelected);
                if (entry.getValue().equals("Deleted Documents")) {
                    if (JOptionPane.showConfirmDialog(jtable_documents, "Delete " + jtable_documents.getSelectedRows().length + " documents from your library?", "Warning", JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
                        for (int index : jtable_documents.getSelectedRows()) {
                            Organizer.instance().deletePermanently(((DocumentsTableModel) jtable_documents.getModel()).getDocs().get(index).getLongId());
                        }
                    }
                    Main.instance().collectionsListValueChanged();
                } else {
                    for (int index : jtable_documents.getSelectedRows()) {
                        Organizer.instance().delete(((DocumentsTableModel) jtable_documents.getModel()).getDocs().get(index).getLongId());
                    }
                    Main.instance().collectionsListValueChanged();
                }
            }
        });

        TableContentMenu tableContentMenu = new TableContentMenu(this);
        tableContentMenu.add(jtable_documents);

        for (int i = 0; i < tm_documents.getColumnCount(); i++) {
            jtable_documents.getTableHeader().getColumnModel().getColumn(i).setHeaderRenderer(new DocumentsTableHeaderRenderer());
        }
        jtable_documents.setDefaultRenderer(Object.class, new DocumentsTableCellRenderer());

        initColumnSizes(jtable_documents);
        jscrollp_table = new JScrollPane(jtable_documents);

        List<Document> list = Organizer.instance().getDocuments();
        if (!list.isEmpty()) {
            tm_documents.insertData(list);
        } else {
            tm_documents.insertData(null);
        }

        // Right part content (Details)
        jtp_details = new javax.swing.JTabbedPane();
        p_details = new JPanel(new BorderLayout());
        p_details.setBackground(new java.awt.Color(255, 255, 255));

        l_noDocs = new JLabel(gui_resources.getString("gui.details.text.nodocuments"), JLabel.CENTER);
        l_noDocs.setFont(new java.awt.Font("Tahoma", Font.PLAIN, 17));
        l_noDocs.setForeground(new Color(120, 120, 120));
        l_noDocs.setBorder(new CompoundBorder(l_noDocs.getBorder(), new EmptyBorder(0, 0, 0, 0)));
//        info = new JLabel("Select only one document to view details", JLabel.CENTER);
//        info.setForeground(new Color(120, 120, 120));
//        info.setForeground(new Color(0, 63, 115));
//        info.setBorder(new CompoundBorder(info.getBorder(), new EmptyBorder(35, 0, 0, 0)));
        p_details.setBackground(new java.awt.Color(255, 255, 255));
        p_details.add(l_noDocs, BorderLayout.CENTER);

        p_details.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                for (Component c : p_details.getComponents()) {
                    if (c instanceof Detail) {
                        ((Detail) c).resize(e.getComponent().getWidth() - 15);
                        detailsWidth = e.getComponent().getWidth();
                    }
                }
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });

        jtp_details.add(gui_resources.getString("gui.details.text.title"), p_details);

        // Split
        jsp_doccumentsDetails = new JSplitPane(HORIZONTAL_SPLIT, jscrollp_table, jtp_details);
        jsp_doccumentsDetails.setBorder(null);
        jsp_doccumentsDetails.setOneTouchExpandable(false);
        jsp_doccumentsDetails.setContinuousLayout(true);
        SplitPaneUI ui = jsp_doccumentsDetails.getUI();
        if (ui instanceof BasicSplitPaneUI) {
            ((BasicSplitPaneUI) ui).getDivider().setBorder(null);
        }

        p_documentsDetails = new JPanel();
        BorderLayout docsDetailsPanelLayout = new BorderLayout();
        p_documentsDetails.setLayout(docsDetailsPanelLayout);

        p_documentsDetails.add(p_top, BorderLayout.NORTH);
        p_documentsDetails.add(jsp_doccumentsDetails, BorderLayout.CENTER);

        // Split
        jsp_collectionsDocuments = new JSplitPane(HORIZONTAL_SPLIT, true, jscrollp_collections, p_documentsDetails);
        //jsp_collectionsDocuments.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jsp_collectionsDocuments.setOneTouchExpandable(false);
        jsp_collectionsDocuments.setBorder(null);
        jsp_collectionsDocuments.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
        jsp_collectionsDocuments.setContinuousLayout(true);
        ui = jsp_collectionsDocuments.getUI();
        if (ui instanceof BasicSplitPaneUI) {
            ((BasicSplitPaneUI) ui).getDivider().setBorder(null);
        }

        add(jsp_collectionsDocuments, BorderLayout.CENTER);

        // Status Panel
        p_status = new JPanel(true);
        FlowLayout statussPanelLayout = new FlowLayout(FlowLayout.LEFT);
        statussPanelLayout.setHgap(0);
        p_status.setLayout(statussPanelLayout);
        p_status.setPreferredSize(new Dimension(this.getWidth(), 28));
        //p_status.setBorder(new MatteBorder(1, 1, 1, 1, new Color(127, 140, 149)));
        p_status.setBorder(null);

        l_info = new JLabel();
        l_info.setVisible(false);
        l_info.setBorder(new CompoundBorder(l_info.getBorder(), new EmptyBorder(0, 10, 0, 0)));

        jpb_addfiles = new JProgressBar();
        jpb_addfiles.setBorder(new CompoundBorder(jpb_addfiles.getBorder(), new EmptyBorder(0, 10, 0, 0)));
        jpb_addfiles.setStringPainted(true);
        jpb_addfiles.setPreferredSize(new Dimension(350, 16));
        jpb_addfiles.setVisible(false);

        p_status.add(jpb_addfiles);
        p_status.add(l_info);
        p_status.add(cancel);

        add(p_status, BorderLayout.SOUTH);

        jtable_documents.requestFocus();

        pack();
    }

    public ListEntry getListEntryAt(int index) {
        return (ListEntry) clm_listcollections.getElementAt(index);
    }

    public static Main instance() {
        if (instance == null) {
            instance = new Main();
        }
        return instance;
    }

    public static void main(String[] args) {

        /* Set the System look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        // Custom fonts:
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException exception) {
        } catch (InstantiationException exception) {
        } catch (IllegalAccessException exception) {
        } catch (UnsupportedLookAndFeelException exception) {
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    //Bind to localhost adapter with a zero connection queue 
                    socket = new ServerSocket(PORT, 0, InetAddress.getByAddress(new byte[]{127, 0, 0, 1}));
                    Main.instance().setVisible(true);
                } catch (BindException e) {
                    JFrame window = new JFrame();
                    JOptionPane.showMessageDialog(window, "Darkaiv already running");
//                    System.err.println("Already running.");
                    System.exit(0);
                } catch (IOException e) {
                    JFrame window = new JFrame();
                    JOptionPane.showMessageDialog(window, "Unexpected error");
//                    System.err.println("Unexpected error.");
                    System.exit(0);
                }
            }
        });
    }

    private void initColumnSizes(JTable table) {
        DocumentsTableModel model = (DocumentsTableModel) table.getModel();
        TableColumn column;
        Component comp;
        int headerWidth;
        int cellWidth;
        Object[] longValues = model.longValues;
        TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();

        table.getColumnModel().getColumn(0).setMaxWidth(36);
        table.getColumnModel().getColumn(1).setMaxWidth(24);
        table.getColumnModel().getColumn(4).setMaxWidth(32);
        table.getColumnModel().getColumn(5).setMaxWidth(70);

        for (int i = 2; i < model.getColumnCount(); i++) {
            column = table.getColumnModel().getColumn(i);

            comp = headerRenderer.getTableCellRendererComponent(null, column.getHeaderValue(), false, false, 0, 0);
            headerWidth = comp.getPreferredSize().width;
            comp = table.getDefaultRenderer(model.getColumnClass(i)).
                    getTableCellRendererComponent(table, longValues[i], false, false, 0, i);
            cellWidth = comp.getPreferredSize().width;

            column.setPreferredWidth(Math.max(headerWidth, cellWidth));
        }
    }

    public void collectionsListValueChanged() {
//        if ((jlist_collections.getSelectedIndex() > 7) && (jlist_collections.getSelectedIndex() < jlist_collections.getModel().getSize() - 4) && jlist_collections.getModel().getSize() > 11) {
//            mi_qualityMetrics.setEnabled(true);
//        } else {
//            mi_qualityMetrics.setEnabled(false);
//        }
        String selectedValue = ((ListEntry) jlist_collections.getSelectedValue()).getValue();

        if (((ListEntry) jlist_collections.getSelectedValue()).getValue().equals(gui_resources.getString("gui.default.collection.addnewcollection"))) {
            jlist_collections.repaint();
            addCollectionctionPerformed();
        } else {
            if (!selectedValue.equals(gui_resources.getString("gui.default.collection.separator"))) {
                if (selectedValue.equals(gui_resources.getString("gui.default.collection.default")) || selectedValue.equals(gui_resources.getString("gui.default.collection.mine")) || selectedValue.equals(gui_resources.getString("gui.default.collection.trash"))) {
                    jlist_collections.setSelectedIndex(jlist_collections.getSelectedIndex() + 1);
                } else {
                    collectionListIndexSelected = jlist_collections.getSelectedIndex();
                    ListEntry tmp = (ListEntry) clm_listcollections.getElementAt(collectionListIndexSelected);
                    l_collectionName.setText(tmp.getValue());
                    l_collectionName.setIcon(tmp.getIcon());

                    List<Document> list = Organizer.instance().getDocumentsByCollection(tmp.getValue());
                    if (!list.isEmpty()) {
                        tm_documents.insertData(list);
                        l_info.setText(jtable_documents.getRowCount() + " " + gui_resources.getString("gui.text.documentsincollection"));
                        l_info.setVisible(true);
                    } else {
                        l_info.setVisible(false);
                        tm_documents.insertData(null);
                    }
                }
                docsTableIndexSelected = -1;
                jtable_documents.clearSelection();
                jtable_documents.repaint();
                jtable_documents.invalidate();
                jscrollp_table.repaint();

                jtp_details.removeAll();
                p_details.removeAll();
                p_details.setLayout(new BorderLayout());
                l_noDocs.setText(gui_resources.getString("gui.details.text.nodocuments"));
                p_details.add(l_noDocs, BorderLayout.CENTER);
                jtp_details.add(gui_resources.getString("gui.details.text.title"), p_details);
            }
        }
    }

    private void addCollectionctionPerformed() {
        jd_addCollection = new AddCollectionDialog(this, true);
        jd_addCollection.setLocation((this.getLocation().x + (this.getWidth() / 2)) - (jd_addCollection.getWidth() / 2), (this.getLocation().y + (this.getHeight() / 2)) - (jd_addCollection.getHeight() / 2));
        jd_addCollection.setVisible(true);
    }

    public void createCollectionActionPerformed() {
        if (jd_addCollection.getNameField().getText() == null || jd_addCollection.getNameField().getText().equals("")) {
            JOptionPane.showMessageDialog(this, gui_resources.getString("gui.panes.text.empty"), "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                if (Organizer.instance().newCollection(jd_addCollection.getNameField().getText(), jd_addCollection.getNameField().getText())) {
                    clm_listcollections.createList(Organizer.instance().getCollections());
                    collectionListIndexSelected = clm_listcollections.getIndexOf(jd_addCollection.getNameField().getText());
                    jlist_collections.setSelectedIndex(clm_listcollections.getIndexOf(jd_addCollection.getNameField().getText()));
                    collectionsListValueChanged();
                    jd_addCollection.setVisible(false);
                }
            } catch (CollectionAlreadyExistException exception) {
                JOptionPane.showMessageDialog(this, gui_resources.getString("gui.panes.text.unique"), "Error", JOptionPane.ERROR_MESSAGE);
                jd_addCollection.getNameField().requestFocus();
            }
        }
    }

    public void setSelectedCollection(String name) {
        int i = 0;
        for (ListEntry entry : ((CollectionsListModel) jlist_collections.getModel()).getData()) {
            if (entry.getValue().equals(name)) {
                jlist_collections.setSelectedIndex(i);
                break;
            }
            i++;
        }
    }

    public void reloadCollections() {
        clm_listcollections.createList(Organizer.instance().getCollections());
    }

    private void addFilesActionPerformed() {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        JFrame window = new JFrame();
        window.setIconImage(new ImageIcon(getClass().getResource(gui_resources.getString("gui.icon.16.addfiles"))).getImage());

        if (chooser.showOpenDialog(window) == JFileChooser.APPROVE_OPTION) {
            File[] files = chooser.getSelectedFiles();

            String[] paths = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                paths[i] = files[i].toString();
            }
            try {
                String collection = ((ListEntry) clm_listcollections.getElementAt(collectionListIndexSelected)).getValue();

                if (nonPhysicsCollections.contains(collection)) {
                    addFilesWorker = new AddFilesWorker(paths, gui_resources.getString("gui.default.collection.unsorted"), l_info, jpb_addfiles, cancel);
                } else {
                    addFilesWorker = new AddFilesWorker(paths, collection, l_info, jpb_addfiles, cancel);
                }
                addFilesWorker.addPropertyChangeListener(
                        new PropertyChangeListener() {
                            @Override
                            public void propertyChange(PropertyChangeEvent e) {
                                // if the changed property is progress,
                                // update the progress bar
                                if (e.getPropertyName().equals("progress")) {
                                    int newValue = (Integer) e.getNewValue();
                                    jpb_addfiles.setValue(newValue);
                                    if (newValue == 100) {
                                        collectionsListValueChanged();
                                    }
                                } // end if
                            } // end method propertyChange
                        } // end anonymous inner class
                ); // end call to addPropertyChangeListener

                addFilesWorker.execute(); // execute the PrimeCalculator object
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void addFolderActionPerformed() {

        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        JFrame window = new JFrame();
        window.setIconImage(new ImageIcon(getClass().getResource(gui_resources.getString("gui.icon.16.addfilesfromfolder"))).getImage());

        if (chooser.showOpenDialog(window) == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            String collection = ((ListEntry) clm_listcollections.getElementAt(collectionListIndexSelected)).getValue();

            if (nonPhysicsCollections.contains(collection)) {
                addFolderWorker = new AddFolderWorker(path, gui_resources.getString("gui.default.collection.unsorted"), l_info, cancel);
            } else {
                addFolderWorker = new AddFolderWorker(path, collection, l_info, cancel);
            }

            addFolderWorker.execute(); // execute the PrimeCalculator object
        }
    }

    public void tableDocumentsMouseReleased() {
        if (jtable_documents.getSelectedRowCount() > 0) {
            l_info.setText(jtable_documents.getSelectedRowCount() + " " + gui_resources.getString("gui.text.of") + " " + jtable_documents.getRowCount() + " " + gui_resources.getString("gui.text.documentsselected"));
            l_info.setVisible(true);
            if (jtable_documents.getSelectedRowCount() == 1) {
                if ((docsTableIndexSelected != jtable_documents.getSelectedRow())) {
                    docsTableIndexSelected = jtable_documents.getSelectedRow();
                    showDetailsPane();
                }
            } else {
                docsTableIndexSelected = -1;
                jtp_details.removeAll();
                p_details.removeAll();
                p_details.setLayout(new BorderLayout());
                l_noDocs.setText(jtable_documents.getSelectedRowCount() + " " + gui_resources.getString("gui.text.of") + " " + jtable_documents.getRowCount() + " " + gui_resources.getString("gui.text.documentsselected"));
                p_details.add(l_noDocs, BorderLayout.CENTER);
//                p_details.add(info, BorderLayout.CENTER);
                jtp_details.add(gui_resources.getString("gui.details.text.title"), p_details);
            }
        } else {
            docsTableIndexSelected = -1;
            jtp_details.removeAll();
            p_details.removeAll();
            p_details.setLayout(new BorderLayout());
            l_noDocs.setText(gui_resources.getString("gui.details.text.nodocuments"));
            p_details.add(l_noDocs, BorderLayout.CENTER);
            jtp_details.add(gui_resources.getString("gui.details.text.title"), p_details);
        }
        for (Component c : p_details.getComponents()) {
            if (c instanceof Detail) {
                if (detailsWidth != 0) {
                    ((Detail) c).resize(detailsWidth - 15);
                }
            }
        }
    }

    private void tableDocumentsMouseClicked() {
        tableDocumentsMouseReleased();
    }

    private void showDetailsPane() {
        jtp_details.removeAll();
        p_details.removeAll();
        p_details.setLayout(new WrapLayout());

        field_type_resources = ResourceBundle.getBundle("org.uclv.darkaiv.gui." + tm_documents.getDocs().get(jtable_documents.getSelectedRow()).getString("type"));
        Set<String> type_keys = field_type_resources.keySet();

        for (String key : fields) {
            if (type_keys.contains(key)) {
                if (key.equals("author") || tm_documents.getDocs().get(jtable_documents.getSelectedRow()).getString(key) == null) {
                    p_details.add(df.getDetail(Integer.parseInt(field_detail_type_resources.getString(key)), key, field_names_resources.getString(key), tm_documents.getDocs().get(jtable_documents.getSelectedRow()).getLong("id"), "", jtable_documents.getSelectedRow()));
                } else {
                    p_details.add(df.getDetail(Integer.parseInt(field_detail_type_resources.getString(key)), key, field_names_resources.getString(key), tm_documents.getDocs().get(jtable_documents.getSelectedRow()).getLong("id"), tm_documents.getDocs().get(jtable_documents.getSelectedRow()).getString(key), jtable_documents.getSelectedRow()));
                }
            }
        }

        LinkedList<String> list = new LinkedList();
        for (String id : identifiers) {
            if (type_keys.contains(id)) {
                list.add(id);
            }
        }

        if (list.size() > 0) {
            p_details.add(df.getDetail(DetailFactory.CATALOG_IDS_DETAIL, "ids", field_names_resources.getString("ids"), tm_documents.getDocs().get(jtable_documents.getSelectedRow()).getLong("id"), list, jtable_documents.getSelectedRow()));
        }

        JScrollPane jScrollPane = new JScrollPane(p_details);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setBorder(null);
        jScrollPane.getVerticalScrollBar().setUnitIncrement(25);
        jtp_details.add("Details", jScrollPane);
    }

    private void backup() {
        JFileChooser chooser = new JFileChooser();
        JFrame window = new JFrame();
        window.setIconImage(new ImageIcon(getClass().getResource(gui_resources.getString("gui.icon.16.backup"))).getImage());

        if (chooser.showSaveDialog(window) == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getPath();
            String filename = chooser.getSelectedFile().getName();
            try {
                Organizer.instance().backupDB(path, filename);
                l_info.setText(gui_resources.getString("gui.panes.text.backup"));
                l_info.setVisible(true);
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void restore() {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
        JFrame window = new JFrame();
        window.setIconImage(new ImageIcon(getClass().getResource(gui_resources.getString("gui.icon.16.restore"))).getImage());

        if (chooser.showOpenDialog(window) == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getPath();
            try {
                Organizer.instance().restoreDB(path);
                clm_listcollections.createList(Organizer.instance().getCollections());
                l_info.setText(gui_resources.getString("gui.panes.text.restore"));
                l_info.setVisible(true);
                jlist_collections.setSelectedIndex(1);
                collectionsListValueChanged();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void update(Document d, int rowIndex, boolean refresh) {
        tm_documents.insertDocument(d, rowIndex);
        if (refresh) {
            jtp_details.removeAll();
            p_details.removeAll();
            p_details.setLayout(new WrapLayout());

            field_type_resources = ResourceBundle.getBundle("org.uclv.darkaiv.gui." + tm_documents.getDocs().get(jtable_documents.getSelectedRow()).getString("type"));
            Set<String> type_keys = field_type_resources.keySet();

            for (String key : fields) {
                if (type_keys.contains(key)) {
                    if (key.equals("author") || tm_documents.getDocs().get(jtable_documents.getSelectedRow()).getString(key) == null) {
                        p_details.add(df.getDetail(Integer.parseInt(field_detail_type_resources.getString(key)), key, field_names_resources.getString(key), tm_documents.getDocs().get(jtable_documents.getSelectedRow()).getLong("id"), "", jtable_documents.getSelectedRow()));
                    } else {
                        p_details.add(df.getDetail(Integer.parseInt(field_detail_type_resources.getString(key)), key, field_names_resources.getString(key), tm_documents.getDocs().get(jtable_documents.getSelectedRow()).getLong("id"), tm_documents.getDocs().get(jtable_documents.getSelectedRow()).getString(key), jtable_documents.getSelectedRow()));
                    }
                }
            }

            LinkedList<String> list = new LinkedList();
            for (String id : identifiers) {
                if (type_keys.contains(id)) {
                    list.add(id);
                }
            }

            if (list.size() > 0) {
                p_details.add(df.getDetail(DetailFactory.CATALOG_IDS_DETAIL, "ids", field_names_resources.getString("ids"), tm_documents.getDocs().get(jtable_documents.getSelectedRow()).getLong("id"), list, jtable_documents.getSelectedRow()));
            }

            JScrollPane jScrollPane = new JScrollPane(p_details);
            jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            jScrollPane.setBorder(null);
            jScrollPane.getVerticalScrollBar().setUnitIncrement(25);
            jtp_details.add(gui_resources.getString("gui.details.text.title"), jScrollPane);
        }
    }

    private void crossrefReview() {
        if (jtable_documents.getSelectedRowCount() > 0) {
            if (JOptionPane.showConfirmDialog(this, gui_resources.getString("gui.panes.text.crossref"), "Warning", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
                int[] selectedDocumentsIndex = jtable_documents.getSelectedRows();

                crossrefReviewWorker = new CrossrefReviewWorker(selectedDocumentsIndex, tm_documents.getDocs(), l_info, jpb_addfiles, cancel);

                crossrefReviewWorker.addPropertyChangeListener(
                        new PropertyChangeListener() {
                            @Override
                            public void propertyChange(PropertyChangeEvent e) {
                                // if the changed property is progress,
                                // update the progress bar
                                if (e.getPropertyName().equals("progress")) {
                                    int newValue = (Integer) e.getNewValue();
                                    jpb_addfiles.setValue(newValue);
                                } // end if
                            } // end method propertyChange
                        } // end anonymous inner class
                ); // end call to addPropertyChangeListener

                crossrefReviewWorker.execute(); // execute the PrimeCalculator object
            }
        } else {
            JOptionPane.showMessageDialog(this, gui_resources.getString("gui.panes.text.select"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void grobidReview() {
        if (jtable_documents.getSelectedRowCount() > 0) {
            if (JOptionPane.showConfirmDialog(this, gui_resources.getString("gui.panes.text.grobid"), "Warning", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
                int[] selectedDocumentsIndex = jtable_documents.getSelectedRows();

                grobidReviewWorker = new GrobidReviewWorker(selectedDocumentsIndex, tm_documents.getDocs(), l_info, jpb_addfiles, cancel);

                grobidReviewWorker.addPropertyChangeListener(
                        new PropertyChangeListener() {
                            @Override
                            public void propertyChange(PropertyChangeEvent e) {
                                // if the changed property is progress,
                                // update the progress bar
                                if (e.getPropertyName().equals("progress")) {
                                    int newValue = (Integer) e.getNewValue();
                                    jpb_addfiles.setValue(newValue);
                                } // end if
                            } // end method propertyChange
                        } // end anonymous inner class
                ); // end call to addPropertyChangeListener

                grobidReviewWorker.execute(); // execute the PrimeCalculator object
            }
        } else {
            JOptionPane.showMessageDialog(this, gui_resources.getString("gui.panes.text.select"), "Error", JOptionPane.ERROR_MESSAGE);

        }
    }

    private void showPublishDialog() {
        try {
            if (jtable_documents.getSelectedRowCount() > 0) {
                jd_publication = new PublicationDialog(this, true, "Publishing " + jtable_documents.getSelectedRowCount() + " of " + jtable_documents.getRowCount() + " documents in collection " + ((ListEntry) jlist_collections.getSelectedValue()).toString(), this.getLocation().x, this.getWidth(), this.getLocation().y, this.getHeight(), tm_documents.getDocs(), jtable_documents.getSelectedRows());
                jd_publication.setLocation((this.getLocation().x + (this.getWidth() / 2)) - (jd_publication.getWidth() / 2), (this.getLocation().y + (this.getHeight() / 2)) - (jd_publication.getHeight() / 2));
                jd_publication.pack();
                jd_publication.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, gui_resources.getString("gui.panes.text.select"), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (MalformedURLException ex) {
//            JOptionPane.showMessageDialog(this, "Can't found DSpace configurations, please check tour configurations in config folder", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void worldCatReview() {
        if (jtable_documents.getSelectedRowCount() > 0) {
            if (JOptionPane.showConfirmDialog(this, gui_resources.getString("gui.panes.text.worldcat"), "Warning", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
                int[] selectedDocumentsIndex = jtable_documents.getSelectedRows();

                worldCatReviewWorker = new WorldCatReviewWorker(selectedDocumentsIndex, tm_documents.getDocs(), l_info, jpb_addfiles, cancel);

                worldCatReviewWorker.addPropertyChangeListener(
                        new PropertyChangeListener() {
                            @Override
                            public void propertyChange(PropertyChangeEvent e) {
                                // if the changed property is progress,
                                // update the progress bar
                                if (e.getPropertyName().equals("progress")) {
                                    int newValue = (Integer) e.getNewValue();
                                    jpb_addfiles.setValue(newValue);
                                } // end if
                            } // end method propertyChange
                        } // end anonymous inner class
                ); // end call to addPropertyChangeListener

                worldCatReviewWorker.execute(); // execute the PrimeCalculator object
            }
        } else {
            JOptionPane.showMessageDialog(this, gui_resources.getString("gui.panes.text.select"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void publish() {
    }
}
