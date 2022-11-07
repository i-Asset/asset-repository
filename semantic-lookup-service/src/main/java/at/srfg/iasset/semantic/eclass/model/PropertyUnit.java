package at.srfg.iasset.semantic.eclass.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the unit database table.
 * 
 */
@Entity
@Table(name="eclass_unit")
@NamedQuery(name="PropertyUnit.findAll", query="SELECT u FROM PropertyUnit u")
public class PropertyUnit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="irdiun")
	private String irdiUN;
	@Column(name="comment")
	private String comment;
	@Column(name="definiton")
	private String definition;
	@Column(name="dinnotation")
	private String dinNotation;
	@Column(name="ececode")
	private String eceCode;
	@Column(name="ecename")
	private String eceName;
	@Column(name="iecclassification")
	private String iecClassification;
	@Column(name="nameofdedicatedquantity")
	private String nameOfDedicatedQuantity;
	@Column(name="nistname")
	private String nistName;
	@Column(name="shortname")
	private String shortName;
	@Column(name="siname")
	private String siName;
	@Column(name="sinotation")
	private String siNotation;
	@Column(name="source")
	private String source;
	@Column(name="structurednaming")
	private String structuredNaming;


	public PropertyUnit() {
	}


	/**
	 * @return the irdiUN
	 */
	public String getIrdiUN() {
		return irdiUN;
	}


	/**
	 * @param irdiUN the irdiUN to set
	 */
	public void setIrdiUN(String irdiUN) {
		this.irdiUN = irdiUN;
	}


	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}


	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}


	/**
	 * @return the definition
	 */
	public String getDefinition() {
		return definition;
	}


	/**
	 * @param definition the definition to set
	 */
	public void setDefinition(String definition) {
		this.definition = definition;
	}


	/**
	 * @return the dinNotation
	 */
	public String getDinNotation() {
		return dinNotation;
	}


	/**
	 * @param dinNotation the dinNotation to set
	 */
	public void setDinNotation(String dinNotation) {
		this.dinNotation = dinNotation;
	}


	/**
	 * @return the eceCode
	 */
	public String getEceCode() {
		return eceCode;
	}


	/**
	 * @param eceCode the eceCode to set
	 */
	public void setEceCode(String eceCode) {
		this.eceCode = eceCode;
	}


	/**
	 * @return the eceName
	 */
	public String getEceName() {
		return eceName;
	}


	/**
	 * @param eceName the eceName to set
	 */
	public void setEceName(String eceName) {
		this.eceName = eceName;
	}


	/**
	 * @return the iecClassification
	 */
	public String getIecClassification() {
		return iecClassification;
	}


	/**
	 * @param iecClassification the iecClassification to set
	 */
	public void setIecClassification(String iecClassification) {
		this.iecClassification = iecClassification;
	}


	/**
	 * @return the nameOfDedicatedQuantity
	 */
	public String getNameOfDedicatedQuantity() {
		return nameOfDedicatedQuantity;
	}


	/**
	 * @param nameOfDedicatedQuantity the nameOfDedicatedQuantity to set
	 */
	public void setNameOfDedicatedQuantity(String nameOfDedicatedQuantity) {
		this.nameOfDedicatedQuantity = nameOfDedicatedQuantity;
	}


	/**
	 * @return the nistName
	 */
	public String getNistName() {
		return nistName;
	}


	/**
	 * @param nistName the nistName to set
	 */
	public void setNistName(String nistName) {
		this.nistName = nistName;
	}


	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}


	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}


	/**
	 * @return the siName
	 */
	public String getSiName() {
		return siName;
	}


	/**
	 * @param siName the siName to set
	 */
	public void setSiName(String siName) {
		this.siName = siName;
	}


	/**
	 * @return the siNotation
	 */
	public String getSiNotation() {
		return siNotation;
	}


	/**
	 * @param siNotation the siNotation to set
	 */
	public void setSiNotation(String siNotation) {
		this.siNotation = siNotation;
	}


	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}


	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}


	/**
	 * @return the structuredNaming
	 */
	public String getStructuredNaming() {
		return structuredNaming;
	}


	/**
	 * @param structuredNaming the structuredNaming to set
	 */
	public void setStructuredNaming(String structuredNaming) {
		this.structuredNaming = structuredNaming;
	}



}