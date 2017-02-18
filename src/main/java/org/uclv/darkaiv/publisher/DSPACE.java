/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uclv.darkaiv.publisher;

import org.uclv.darkaiv.dspace.ItemFactory;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import javax.ws.rs.core.MediaType;
import org.uclv.darkaiv.dspace.Bitstream;
import org.uclv.darkaiv.dspace.Community;
import org.uclv.darkaiv.dspace.Item;
import org.uclv.darkaiv.dspace.TestHelper;

import org.uclv.darkaiv.model.Document;

/**
 *
 * @author daniel
 */
public class DSPACE extends PublishStrategy {
    
    private ItemFactory itemFactory;

    public ItemFactory getItemFactory() {
        return itemFactory;
    }

    
    public void setItemFactory(ItemFactory itemFactory) {
        this.itemFactory = itemFactory;
    }
    
    

    // implement a structure to cache values
    @Override
    public int publish(String collectionId, Document doc) throws MalformedURLException {
        TestHelper helper = new TestHelper();
        Client client = helper.createClient();
        String token = helper.loginAdmin();

        //ItemFactory factory = new ItemFactory();
        Item item = itemFactory.createItem(doc);

        // Test admin creation.
        // Create item
        WebResource webResource = client.resource(helper.getServer() + TestHelper.COLLECTIONS + "/" + collectionId + TestHelper.ITEMS);
        Builder builder = webResource.header("rest-dspace-token", token);

        ClientResponse response = builder.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, item);

        if (response.getStatus() == 200) {
            item = response.getEntity(Item.class);
            client.destroy();

            return item.getId();
        } else {
            client.destroy();

            return -1;
        }
    }

    public boolean uploadBitstream(long item, String path) throws FileNotFoundException, MalformedURLException {
        TestHelper helper = new TestHelper();
        Client client = helper.createClient();
        String token = helper.loginAdmin();

        //InputStream is = new FileInputStream(TestHelper.getPath("pdf1.pdf"));
//        InputStream is = new FileInputStream(helper.getPath(path));
        InputStream is = new FileInputStream(path);
        WebResource webResource = client.resource(helper.getServer() + TestHelper.ITEMS + "/" + item + TestHelper.BITSTREAMS);
        Builder builder = webResource.header("rest-dspace-token", token);
        ClientResponse response = builder.accept(MediaType.APPLICATION_JSON).post(ClientResponse.class, is);
        String bitstreamId = response.getEntity(Bitstream.class).getId().toString();

        Bitstream mockBitstream = new Bitstream();
        File file = new File(path);
        mockBitstream.setDescription(file.getName());
        mockBitstream.setName(file.getName());

        webResource = client.resource(helper.getServer() + TestHelper.BITSTREAMS + "/" + bitstreamId);
        builder = webResource.header("rest-dspace-token", token);
        response = builder.accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, mockBitstream);
        client.destroy();

        if (response.getStatus() == 200) {
//            mockBitstream = response.getEntity(Bitstream.class);
//            return mockBitstream.getId();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean update(String collectionId, Document doc) {

        return true;
    }

    public Community[] getAllCommunities() throws MalformedURLException {
        TestHelper helper = new TestHelper();
        Client client = helper.createClient();
        String token = helper.loginAdmin();

        // Test anonymous view
        WebResource webResource = client.resource(helper.getServer() + TestHelper.COMMUNITIES);

        // Test admin view
        WebResource.Builder builder = webResource.header("rest-dspace-token", token);
        ClientResponse response = builder.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        Community[] communities = response.getEntity(Community[].class);

        client.destroy();
        return communities;
    }

    public Community[] getTopCommunities() throws MalformedURLException {
        TestHelper helper = new TestHelper();
        Client client = helper.createClient();
        String token = helper.loginAdmin();

        // Test anonymous view
        WebResource webResource = client.resource(helper.getServer() + TestHelper.TOP_COMMUNITIES);

        // Test admin view
        WebResource.Builder builder = webResource.header("rest-dspace-token", token);
        ClientResponse response = builder.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        Community[] communities = response.getEntity(Community[].class);

        client.destroy();
        return communities;
    }
}
