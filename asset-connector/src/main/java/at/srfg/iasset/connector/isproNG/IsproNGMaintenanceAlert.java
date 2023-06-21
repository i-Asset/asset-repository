package at.srfg.iasset.connector.isproNG;

public class IsproNGMaintenanceAlert {
    public IsproNGMaintenanceAlert(String text, String note, int priority, String alertFreefield1, String causeOfError, String assetId) {
        this.causeOfError = causeOfError;
        this.text = text;
        this.note = note;
        this.priority = priority;
        this.alertFreefield1 = alertFreefield1;
        this.plantStructure = new IsproNGPlantStructureIdentification(assetId);
    }

    public String getCauseOfError() {
        return causeOfError;
    }

    public void setCauseOfError(String causeOfError) {
        this.causeOfError = causeOfError;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getAlertFreefield1() {
        return alertFreefield1;
    }

    public void setAlertFreefield1(String alertFreefield1) {
        this.alertFreefield1 = alertFreefield1;
    }

    String causeOfError;
    String text;
    String note;
    int priority;
    String alertFreefield1;

    public IsproNGPlantStructureIdentification getPlantStructure() {
        return plantStructure;
    }

    public void setPlantStructure(IsproNGPlantStructureIdentification plantStructure) {
        this.plantStructure = plantStructure;
    }

    IsproNGPlantStructureIdentification plantStructure;
}
