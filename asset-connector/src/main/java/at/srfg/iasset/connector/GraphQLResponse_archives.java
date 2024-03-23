package at.srfg.iasset.connector;

import at.srfg.iasset.repository.model.ZenonArchive;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GraphQLResponse_archives {
    private Data_archiveData data;

    // Getters, setters, constructors...


    public Data_archiveData getData() {
        return data;
    }

    public void setData(Data_archiveData data) {
        this.data = data;
    }
}

class Data_archiveData {

    @JsonProperty("archiveData")
    private List<ZenonArchive> archiveData;

    // Public getters and setters for alarmData
    public List<ZenonArchive> getArchiveData() {
        return archiveData;
    }

    public void setAlarmData(List<ZenonArchive> alarmData) {
        this.archiveData = archiveData;
    }

    // Getters, setters, constructors...
}