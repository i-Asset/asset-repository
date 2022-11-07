package at.srfg.iasset.semantic.eclass.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the classification_class_property_value database table.
 * 
 */
@Entity
@Table(name="eclass_classification_class_property_value")
@NamedQuery(name="ClassificationClassPropertyValue.findAll", query="SELECT c FROM ClassificationClassPropertyValue c")
public class ClassificationClassPropertyValue implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ClassificationClassPropertyValuePK id;

	@Column(name="value_constraint")
	private String valueConstraint;

	//bi-directional many-to-one association to ClassificationClass
	@ManyToOne
	@JoinColumn(name="irdicc", insertable = false, updatable = false)
	private ClassificationClass classificationClass;

	//bi-directional many-to-one association to EclassValue
	@ManyToOne
	@JoinColumn(name="irdiva", insertable = false, updatable = false)
	private PropertyValue value;

	//bi-directional many-to-one association to Property
	@ManyToOne
	@JoinColumn(name="irdipr", insertable = false, updatable = false)
	private PropertyDefinition property;

	public ClassificationClassPropertyValue() {
	}

	public ClassificationClassPropertyValuePK getId() {
		return this.id;
	}

	public void setId(ClassificationClassPropertyValuePK id) {
		this.id = id;
	}

	public String getValueConstraint() {
		return this.valueConstraint;
	}

	public void setValueConstraint(String valueConstraint) {
		this.valueConstraint = valueConstraint;
	}

	public ClassificationClass getClassificationClass() {
		return this.classificationClass;
	}

	public void setClassificationClass(ClassificationClass classificationClass) {
		this.classificationClass = classificationClass;
	}

	public PropertyValue getValue() {
		return this.value;
	}

	public void setValue(PropertyValue eclassValue) {
		this.value = eclassValue;
	}

	public PropertyDefinition getProperty() {
		return this.property;
	}

	public void setProperty(PropertyDefinition property) {
		this.property = property;
	}

}