/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.gui.workers;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import org.uclv.darkaiv.dspace.Community;
import org.uclv.darkaiv.gui.TreeEntry;
import org.uclv.darkaiv.organizer.Organizer;

/**
 *
 * @author fenriquez
 */
public class LoadTopCommunitiesWorker extends SwingWorker< Integer, Integer> {

    public static DefaultMutableTreeNode root;

    private String server;
    private String instance;
    private JLabel progress;

    public LoadTopCommunitiesWorker(String instance, String server, JLabel progress) {
        this.instance = instance;
        this.server = server;
        this.progress = progress;
    }

    @Override
    protected Integer doInBackground() throws Exception {
        int count = 0; // the number of files processed

        root = new DefaultMutableTreeNode(new TreeEntry(instance + " [" + server + "]", null, TreeEntry.ROOT, null, -1));

        progress.setVisible(true);
        progress.setText("Loading...");

        if (isCancelled()) {
            return count;
        }

        progress.setText("Loading...");

        try {
            Community[] communitys = Organizer.instance().getopCommunitys();

            for (Community community : communitys) {
                progress.setText("Loading...");
                if (isCancelled()) {
                    return count;
                } else {
                    setProgress(100 * (count + 1) / communitys.length);
                    root.add(new DefaultMutableTreeNode(new TreeEntry(community.getName(), new ImageIcon(getClass().getResource("img/users.png")), TreeEntry.COMMUNITY, community, community.getId())));
                    publish(count + 1);
                    count++;
                }
            }
        } catch (MalformedURLException exception) {
        }

        return count;
    }

    @Override
    protected void process(List< Integer> publishedVals) {
        progress.setText("Loading...");
    } // end method process
    // code to execute when doInBackground completes

    @Override
    protected void done() {
        try {
            get(); // retrieve doInBackground return value
        } // end try
        catch (InterruptedException exception) {
        } // end catch
        catch (ExecutionException exception) {
            progress.setText("Online connection to DSpace fails");
        } // end catch
        catch (CancellationException exception) {
        } // end catch
//        progress.setVisible(false);
    } // end method done
}
