package at.srfg.iasset.semantic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="concept_property_unit")
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name="identifier")
public class ConceptPropertyUnit extends ConceptBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name="din_notation")
	private String dinNotation;
	@Column(name="ece_code")
	private String eceCode;
	@Column(name="ece_name")
	private String eceName;
	@Column(name="iec_classification")
	private String iecClassification;
	@Column(name="name_of_dedicated_quantity")
	private String nameOfDedicatedQuantity;
	@Column(name="nist_name")
	private String nistName;
	@Column(name="si_name")
	private String siName;
	@Column(name="si_notation")
	private String siNotation;
	@Column(name="source")
	private String source;
	@Column(name="structured_naming")
	private String structuredNaming;
	public ConceptPropertyUnit() {
		// default
	}
	public ConceptPropertyUnit(String id) {
		super(id);
	}
	public String getDinNotation() {
		return dinNotation;
	}
	public void setDinNotation(String dinNotation) {
		this.dinNotation = dinNotation;
	}
	public String getEceCode() {
		return eceCode;
	}
	public void setEceCode(String eceCode) {
		this.eceCode = eceCode;
	}
	public String getEceName() {
		return eceName;
	}
	public void setEceName(String eceName) {
		this.eceName = eceName;
	}
	public String getIecClassification() {
		return iecClassification;
	}
	public void setIecClassification(String iecClassification) {
		this.iecClassification = iecClassification;
	}
	public String getNameOfDedicatedQuantity() {
		return nameOfDedicatedQuantity;
	}
	public void setNameOfDedicatedQuantity(String nameOfDedicatedQuantity) {
		this.nameOfDedicatedQuantity = nameOfDedicatedQuantity;
	}
	public String getNistName() {
		return nistName;
	}
	public void setNistName(String nistName) {
		this.nistName = nistName;
	}
	public String getSiName() {
		return siName;
	}
	public void setSiName(String siName) {
		this.siName = siName;
	}
	public String getSiNotation() {
		return siNotation;
	}
	public void setSiNotation(String siNotation) {
		this.siNotation = siNotation;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getStructuredNaming() {
		return structuredNaming;
	}
	public void setStructuredNaming(String structuredNaming) {
		this.structuredNaming = structuredNaming;
	}

}
