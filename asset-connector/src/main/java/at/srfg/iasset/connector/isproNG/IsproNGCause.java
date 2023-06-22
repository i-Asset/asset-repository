package at.srfg.iasset.connector.isproNG;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IsproNGCause {
    public IsproNGCause(String code, String description) {
        this.setCode(code);
        this.setDescription(description);
    }

    public IsproNGCause() {

    }

    private String code;
    private String description;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
