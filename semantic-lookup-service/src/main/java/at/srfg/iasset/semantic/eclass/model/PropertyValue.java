package at.srfg.iasset.semantic.eclass.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * Persistent entity for the eclass values.
 * 
 * @author dglachs
 *
 */
@Entity
@Table(name = "eclass_value")
public class PropertyValue {
	
	@Column(name="supplier")
	private String supplier;
	@Column(name="idva")
	private String idVA;
	@Column(name="identifier")
	private String identifier;
	@Column(name="versionnumber")
	private String versionNumber;
	@Column(name="revisionnumber")
	private String revisionNumber;
	@Column(name="versiondate")
	private LocalDate versionDate;
	@Column(name="preferredname")
	private String preferredName;
	@Column(name="shortname")
	private String shortName;
	@Column(name="definition")
	private String definition;
	@Column(name="reference")
	private String reference;
	@Column(name="isolanguagecode")
	private String isoLanguage;
	@Column(name="isocountrycode")
	private String isoCountryCode;
	@Id
	@Column(name="irdiva")
	private String irdiVA;
	@Column(name="datatype")
	private String dataType;
	/**
	 * @return the irdiVA
	 */
	public String getIrdiVA() {
		return irdiVA;
	}
	/**
	 * @param irdiVA the irdiVA to set
	 */
	public void setIrdiVA(String irdiVA) {
		this.irdiVA = irdiVA;
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
	 * @return the idVA
	 */
	public String getIdVA() {
		return idVA;
	}
	/**
	 * @param idVA the idVA to set
	 */
	public void setIdVA(String idVA) {
		this.idVA = idVA;
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
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}
	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}
	/**
	 * @return the isoLanguage
	 */
	public String getIsoLanguage() {
		return isoLanguage;
	}
	/**
	 * @param isoLanguage the isoLanguage to set
	 */
	public void setIsoLanguage(String isoLanguage) {
		this.isoLanguage = isoLanguage;
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
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}
	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
}
