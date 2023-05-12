package at.srfg.iasset.semantic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name="concept_property_value")
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name="identifier")
public class ConceptPropertyValue extends ConceptBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name="value")
	private String value;
	@Column(name="reference")
	private String reference;
	@Column(name="data_type")
	private DataTypeEnum dataType;
	
	public ConceptPropertyValue() {
		//
	}
	public ConceptPropertyValue(String id) {
		super(id);
		//
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public DataTypeEnum getDataType() {
		return dataType;
	}
	public void setDataType(DataTypeEnum dataType) {
		this.dataType = dataType;
	}
}
