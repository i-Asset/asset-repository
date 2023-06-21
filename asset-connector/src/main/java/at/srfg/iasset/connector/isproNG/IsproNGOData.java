package at.srfg.iasset.connector.isproNG;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IsproNGOData {
    public IsproNGOData(){

    }

    public IsproNGErrorCause[] getValue() {
        return value;
    }

    public void setValue(IsproNGErrorCause[] value) {
        this.value = value;
    }

    private IsproNGErrorCause[] value;
}
