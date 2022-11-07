package at.srfg.iasset.semantic.eclass.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the keyword_synonym database table.
 * 
 */
@Entity
@Table(name="eclass_keyword_synonym")
@NamedQuery(name="KeywordSynonym.findAll", query="SELECT k FROM KeywordSynonym k")
public class KeywordSynonym implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private KeywordSynonymPK id;
	@Column(name="explanation")
	private String explanation;
	@Column(name="idccidpr")
	private String irdi;
	@Column(name="identifier")
	private String identifier;
	@Column(name="isocountrycode")
	private String isoCountryCode;
	@Column(name="isolanguagecode")
	private String isoLanguageCode;
	@Column(name="keywordvaluesynonymvalue")
	private String value;
	@Column(name="supplierkwsuppliersy")
	private String supplier;
	@Column(name="typeofse")
	private String typeOfSE;
	@Column(name="typeoftargetse")
	private String typeOfTargetSE;
	@Column(name="versionnumber")
	private String versionNumber;

	public KeywordSynonym() {
	}

	/**
	 * @return the id
	 */
	public KeywordSynonymPK getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(KeywordSynonymPK id) {
		this.id = id;
	}

	/**
	 * @return the explanation
	 */
	public String getExplanation() {
		return explanation;
	}

	/**
	 * @param explanation the explanation to set
	 */
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	/**
	 * @return the irdi
	 */
	public String getIrdi() {
		return irdi;
	}

	/**
	 * @param irdi the irdi to set
	 */
	public void setIrdi(String irdi) {
		this.irdi = irdi;
	}

	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the isoCountryCode
	 */
	public String getIsoCountryCode() {
		return isoCountryCode;
	}

	/**
	 * @param isoCountryCode the isoCountryCode to set
	 */
	public void setIsoCountryCode(String isoCountryCode) {
		this.isoCountryCode = isoCountryCode;
	}

	/**
	 * @return the isoLanguageCode
	 */
	public String getIsoLanguageCode() {
		return isoLanguageCode;
	}

	/**
	 * @param isoLanguageCode the isoLanguageCode to set
	 */
	public void setIsoLanguageCode(String isoLanguageCode) {
		this.isoLanguageCode = isoLanguageCode;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the supplier
	 */
	public String getSupplier() {
		return supplier;
	}

	/**
	 * @param supplier the supplier to set
	 */
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	/**
	 * @return the typeOfSE
	 */
	public String getTypeOfSE() {
		return typeOfSE;
	}

	/**
	 * @param typeOfSE the typeOfSE to set
	 */
	public void setTypeOfSE(String typeOfSE) {
		this.typeOfSE = typeOfSE;
	}

	/**
	 * @return the typeOfTargetSE
	 */
	public String getTypeOfTargetSE() {
		return typeOfTargetSE;
	}

	/**
	 * @param typeOfTargetSE the typeOfTargetSE to set
	 */
	public void setTypeOfTargetSE(String typeOfTargetSE) {
		this.typeOfTargetSE = typeOfTargetSE;
	}

	/**
	 * @return the versionNumber
	 */
	public String getVersionNumber() {
		return versionNumber;
	}

	/**
	 * @param versionNumber the versionNumber to set
	 */
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

}