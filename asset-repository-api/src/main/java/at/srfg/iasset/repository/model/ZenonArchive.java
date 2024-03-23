package at.srfg.iasset.repository.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public class ZenonArchive {
    private String variableName;
    private String variableDataType;
    private String stringValue;
    private double numericValue;
    private String calculation;
    private Instant timestamp;


    // Getters
    public String getVariableName() { return variableName; }
    public String getVariableDataType() { return variableDataType; }
    public String getStringValue() { return stringValue; }
    public double getNumericValue() { return numericValue; }
    public String getCalculation() { return calculation; }
    public Instant getTimestamp() { return timestamp; }

    // Setters
    public void setVariableName(String variableName) { this.variableName = variableName; }
    public void setVariableDataType(String variableDataType) { this.variableDataType = variableDataType; }
    public void setStringValue(String stringValue) { this.stringValue = stringValue; }
    public void setNumericValue(double numericValue) { this.numericValue = numericValue; }
    public void setCalculation(String calculation) { this.calculation = calculation; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}