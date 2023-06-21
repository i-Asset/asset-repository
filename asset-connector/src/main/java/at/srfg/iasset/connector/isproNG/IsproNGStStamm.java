package at.srfg.iasset.connector.isproNG;

import com.google.gson.annotations.SerializedName;

public class IsproNGStStamm {
    public String getText() {
        return text;
    }

    public String getFk1() {
        return fk1;
    }

    public void setFk1(String fk1) {
        this.fk1 = fk1;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNotiz() {
        return notiz;
    }

    public void setNotiz(String notiz) {
        this.notiz = notiz;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }


    public String getMonteur1() {
        return monteur1;
    }

    public void setMonteur1(String monteur1) {
        this.monteur1 = monteur1;
    }

    public String getIhStoerTFreifeld1Meldung() {
        return ihStoerTFreifeld1Meldung;
    }

    public void setIhStoerTFreifeld1Meldung(String ihStoerTFreifeld1Meldung) {
        this.ihStoerTFreifeld1Meldung = ihStoerTFreifeld1Meldung;
    }

    @SerializedName(value="text", alternate={"Text"})
    String text;
    @SerializedName(value="notiz", alternate={"Notiz"})
    String notiz;
    @SerializedName(value="priority", alternate={"Priority"})
    int priority;

    @SerializedName(value="monteur1", alternate={"Monteur1"})
    String monteur1;
    @SerializedName(value="ihStoerTFreifeld1Meldung", alternate={"IhStoerTFreifeld1Meldung"})
    String ihStoerTFreifeld1Meldung;


    @SerializedName(value="fk1", alternate={"Fk1"})
    String fk1;


}
