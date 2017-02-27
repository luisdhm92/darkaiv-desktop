package org.uclv.darkaiv.dspace;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "edge")
public class Edge {

    private String parentExternalId;
    private String childExternalId;

    public String getParentExternalId() {
        return parentExternalId;
    }

    public void setParentExternalId(String parentExternalId) {
        this.parentExternalId = parentExternalId;
    }

    public String getChildExternalId() {
        return childExternalId;
    }

    public void setChildExternalId(String childExternalId) {
        this.childExternalId = childExternalId;
    }
}
