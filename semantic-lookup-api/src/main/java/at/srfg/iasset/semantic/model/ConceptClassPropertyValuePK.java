package at.srfg.iasset.semantic.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ConceptClassPropertyValuePK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name="class_id", insertable=false, updatable=false)
	private Long classId;
	@Column(name="property_id", insertable=false, updatable=false)
	private Long propertyId;
	@Column(name="value_id", insertable=false, updatable=false)
	private Long propertyValueId;
	public ConceptClassPropertyValuePK() {
		
	}
	public ConceptClassPropertyValuePK(ConceptClassProperty cp, ConceptPropertyValue value) {
		this.classId = cp.getConceptClass().getId();
		this.propertyId = cp.getProperty().getId();
		this.propertyValueId = value.getId();
	}
//	public ConceptClass getConceptClass() {
//		return classId;
//	}
//	public Property getProperty() {
//		return propertyId;
//	}
//	public PropertyValue getPropertyValue() {
//		return propertyValueId;
//	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classId == null) ? 0 : classId.hashCode());
		result = prime * result + ((propertyId == null) ? 0 : propertyId.hashCode());
		result = prime * result + ((propertyValueId == null) ? 0 : propertyValueId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConceptClassPropertyValuePK other = (ConceptClassPropertyValuePK) obj;
		if (classId == null) {
			if (other.classId != null)
				return false;
		} else if (!classId.equals(other.classId))
			return false;
		if (propertyId == null) {
			if (other.propertyId != null)
				return false;
		} else if (!propertyId.equals(other.propertyId))
			return false;
		if (propertyValueId == null) {
			if (other.propertyValueId != null)
				return false;
		} else if (!propertyValueId.equals(other.propertyValueId))
			return false;
		return true;
	}

}
