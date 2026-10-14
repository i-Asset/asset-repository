package org.eclipse.digitaltwin.aas4j.v3.model;

import java.util.List;

public interface DataSpecificationRDF extends CustomDataSpecification {
	/**
	 * Gets the IRI of the data specification. To be used in the context of RDF, 
	 * this is the IRI of the resource that is described by the data specification
	 * and names the element in an RDF graph. 
	 * The IRI is used to identify the resource in RDF statements and can be used 
	 * to retrieve additional information about the resource from the web.
	 * 
	 * 
	 * @return
	 */
	String getIri();
	void setIri(String iri);
	/**
	 * Gets the local name of the data specification. The local name is the part
	 * of the IRI that identifies the resource within its namespace.
	 * It is used to create a unique identifier for the resource in 
	 * RDF statements and can be used to retrieve additional information about the resource from the web.	
	 * 
	 * @return
	 */
	String getLocalName();
	void setLocalName(String localName);
	/**
	 * Gets the namespace of the data specification. The namespace is the part of 
	 * the IRI that identifies the organization or entity that defines the resource.
	 * It is used to create a unique identifier for the resource in 
	 * RDF statements and can be used to retrieve additional information about the resource from the web.	
	 * @return
	 */
	String getNamespace();
	void setNamespace(String namespace);

	/**
	 * Gets the RDF type of the data specification. The RDF type is a URI that identifies 
	 * the class of the resource in an RDF graph.
	 * It is used to create a unique identifier for the resource in 
	 * RDF statements and can be used to retrieve additional information about the resource from the web.	
	 * @return
	 */
	String getRdfType();
	void setRdfType(String rdfType);
	/**
	 * Gets the data type of the data specification. The data type is a URI that identifies 
	 * the type of the resource in an RDF graph.
	 * It is used to create a unique identifier for the resource in 
	 * RDF statements and can be used to retrieve additional information about the resource from the web.	
	 * @return
	 */
	String getDataType();
	void setDataType(String dataType);

	/**
	 * Gets the preferred name of the data specification. The preferred name is a human-readable
	 * name for the resource that can be used in user interfaces and documentation.
	 * @return
	 */
	List<LangStringNameType> getPreferredName();
	void setPreferredName(List<LangStringNameType> preferredName);

	/**
	 * Gets the definition of the data specification. The definition is a human-readable
	 * description of the resource that can be used in user interfaces and documentation.
	 * @return
	 */
	List<LangStringTextType> getDefinition();
	void setDefinition(List<LangStringTextType> definition);

	

}