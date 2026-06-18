package org.eclipse.digitaltwin.aas4j.v3.model;

import java.util.List;

import org.eclipse.digitaltwin.aas4j.v3.model.annotations.KnownSubtypes;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultDataSpecificationContentSAMM;

@KnownSubtypes({@KnownSubtypes.Type(value = DefaultDataSpecificationContentSAMM.class)})
public interface DataSpecificationContentSAMM extends CustomDataSpecification {

    public String getIRI();

    public void setIRI(String iri);

    public String getDataSpecificationContentType();

    public void setDataSpecificationContentType(String dataSpecificationContentType);

    public String getSourceOfDefinition();

    public void setSourceOfDefinition(String sourceOfDefinition);

    public List<LangStringNameType> getPrefererredName();

    public void setPrefererredName(List<LangStringNameType> prefererredName);

    public List<LangStringTextType> getDescription();

    public void setDescription(List<LangStringTextType> description);

    public List<String> getSeeAlso();

    public void setSeeAlso(List<String> seeAlso);

}
