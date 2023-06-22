package at.srfg.iasset.connector.isproNG;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IsproNGOData {
    public IsproNGOData(){

    }

    public IsproNGCause[] getValue() {
        return value;
    }

    public void setValue(IsproNGCause[] value) {
        this.value = value;
    }

    private IsproNGCause[] value;
}
