package org.eclipse.digitaltwin.aas4j.v3.model;

import java.util.List;

public interface DataSpecificationContentRDF extends CustomDataSpecification {

	String getIri();

	void setIri(String iri);

	String getResourceType();

	void setResourceType(String resourceType);

	List<LangStringNameType> getPreferredName();

	void setPreferredName(List<LangStringNameType> preferredName);

	List<LangStringTextType> getDescription();

	void setDescription(List<LangStringTextType> description);

	List<String> getSameAs();

	void setSameAs(List<String> sameAs);

}