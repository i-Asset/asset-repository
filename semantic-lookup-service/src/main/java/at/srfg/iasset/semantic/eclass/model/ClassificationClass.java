package at.srfg.iasset.semantic.eclass.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the classification_class database table.
 * 
 */
@Entity
@Table(name="eclass_classification_class")
@NamedQuery(name="ClassificationClass.findAll", query="SELECT c FROM ClassificationClass c")
public class ClassificationClass implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="irdicc", nullable = false)
	private String irdiCC;
	@Column(name = "codedname")
	private String codedName;
	@Column(name="definition")
	private String definition;
	@Column(name="idcc")
	private String idCC;
	@Column(name="identifier")
	private String identifier;
	@Column(name="isocountrycode")
	private String isoCountryCode;
	@Column(name="isolanguagecode")
	private String isoLanguageCode;
	@Column(name="level")
	private Integer level;
	@Column(name="mkkeyword")
	private boolean keywordPresent;
	@Column(name="mksubclass")
	private boolean subclassPresent;
	@Column(name="note")
	private String note;
	@Column(name="preferredname")
	private String preferredName;
	@Column(name="remark")
	private String remark;
	@Column(name="revisionnumber")
	private String revisionNumber;
	@Column(name="supplier")
	private String supplier;
	@Column(name="versiondate")
	private LocalDate versionDate;
	@Column(name="versionnumber")
	private String versionNumber;



	public ClassificationClass() {
	}



	/**
	 * @return the irdiCC
	 */
	public String getIrdiCC() {
		return irdiCC;
	}



	/**
	 * @param irdiCC the irdiCC to set
	 */
	public void setIrdiCC(String irdiCC) {
		this.irdiCC = irdiCC;
	}



	/**
	 * @return the codedName
	 */
	public String getCodedName() {
		return codedName;
	}



	/**
	 * @param codedName the codedName to set
	 */
	public void setCodedName(String codedName) {
		this.codedName = codedName;
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
	 * @return the idCC
	 */
	public String getIdCC() {
		return idCC;
	}



	/**
	 * @param idCC the idCC to set
	 */
	public void setIdCC(String idCC) {
		this.idCC = idCC;
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
	 * @return the level
	 */
	public Integer getLevel() {
		return level;
	}



	/**
	 * @param level the level to set
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}



	/**
	 * @return the keyworPresent
	 */
	public boolean isKeywordPresent() {
		return keywordPresent;
	}



	/**
	 * @param keyworPresent the keyworPresent to set
	 */
	public void setKeywordPresent(boolean keyworPresent) {
		this.keywordPresent = keyworPresent;
	}



	/**
	 * @return the subclassPresent
	 */
	public boolean isSubclassPresent() {
		return subclassPresent;
	}



	/**
	 * @param subclassPresent the subclassPresent to set
	 */
	public void setSubclassPresent(boolean subclassPresent) {
		this.subclassPresent = subclassPresent;
	}



	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}



	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}



	/**
	 * @return the preferredName
	 */
	public String getPreferredName() {
		return preferredName;
	}



	/**
	 * @param preferredName the preferredName to set
	 */
	public void setPreferredName(String preferredName) {
		this.preferredName = preferredName;
	}



	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}



	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}



	/**
	 * @return the revisionNumber
	 */
	public String getRevisionNumber() {
		return revisionNumber;
	}



	/**
	 * @param revisionNumber the revisionNumber to set
	 */
	public void setRevisionNumber(String revisionNumber) {
		this.revisionNumber = revisionNumber;
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
	 * @return the versionDate
	 */
	public LocalDate getVersionDate() {
		return versionDate;
	}



	/**
	 * @param versionDate the versionDate to set
	 */
	public void setVersionDate(LocalDate versionDate) {
		this.versionDate = versionDate;
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