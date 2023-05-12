package at.srfg.iasset.semantic.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ConceptClassPropertyPK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name="class_id", insertable=false, updatable=false)
	private Long classId;
	@Column(name="property_id", insertable=false, updatable=false)
	private Long propertyId;
	public ConceptClassPropertyPK() {
		
	}
	public ConceptClassPropertyPK(ConceptClass conceptClass, ConceptProperty property) {
		this.classId = conceptClass.getId();
		this.propertyId = property.getId();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classId == null) ? 0 : classId.hashCode());
		result = prime * result + ((propertyId == null) ? 0 : propertyId.hashCode());
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
		ConceptClassPropertyPK other = (ConceptClassPropertyPK) obj;
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
		return true;
	}

}
