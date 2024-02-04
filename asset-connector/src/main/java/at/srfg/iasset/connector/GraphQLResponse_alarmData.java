package at.srfg.iasset.connector;

import at.srfg.iasset.repository.model.ZenonAlarm;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GraphQLResponse_alarmData {
    private Data_alarmData data;

    // Getters, setters, constructors...


    public Data_alarmData getData() {
        return data;
    }

    public void setData(Data_alarmData data) {
        this.data = data;
    }
}

class Data_alarmData {

    @JsonProperty("alarmData")
    private List<ZenonAlarm> alarmData;

    // Public getters and setters for alarmData
    public List<ZenonAlarm> getAlarmData() {
        return alarmData;
    }

    public void setAlarmData(List<ZenonAlarm> alarmData) {
        this.alarmData = alarmData;
    }

    // Getters, setters, constructors...
}