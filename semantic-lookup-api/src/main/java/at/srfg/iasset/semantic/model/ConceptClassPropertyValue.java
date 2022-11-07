package at.srfg.iasset.semantic.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="concept_class_property_value")
public class ConceptClassPropertyValue {
	@EmbeddedId
	private ConceptClassPropertyValuePK pk;
	
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="class_id", insertable = false, updatable = false),
		@JoinColumn(name="property_id", insertable = false, updatable = false)
	})
	private ConceptClassProperty classProperty;
	
	@ManyToOne
	@JoinColumn(name="value_id", insertable=false, updatable = false)
	private ConceptPropertyValue propertyValue;

	public ConceptClassPropertyValue() {
		// default
	}
	public ConceptClassPropertyValue(ConceptClassProperty context, ConceptPropertyValue value) {
		this.pk = new ConceptClassPropertyValuePK(context, value);
		this.propertyValue = value;
	}
	
	public ConceptClassPropertyValuePK getPk() {
		return pk;
	}

	public void setPk(ConceptClassPropertyValuePK pk) {
		this.pk = pk;
	}

	public ConceptPropertyValue getValue() {
		return propertyValue;
	}

	public void setValue(ConceptPropertyValue value) {
		this.propertyValue = value;
	}

	public ConceptClass getConceptClass() {
		return classProperty.getConceptClass();
	}

	public ConceptProperty getProperty() {
		return classProperty.getProperty();
	}


}
