package at.srfg.iasset.connector.isproNG;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IsproNGOData<T> {
    public IsproNGOData(){

    }

    public T[] getValue() {
        return value;
    }

    public void setValue(T[] value) {
        this.value = value;
    }

    private T[] value;
}
