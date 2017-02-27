/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.gui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;
import org.uclv.darkaiv.model.Document;
import org.uclv.darkaiv.organizer.Organizer;

/**
 *
 * @author fenriquez
 */
public class DocumentsTableModel extends AbstractTableModel {

    private Object[] columnNames;
    private Object[][] data;
    private ArrayList<Document> docs;
    private Class<?>[] columnClasses = {String.class, String.class, String.class, String.class, String.class, String.class};
    public final Object[] longValues = {"#####", "#####", "############", "############################", "###", "########"};

    private ResourceBundle resources;

    private String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dic"};

    public DocumentsTableModel() {
        data = new Object[0][0];
        resources = ResourceBundle.getBundle("org.uclv.darkaiv.gui.gui");
        columnNames = new Object[6];

        columnNames[0] = resources.getString("gui.table.columName.published");
        columnNames[1] = resources.getString("gui.table.columName.quality");
        columnNames[2] = resources.getString("gui.table.columName.authors");
        columnNames[3] = resources.getString("gui.table.columName.title");
        columnNames[4] = resources.getString("gui.table.columName.year");
        columnNames[5] = resources.getString("gui.table.columName.added");
    }

    public List<Document> getDocs() {
        return docs;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex].toString();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnClasses[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        data[rowIndex][columnIndex] = aValue;
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void insertDocument(Document document, int rowIndex) {
        docs.remove(rowIndex);
        docs.add(rowIndex, document);
        DecimalFormat df = new DecimalFormat("###");
        setValueAt(document.retrieveMetadata().get("uploaded"), rowIndex, 0);
        setValueAt(df.format(Organizer.instance().getQuality(document) * 100), rowIndex, 1);
        if (document.retrieveMetadata().containsKey("author")) {
            setValueAt(document.retrieveMetadata().get("author"), rowIndex, 2);
        } else {
            setValueAt(null, rowIndex, 2);
        }
        setValueAt(document.get("title"), rowIndex, 3);
        setValueAt(document.get("year"), rowIndex, 4);
        setValueAt(getFormatDate(document.get("created_at").toString()), rowIndex, 5);
    }

    public void insertData(List<Document> newData) {
        if (newData == null) {
            clear();
            fireTableDataChanged();
        } else {
            DecimalFormat df = new DecimalFormat("###");
            data = new Object[newData.size()][columnNames.length];

            docs = new ArrayList<Document>();
            for (Document d : newData) {
                docs.add(d);
            }

            int i = 0;
            for (Document d : newData) {
                setValueAt(d.retrieveMetadata().get("uploaded"), i, 0);
                setValueAt(df.format(Organizer.instance().getQuality(d) * 100), i, 1);
                if (d.retrieveMetadata().containsKey("author")) {
                    setValueAt(d.retrieveMetadata().get("author"), i, 2);
                } else {
                    setValueAt(null, i, 2);
                }
                setValueAt(d.get("title"), i, 3);
                setValueAt(d.get("year"), i, 4);
                setValueAt(getFormatDate(d.get("created_at").toString()), i, 5);
                i++;
            }
        }
    }

    public void clear() {
        data = new Object[0][0];
    }

    private String getFormatDate(String date) {
        String formatDate = "";
        formatDate += months[Integer.parseInt(date.substring(5, 7))] + " ";
        formatDate += Integer.parseInt(date.substring(8, 10)) + " ";
        formatDate += date.substring(0, 4);
        return formatDate;
    }
}
