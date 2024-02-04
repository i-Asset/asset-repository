package at.srfg.iasset.repository.model;


public class ZenonVariable {
    private String variableName;
    private String displayName;
    private String identification;
    private String description;
    private String dataType;
    private String resourcesLabel;
    private String measuringUnit;


    // Getters and setters
    public String getVariableName() {
        return variableName;
    }
    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getIdentification() {
        return identification;
    }
    public void setIdentification(String identification) {
        this.identification = identification;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    public String getDataType() {
        return dataType;
    }
    public void setResourcesLabel(String resourceLabel) {
        this.resourcesLabel = resourceLabel;
    }
    public String getResourcesLabel() {
        return resourcesLabel;
    }
    public void setMeasuringUnit(String measuringUnit) {
        this.measuringUnit = measuringUnit;
    }
    public String getMeasuringUnit() {
        return measuringUnit;
    }
}