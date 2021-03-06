/**
 * The contents of this file are subject to the license and copyright detailed
 * in the LICENSE and NOTICE files at the root of the source tree and available
 * online at
 *
 * http://www.dspace.org/license/
 */
package org.uclv.darkaiv.dspace;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

import org.codehaus.jackson.annotate.JsonIgnore;

@XmlType
public class CheckSum {

    @JsonIgnore
    String checkSumAlgorithm;
    String value;

    public CheckSum() {
    }

    @XmlAttribute(name = "checkSumAlgorithm")
    public String getCheckSumAlgorith() {
        return checkSumAlgorithm;
    }

    public void setCheckSumAlgorith(String checkSumAlgorith) {
        this.checkSumAlgorithm = checkSumAlgorith;
    }

    @XmlValue
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
