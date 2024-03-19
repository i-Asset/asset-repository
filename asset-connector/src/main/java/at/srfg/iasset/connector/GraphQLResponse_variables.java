package at.srfg.iasset.connector;

import at.srfg.iasset.repository.model.ZenonVariable;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GraphQLResponse_variables {
    private Data_variables data;

    // Getters, setters, constructors...


    public Data_variables getData() {
        return data;
    }

    public void setData(Data_variables data) {
        this.data = data;
    }
}

class Data_variables {

    @JsonProperty("variables")
    private List<ZenonVariable> variables;

    // Public getters and setters for alarmData
    public List<ZenonVariable> getVariablesData() {
        return variables;
    }

    public void setAlarmData(List<ZenonVariable> variables) {
        this.variables = variables;
    }

    // Getters, setters, constructors...
}