package at.srfg.iasset.connector.isproNG;

public class IsproNGErrorCause {
    public IsproNGErrorCause(String code, String description) {
        this.setCode(code);
        this.setDescription(description);
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
