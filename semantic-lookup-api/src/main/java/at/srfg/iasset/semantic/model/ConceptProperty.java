package at.srfg.iasset.semantic.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlTransient;


@Entity
@Table(name="concept_property")
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name="identifier")
public class ConceptProperty extends ConceptBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name="category")
	private String category;
	@Column(name="source_of_definition")
	private String sourceOfDefinition;
	@Column(name="coded_values", nullable=false)
	private boolean coded;
	/**
	 * DATE
	 * rational_measure
	 */
	@Column(name="data_type")
	private DataTypeEnum dataType;
	@JsonIgnore
	@XmlTransient
	@OneToMany(mappedBy = "property", cascade = {CascadeType.REMOVE})
	private List<ConceptClassProperty> classProperty;

	//bi-directional many-to-many association to EclassValue
	@ManyToMany(cascade = {CascadeType.ALL, CascadeType.REMOVE})
	@JoinTable(
		name="concept_property_value_assignment"
		, joinColumns={
			@JoinColumn(name="property_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="value_id")
			}
		)
	private Set<ConceptPropertyValue> values = new HashSet<>();
	

	//bi-directional many-to-one association to Unit
	@ManyToOne
	@JoinColumn(name="unit_id")
	private ConceptPropertyUnit unit;
	
	public ConceptProperty() {
		// default
	}
	public ConceptProperty(String id) {
		super(id);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSourceOfDefinition() {
		return sourceOfDefinition;
	}

	public void setSourceOfDefinition(String sourceOfDefinition) {
		this.sourceOfDefinition = sourceOfDefinition;
	}


	public DataTypeEnum getDataType() {
		return dataType;
	}

	public void setDataType(DataTypeEnum dataType) {
		this.dataType = dataType;
	}



	public boolean isCoded() {
		return coded;
	}
	public void setCoded(boolean coded) {
		this.coded = coded;
	}
	public ConceptPropertyUnit getUnit() {
		return unit;
	}
	public void setUnit(ConceptPropertyUnit unit) {
		this.unit = unit;
	}
	public Set<ConceptPropertyValue> getValues() {
		if ( values==null) {
			values = new HashSet<ConceptPropertyValue>();
		}
		return values;
	}

	public void setValues(Set<ConceptPropertyValue> values) {
		this.values = values;
	}
	public void addPropertyValue(ConceptPropertyValue value) {

		this.getValues().add(value);
	}
	public boolean removePropertyValue(ConceptPropertyValue value) {
		return this.getValues().remove(value);
	}
}
