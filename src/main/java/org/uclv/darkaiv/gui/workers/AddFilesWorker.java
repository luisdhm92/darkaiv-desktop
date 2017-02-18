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
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import org.uclv.darkaiv.organizer.Organizer;

/**
 *
 * @author fenriquez
 */
public class AddFilesWorker extends SwingWorker< Integer, Integer> {

    private final Random generator = new Random();
    private String collection;
    private String[] files;
    private JLabel status;
    private JProgressBar progressBar;
    private JButton cancel;

    public AddFilesWorker(String[] files, String collection, JLabel status, JProgressBar progressBar, JButton cancel) {
        this.files = files;
        this.collection = collection;
        this.status = status;
        this.progressBar = progressBar;
        this.cancel = cancel;

        this.cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancel(true);
            }
        });
    }

    @Override
    protected Integer doInBackground() throws Exception {
        int count = 0; // the number of files processed

        if (files.length != 0) {
            status.setText("Processing file 1 of " + files.length);
            status.setVisible(true);
            progressBar.setValue(0);
            progressBar.setVisible(true);
            cancel.setVisible(true);

            Organizer.instance().open();

            for (String file : files) {
                if (isCancelled()) // if the process has been canceled
                {
                    return count;
                } else {
                    

                    Organizer.instance().addFile(file, collection);

                    setProgress(100 * (count + 1) / files.length);

                    publish(count + 1);
                    ++count;
                } // end else
            }
            Organizer.instance().close();
        }

        return count;
    }

    @Override
    protected void process(List< Integer> publishedVals) {
        if (!isCancelled()) {
            status.setText("Processing file " + (publishedVals.get(publishedVals.size() - 1) + 1) + " of " + files.length);
            status.setVisible(true);
        }
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
            progressBar.setVisible(false);
            cancel.setVisible(false);
            return;
        } // end catch
        catch (ExecutionException exception) {
            status.setText("Error performing computation.");
            status.setVisible(true);
            progressBar.setVisible(false);
            cancel.setVisible(false);
            return;
        } // end catch
        catch (CancellationException exception) {
            status.setText("Cancelled.");
            status.setVisible(true);
            progressBar.setVisible(false);
            cancel.setVisible(false);
            return;
        } // end catch

        status.setText("Processed " + count + " files.");
        status.setVisible(true);
        progressBar.setVisible(false);
        cancel.setVisible(false);
    } // end method done
}
