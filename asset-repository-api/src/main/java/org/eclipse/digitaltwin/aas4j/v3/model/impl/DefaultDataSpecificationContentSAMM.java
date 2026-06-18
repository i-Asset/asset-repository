package org.eclipse.digitaltwin.aas4j.v3.model.impl;

import java.util.List;
import java.util.Objects;

import org.eclipse.digitaltwin.aas4j.v3.model.DataSpecificationContentSAMM;
import org.eclipse.digitaltwin.aas4j.v3.model.LangStringNameType;
import org.eclipse.digitaltwin.aas4j.v3.model.LangStringTextType;
import org.eclipse.digitaltwin.aas4j.v3.model.annotations.IRI;

public class DefaultDataSpecificationContentSAMM implements DataSpecificationContentSAMM {

    @IRI("https://admin-shell.io/aas/3/1/DataSpecificationContentSAMM/iri")
    private String iri;
    @IRI("https://admin-shell.io/aas/3/1/DataSpecificationContentSAMM/dataSpecificationContentType")
    private String dataSpecificationContentType;
    @IRI("https://admin-shell.io/aas/3/1/DataSpecificationContentSAMM/sourceOfDefinition")
    private String sourceOfDefinition;
    @IRI("https://admin-shell.io/aas/3/1/DataSpecificationContentSAMM/prefererredName")
    private List<LangStringNameType> prefererredName;
    @IRI("https://admin-shell.io/aas/3/1/DataSpecificationContentSAMM/description")
    private List<LangStringTextType> description;
    @IRI("https://admin-shell.io/aas/3/1/DataSpecificationContentSAMM/seeAlso")
    private List<String> seeAlso;

    
    @Override
    public String getIRI() {
        return iri;
    }

    @Override
    public void setIRI(String iri) {
        this.iri = iri;
    }

    @Override
    public String getDataSpecificationContentType() {
        return dataSpecificationContentType;
    }

    @Override
    public void setDataSpecificationContentType(String dataSpecificationContentType) {
        this.dataSpecificationContentType = dataSpecificationContentType;
    }

    @Override
    public String getSourceOfDefinition() {
        return sourceOfDefinition;
    }

    @Override
    public void setSourceOfDefinition(String sourceOfDefinition) {
        this.sourceOfDefinition = sourceOfDefinition;
    }

    @Override
    public List<LangStringNameType> getPrefererredName() {
        return prefererredName;
    }

    @Override
    public void setPrefererredName(List<LangStringNameType> prefererredName) {
        this.prefererredName = prefererredName;
    }

    @Override
    public List<LangStringTextType> getDescription() {
        return description;
    }

    @Override
    public void setDescription(List<LangStringTextType> description) {
        this.description = description;
    }

    @Override
    public List<String> getSeeAlso() {
        return seeAlso;
    }

    @Override
    public void setSeeAlso(List<String> seeAlso) {
        this.seeAlso = seeAlso;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.iri);
        hash = 43 * hash + Objects.hashCode(this.dataSpecificationContentType);
        hash = 43 * hash + Objects.hashCode(this.sourceOfDefinition);
        hash = 43 * hash + Objects.hashCode(this.prefererredName);
        hash = 43 * hash + Objects.hashCode(this.description);
        hash = 43 * hash + Objects.hashCode(this.seeAlso);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DefaultDataSpecificationContentSAMM other = (DefaultDataSpecificationContentSAMM) obj;
        if (!Objects.equals(this.iri, other.iri)) {
            return false;
        }
        if (!Objects.equals(this.dataSpecificationContentType, other.dataSpecificationContentType)) {
            return false;
        }
        if (!Objects.equals(this.sourceOfDefinition, other.sourceOfDefinition)) {
            return false;
        }
        if (!Objects.equals(this.prefererredName, other.prefererredName)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        return Objects.equals(this.seeAlso, other.seeAlso);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DefaultDataSpecificationContentSAMM{");
        sb.append("iri=").append(iri);
        sb.append(", dataSpecificationContentType=").append(dataSpecificationContentType);
        sb.append(", sourceOfDefinition=").append(sourceOfDefinition);
        sb.append(", prefererredName=").append(prefererredName);
        sb.append(", description=").append(description);
        sb.append(", seeAlso=").append(seeAlso);
        sb.append('}');
        return sb.toString();
    }

}
