/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.gui.workers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import org.uclv.darkaiv.exceptions.ConnectionCanNotBeEstablishException;
import org.uclv.darkaiv.exceptions.DocumentNotFoundException;
import org.uclv.darkaiv.exceptions.OnlineConnectionFailsException;
import org.uclv.darkaiv.gui.Main;
import org.uclv.darkaiv.model.Document;
import org.uclv.darkaiv.organizer.Organizer;

/**
 *
 * @author fenriquez
 */
public class GrobidReviewWorker extends SwingWorker< Integer, Integer> {

    private final Random generator = new Random();
    private int[] indexes;
    private List<Document> docs;
    private JLabel status;
    private JProgressBar progressBar;
    private JButton cancel;

    public GrobidReviewWorker(int[] indexes, List<Document> docs, JLabel status, JProgressBar progressBar, JButton cancel) {
        this.indexes = indexes;
        this.docs = docs;
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
    protected Integer doInBackground() {
        int count = 0; // the number of files processed

        if (indexes.length != 0) {
            status.setText("Processing document 1 of " + indexes.length);
            status.setVisible(true);
            progressBar.setValue(0);
            progressBar.setVisible(true);
            cancel.setVisible(true);

            Organizer.instance().open();

            for (int index : indexes) {
                if (isCancelled()) // if the process has been canceled
                {
                    return count;
                } else {
                    
                    try {
                        Main.instance().update(Organizer.instance().extractMDGrobid(docs.get(index)), index, indexes.length == 1);
                    } catch (OnlineConnectionFailsException ex) {
                        JOptionPane.showMessageDialog(null, "Online connection to Grobid Service fails. Please check your Internet connection", "Error", JOptionPane.ERROR_MESSAGE);
                        status.setText("Online Connection Fails. Please check your Internet connection. Processed " + count + " of " + indexes.length + " documents");
                        status.setVisible(true);
                        progressBar.setVisible(false);
                        return count;
                    } catch (IOException ex) {
                        status.setText("Can't found Grobid configurations, please check tour configurations in config folder. Processed " + count + " of " + indexes.length + " documents");
                        status.setVisible(true);
                        progressBar.setVisible(false);
                        return count;
                    } catch (DocumentNotFoundException ex) {
                        status.setText("Document not found. Processed " + count + " of " + indexes.length + " documents");
                        status.setVisible(true);
                        progressBar.setVisible(false);
                        return count;
                    } catch (ConnectionCanNotBeEstablishException ex) {
                        // daniel: i don't know why 2 Exceptions with similar names!!!!
                        JOptionPane.showMessageDialog(null, "Online connection to Grobid Service fails. Please check your Internet connection", "Error", JOptionPane.ERROR_MESSAGE);
                        status.setText("Online Connection Fails. Please check your Internet connection. Processed " + count + " of " + indexes.length + " documents");
                        status.setVisible(true);
                        progressBar.setVisible(false);
                        return count;
                    }

                    setProgress(100 * (count + 1) / indexes.length);

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
            status.setText("Processing document " + (publishedVals.get(publishedVals.size() - 1) + 1) + " of " + indexes.length);
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
            exception.printStackTrace();
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
