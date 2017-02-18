/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.gui.workers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import org.uclv.darkaiv.gui.Main;
import org.uclv.darkaiv.organizer.Organizer;

/**
 *
 * @author fenriquez
 */
public class AddFolderWorker extends SwingWorker< Integer, Integer> {

    private final Random generator = new Random();
    private String collection;
    private String folder;
    private JLabel status;
    private JButton cancel;

    public AddFolderWorker(String folder, String collection, JLabel status, JButton cancel) {
        this.folder = folder;
        this.collection = collection;
        this.status = status;

        this.cancel = cancel;

        this.cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancel(true);
            }
        });
    }

    private boolean isPDF(String name) {
        StringTokenizer st = new StringTokenizer(name, ".");
        String token = "";
        while (st.hasMoreElements()) {
            token = st.nextToken();
        }
        return token.equals("pdf");
    }

    @Override
    protected Integer doInBackground() throws Exception {
        int count = 0; // the number of files processed

        Stack<String> foldersStack = new Stack<String>();
        foldersStack.push(folder);
        cancel.setVisible(true);

        Organizer.instance().open();

        while (!foldersStack.isEmpty()) {
            if (isCancelled()) // if the process has been canceled
            {
                return count;
            } else {

                String actualFolder = foldersStack.pop();
                java.io.File file = new java.io.File(actualFolder);
                java.io.File[] files = file.listFiles();

                for (java.io.File subFile : files) {
                    if (isCancelled()) {
                        return count;
                    }
                    if (subFile.isDirectory()) {
                        //System.out.println(file1.getName() + " is a directory");
                        foldersStack.push(subFile.getAbsolutePath());
                    } else {
                        if (isPDF(subFile.getName())) {
                            count++;
                            publish(count);
                            status.setText("Processing file " + count + ": " + subFile.getAbsolutePath());
                            status.setVisible(true);
                            try {
                                System.out.println("File " + subFile.getAbsolutePath());
                                Organizer.instance().addFile(subFile.getAbsolutePath(), collection);
                            } catch (Exception e) {
                                continue;
                            }

                        }
                    }
                }
            }
        }
        return count;
    }

    @Override
    protected void process(List< Integer> publishedVals) {
    } // end method process
    // code to execute when doInBackground completes

    @Override
    protected void done() {
        int count;

        try {
            count = get(); // retrieve doInBackground return value
        } // end try
        catch (InterruptedException exception) {
            status.setText("Interrupted while waiting for results.");
            status.setVisible(true);
            cancel.setVisible(false);
            return;
        } // end catch
        catch (ExecutionException exception) {
            status.setText("Error performing computation.");
            // for testing purpose
            System.out.println("Cause " + exception.getCause());
            System.out.println("Message " + exception.getMessage());
            exception.getStackTrace();
            status.setVisible(true);
            cancel.setVisible(false);
            return;
        } // end catch
        catch (CancellationException exception) {
            status.setText("Cancelled.");
            status.setVisible(true);
            cancel.setVisible(false);
            return;
        } // end catch

        cancel.setVisible(false);
        status.setText("Processed " + count + " files.");
        status.setVisible(true);
        Main.instance().collectionsListValueChanged();
    } // end method done
}
