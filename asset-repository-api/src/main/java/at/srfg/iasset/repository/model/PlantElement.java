package at.srfg.iasset.repository.model;

import java.util.Collections;
import java.util.List;

public class PlantElement {

    String name = "defaultName";
    String description = "defaultDescription";
    List<String> identifiers = Collections.singletonList("defaultIdentifier");
    PlantElement parent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<String> identifiers) {
        this.identifiers = identifiers;
    }

    public PlantElement getParent() {
        return parent;
    }

    public void setParent(PlantElement parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "PlantElement{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", identifiers=" + identifiers +
                ", parent=" + parent +
                '}';
    }

    
}
