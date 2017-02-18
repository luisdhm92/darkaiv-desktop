/**
 * The contents of this file are subject to the license and copyright detailed
 * in the LICENSE and NOTICE files at the root of the source tree and available
 * online at
 *
 * http://www.dspace.org/license/
 */
package org.uclv.darkaiv.dspace;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MediaType;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created with IntelliJ IDEA. User: peterdietz Date: 5/22/13 Time: 9:41 AM To
 * change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "community")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Community extends DSpaceObject {

    //Exandable relationships
    private Bitstream logo;

    private Community parentCommunity;

    private String copyrightText, introductoryText, shortDescription, sidebarText;
    private Integer countItems;

    // Renamed because of xml annotation exception with this attribute and getSubCommunities.
    @XmlElement(name = "subcommunities2", required = true)
    private List<Community> subCommunitiesList = new ArrayList<Community>();

    private List<Collection> collectionsList = new ArrayList<Collection>();

    public Community() {
    }

    public List<Collection> getCollectionsList() {
        return collectionsList;
    }

    public void setCollectionsList(List<Collection> collections) {
        this.collectionsList = collections;
    }

    public Integer getCountItems() {
        return countItems;
    }

    public void setCountItems(Integer countItems) {
        this.countItems = countItems;
    }

    public String getSidebarText() {
        return sidebarText;
    }

    public void setSidebarText(String sidebarText) {
        this.sidebarText = sidebarText;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getIntroductoryText() {
        return introductoryText;
    }

    public void setIntroductoryText(String introductoryText) {
        this.introductoryText = introductoryText;
    }

    public String getCopyrightText() {
        return copyrightText;
    }

    public void setCopyrightText(String copyrightText) {
        this.copyrightText = copyrightText;
    }

    public Community getParentCommunity() {
        return parentCommunity;
    }

    public void setParentCommunity(Community parentCommunity) {
        this.parentCommunity = parentCommunity;
    }

    public Bitstream getLogo() {
        return logo;
    }

    public List<Community> getSubCommunities() {
        return subCommunitiesList;
    }

    public void setSubCommunitiesList(List<Community> subCommunities) {
        this.subCommunitiesList = subCommunities;
    }

    public Community[] getSubCommunities2() throws MalformedURLException {
        TestHelper helper = new TestHelper();
        Client client = helper.createClient();
        String token = helper.loginAdmin();

        // Test anonymous view
        WebResource webResource = client.resource(helper.getServer() + TestHelper.COMMUNITIES + "/" + id + "/communities");

        // Test admin view
        WebResource.Builder builder = webResource.header("rest-dspace-token", token);
        ClientResponse response = builder.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

        Community[] communities = response.getEntity(Community[].class);

        client.destroy();
        return communities;
    }

    public Collection[] getCollections() throws MalformedURLException {
        TestHelper helper = new TestHelper();
        Client client = helper.createClient();
        String token = helper.loginAdmin();

        // Test anonymous view
        WebResource webResource = client.resource(helper.getServer() + TestHelper.COMMUNITIES + "/" + id + "/collections");

        // Test admin view
        WebResource.Builder builder = webResource.header("rest-dspace-token", token);
        ClientResponse response = builder.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
        Collection[] collections = response.getEntity(Collection[].class);

        client.destroy();
        return collections;
    }
}
