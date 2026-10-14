package org.eclipse.digitaltwin.aas4j.v3.model.impl;

import java.util.List;
import java.util.Objects;

import org.eclipse.digitaltwin.aas4j.v3.model.DataSpecificationRDF;
import org.eclipse.digitaltwin.aas4j.v3.model.LangStringNameType;
import org.eclipse.digitaltwin.aas4j.v3.model.LangStringTextType;
import org.eclipse.digitaltwin.aas4j.v3.model.builder.DataSpecificationRDFBuilder;

public class DefaultDataSpecificationRDF implements DataSpecificationRDF {

    private String iri;
    private String localName;
    private String namespace;
    private String rdfType;
    private String dataType;
    private List<LangStringNameType> preferredName;
    private List<LangStringTextType> definition;

    @Override
    public String getIri() {
        return iri;
    }

    @Override
    public void setIri(String iri) {
        this.iri = iri;
    }

    @Override
    public String getLocalName() {
        return localName;
    }

    @Override
    public void setLocalName(String localName) {
        this.localName = localName;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public String getDataType() {
        return dataType;
    }

    @Override
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }   

    @Override
    public String getRdfType() {
        return rdfType;
    }

    @Override
    public void setRdfType(String rdfType) {
        this.rdfType = rdfType;
    }

    @Override
    public List<LangStringNameType> getPreferredName() {
        return preferredName;
    }
    @Override
    public void setPreferredName(List<LangStringNameType> preferredName) {
        this.preferredName = preferredName;
    }

    @Override
    public List<LangStringTextType> getDefinition() {
        return definition;
    }

    @Override
    public void setDefinition(List<LangStringTextType> definition) {
        this.definition = definition;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.iri);
        hash = 67 * hash + Objects.hashCode(this.localName);
        hash = 67 * hash + Objects.hashCode(this.namespace);
        hash = 67 * hash + Objects.hashCode(this.rdfType);
        hash = 67 * hash + Objects.hashCode(this.dataType);
        hash = 67 * hash + Objects.hashCode(this.preferredName);
        hash = 67 * hash + Objects.hashCode(this.definition);
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
        final DefaultDataSpecificationRDF other = (DefaultDataSpecificationRDF) obj;
        if (!Objects.equals(this.iri, other.iri)) {
            return false;
        }
        if (!Objects.equals(this.localName, other.localName)) {
            return false;
        }
        if (!Objects.equals(this.namespace, other.namespace)) {
            return false;
        }
        if (!Objects.equals(this.rdfType, other.rdfType)) {
            return false;
        }
        if (!Objects.equals(this.dataType, other.dataType)) {
            return false;
        }
        if (!Objects.equals(this.preferredName, other.preferredName)) {
            return false;
        }
        return Objects.equals(this.definition, other.definition);
    }

    @Override
    public String toString() {
        return "DefaultDataSpecificationRDF [iri=" + iri + ", localName=" + localName + ", namespace=" + namespace
                + "]";
    }
	/**
	 * This builder class can be used to construct a DefaultDataSpecificationRDF bean.
	 */
	public static class Builder extends DataSpecificationRDFBuilder<DataSpecificationRDF, Builder> {

		@Override
		protected Builder getSelf() {
			return this;
		}

		@Override
		protected DataSpecificationRDF newBuildingInstance() {
			return new DefaultDataSpecificationRDF();
		}
	}

}
