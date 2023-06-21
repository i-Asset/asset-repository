package at.srfg.iasset.connector.isproNG;

import com.google.gson.annotations.SerializedName;

public class IsproNGEventPost {

    @SerializedName(value="model", alternate={"Model"})
    IsproNGMaintenanceAlert model;
}
