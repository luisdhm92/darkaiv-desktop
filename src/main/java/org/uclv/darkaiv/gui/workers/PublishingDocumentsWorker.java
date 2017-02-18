/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.gui.workers;

import com.sun.jersey.api.client.ClientHandlerException;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import org.uclv.darkaiv.dspace.Collection;
import org.uclv.darkaiv.model.Document;
import org.uclv.darkaiv.model.File;
import org.uclv.darkaiv.organizer.Organizer;

/**
 *
 * @author fenriquez
 */
public class PublishingDocumentsWorker extends SwingWorker< Integer, Integer> {

    private final Random generator = new Random();
    private int[] indexes;
    private List<Document> docs;
    private JLabel status;
    private JProgressBar progressBar;
    private Collection collection;
    private List<String> published;
    private List<String> bitstream;
    private List<String> unpublished;
    private List<String> url;

    public PublishingDocumentsWorker(int[] indexes, List<Document> docs, JLabel status, JProgressBar progressBar, Collection collection, List<String> published, List<String> bitstream, List<String> unpublished, List<String> url) {
        this.indexes = indexes;
        this.docs = docs;
        this.status = status;
        this.progressBar = progressBar;
        this.collection = collection;

        this.published = published;
        this.bitstream = bitstream;
        this.unpublished = unpublished;
        this.url = url;
    }

    @Override
    protected Integer doInBackground() {
        int count = 0; // the number of files processed

        if (indexes.length != 0) {
            status.setVisible(true);
            progressBar.setValue(0);
            progressBar.setVisible(true);

            Organizer.instance().open();

            for (int index : indexes) {
                
                if (isCancelled()) // if the process has been canceled
                {
                    return count;
                } else {
                    

                    try {
                        status.setText("Processing document " + (count + 1) + " of " + indexes.length + ": " + docs.get(index).get("title"));

                        // this logic should change, although a doc does not exist on disk these metadata could be published
                        if (docs.get(index).validateFile()) {
                            int item = Organizer.instance().publish(collection.getId() + "", docs.get(index));
                            if (item != -1) {
                                File file = File.findFirst("document_id = ?", docs.get(index).getId());
                                boolean upload = Organizer.instance().uploadBitstream(item, file.getString("path"));

                                if (upload) {
                                    published.add(docs.get(index).getString("title"));
                                } else {
                                    bitstream.add(docs.get(index).getString("title"));
                                }
                            } else {
                                unpublished.add(docs.get(index).getString("title"));
                            }
                        } else {
                            System.out.println("The file does no exist");
                            url.add(docs.get(index).getString("title"));
                        }
                    } catch (MalformedURLException exception) {
//                        Logger.getLogger(PublishingDocumentsWorker.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (FileNotFoundException ex) {
//                        Logger.getLogger(PublishingDocumentsWorker.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClientHandlerException ex) {
                        JOptionPane.showMessageDialog(null, "Online connection to DSpace close unexpectedly. Please check your Internet connection", "Error", JOptionPane.ERROR_MESSAGE);
                        return count;
                    }
                    setProgress(100 * (count + 1) / indexes.length);


                    ++count;
                } // end else
            }
            Organizer.instance().close();
        }

        return count;
    }

    @Override
    protected void process(List< Integer> publishedVals) {
//        status.setText("Processing document " + (publishedVals.get(publishedVals.size() - 1) + 1) + " of " + indexes.length);
        status.setVisible(true);
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
//            progressBar.setVisible(false);
            return;
        } // end catch
        catch (ExecutionException exception) {
            exception.printStackTrace();
            status.setText("Error performing computation.");
            status.setVisible(true);
//            progressBar.setVisible(false);
            return;
        } // end catch
        catch (CancellationException exception) {
            status.setText("Cancelled.");
            status.setVisible(true);
//            progressBar.setVisible(false);
            return;
        } // end catch

        status.setText("Processed " + count + " files.");
        status.setVisible(true);
//        progressBar.setVisible(false);
    } // end method done
}
