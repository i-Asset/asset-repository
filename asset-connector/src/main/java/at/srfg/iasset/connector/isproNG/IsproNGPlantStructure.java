package at.srfg.iasset.connector.isproNG;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IsproNGPlantStructure {
    private int id;
    private String name;
    private String foreignKey1;
    private String structureClass;
    private int layer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getForeignKey1() {
        return foreignKey1;
    }

    public void setForeignKey1(String foreignKey1) {
        this.foreignKey1 = foreignKey1;
    }

    public String getStructureClass() {
        return structureClass;
    }

    public void setStructureClass(String structureClass) {
        this.structureClass = structureClass;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

}
