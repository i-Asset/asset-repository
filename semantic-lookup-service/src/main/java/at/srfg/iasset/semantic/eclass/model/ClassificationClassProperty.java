package at.srfg.iasset.semantic.eclass.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the classification_class_property database table.
 * 
 */
@Entity
@Table(name="eclass_classification_class_property")
@NamedQuery(name="ClassificationClassProperty.findAll", query="SELECT c FROM ClassificationClassProperty c")
public class ClassificationClassProperty implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ClassificationClassPropertyPK id;
	@Column(name = "classcodedname")
	private String classCodedName;
	@Column(name="idcc")
	private String idCC;
	@Column(name="idpr")
	private String idPR;
	@Column(name="supplieridcc")
	private String supplierIdCC;
	@Column(name="supplieridpr")
	private String supplierIdPR;

	//bi-directional many-to-one association to ClassificationClass
	@ManyToOne
	@JoinColumn(name="irdicc", insertable=false, updatable=false)
	private ClassificationClass classificationClass;

	//bi-directional many-to-one association to Property
	@ManyToOne
	@JoinColumn(name="irdipr", insertable=false, updatable=false)
	private PropertyDefinition property;

	public ClassificationClassProperty() {
	}

	public ClassificationClassPropertyPK getId() {
		return this.id;
	}

	public void setId(ClassificationClassPropertyPK id) {
		this.id = id;
	}

	public String getClassCodedName() {
		return this.classCodedName;
	}

	public void setClassCodedName(String classcodedname) {
		this.classCodedName = classcodedname;
	}

	public String getIdCC() {
		return this.idCC;
	}

	public void setIdCC(String idcc) {
		this.idCC = idcc;
	}

	public String getIdPR() {
		return this.idPR;
	}

	public void setIdPR(String idpr) {
		this.idPR = idpr;
	}

	public String getSupplierIdCC() {
		return this.supplierIdCC;
	}

	public void setSupplierIdCC(String supplieridcc) {
		this.supplierIdCC = supplieridcc;
	}

	public String getSupplierIdPR() {
		return this.supplierIdPR;
	}

	public void setSupplierIdPR(String supplieridpr) {
		this.supplierIdPR = supplieridpr;
	}

	public ClassificationClass getClassificationClass() {
		return this.classificationClass;
	}

	public void setClassificationClass(ClassificationClass classificationClass) {
		this.classificationClass = classificationClass;
	}

	public PropertyDefinition getProperty() {
		return this.property;
	}

	public void setProperty(PropertyDefinition property) {
		this.property = property;
	}

}