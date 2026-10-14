package org.eclipse.digitaltwin.aas4j.v3.model.impl;

import java.util.List;
import java.util.Objects;

import org.eclipse.digitaltwin.aas4j.v3.model.DataSpecificationContentRDF;
import org.eclipse.digitaltwin.aas4j.v3.model.LangStringNameType;
import org.eclipse.digitaltwin.aas4j.v3.model.LangStringTextType;
import org.eclipse.digitaltwin.aas4j.v3.model.annotations.IRI;
import org.eclipse.digitaltwin.aas4j.v3.model.builder.DataSpecificationContentRDFBuilder;

public class DefaultDataSpecificationContentRDF implements DataSpecificationContentRDF {

	@IRI("https://admin-shell.io/aas/3/1/DataSpecificationContentRDF/iri")
	private String iri;

	@IRI("https://admin-shell.io/aas/3/1/DataSpecificationContentRDF/resourceType")
	private String resourceType;

	@IRI("https://admin-shell.io/aas/3/1/DataSpecificationContentRDF/preferredName")
	private List<LangStringNameType> preferredName;

	@IRI("https://admin-shell.io/aas/3/1/DataSpecificationContentRDF/description")
	private List<LangStringTextType> description;

	@IRI("https://admin-shell.io/aas/3/1/DataSpecificationContentRDF/sameAs")
	private List<String> sameAs;

	@Override
	public String getIri() {
		return iri;
	}

	@Override
	public void setIri(String iri) {
		this.iri = iri;
	}

	@Override
	public String getResourceType() {
		return resourceType;
	}

	@Override
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
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
	public List<LangStringTextType> getDescription() {
		return description;
	}

	@Override
	public void setDescription(List<LangStringTextType> description) {
		this.description = description;
	}

	@Override
	public List<String> getSameAs() {
		return sameAs;
	}

	@Override
	public void setSameAs(List<String> sameAs) {
		this.sameAs = sameAs;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, iri, preferredName, resourceType, sameAs);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefaultDataSpecificationContentRDF other = (DefaultDataSpecificationContentRDF) obj;
		return Objects.equals(description, other.description) && Objects.equals(iri, other.iri)
				&& Objects.equals(preferredName, other.preferredName)
				&& Objects.equals(resourceType, other.resourceType) && Objects.equals(sameAs, other.sameAs);
	}

	@Override
	public String toString() {
		return "DefaultDataSpecificationContentRDF [iri=" + iri + ", preferredName=" + preferredName + ", resourceType="
				+ resourceType + "]";
	}

	/**
	 * This builder class can be used to construct a DefaultOperationRequest bean.
	 */
	public static class Builder extends DataSpecificationContentRDFBuilder<DataSpecificationContentRDF, Builder> {

		@Override
		protected Builder getSelf() {
			return this;
		}

		@Override
		protected DataSpecificationContentRDF newBuildingInstance() {
			return new DefaultDataSpecificationContentRDF();
		}
	}

}
