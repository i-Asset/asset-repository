package at.srfg.iasset.semantic.eclass.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the property database table.
 * 
 */
@Entity
@Table(name="eclass_property")
@NamedQuery(name="PropertyDefinition.findAll", query="SELECT p FROM PropertyDefinition p")
public class PropertyDefinition implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="irdipr", nullable=false)
	private String irdiPR;
	@Column(name="attributetype")
	private String attributeType;
	@Column(name="category")
	private String category;
	@Column(name="currencyalphacode")
	private String currencyAlphaCode;
	@Column(name="datatype")
	private String dataType;
	@Column(name="definition")
	private String definition;
	@Column(name="definitionclass")
	private String definitionClass;
	@Column(name="identifier")
	private String identifier;
	@Column(name="idpr")
	private String idPR;
	@Column(name="isocountrycode")
	private String isoCountryCode;
	@Column(name="isolanguagecode")
	private String isoLanguageCode;
	@Column(name="note")
	private String note;
	@Column(name="preferredname")
	private String preferredName;
	@Column(name="preferredsymbol")
	private String preferredSymbol;
	@Column(name="remark")
	private String remark;
	@Column(name="revisionnumber")
	private String revisionNumber;
	@Column(name="shortname")
	private String shortName;
	@Column(name="sourceofdefinition")
	private String sourceOfDefinition;
	@Column(name="supplier")
	private String supplier;
	@Column(name="versiondate")
	private LocalDate versionDate;
	@Column(name="versionnumber")
	private String versionNumber;


	//bi-directional many-to-one association to Unit
	@ManyToOne
	@JoinColumn(name="irdiun")
	private PropertyUnit unit;

	//bi-directional many-to-many association to EclassValue
	@ManyToMany
	@JoinTable(
		name="eclass_property_value"
		, joinColumns={
			@JoinColumn(name="irdipr")
			}
		, inverseJoinColumns={
			@JoinColumn(name="irdiva")
			}
		)
	private List<PropertyValue> values;

	public PropertyDefinition() {
	}

	/**
	 * @return the irdiPR
	 */
	public String getIrdiPR() {
		return irdiPR;
	}

	/**
	 * @param irdiPR the irdiPR to set
	 */
	public void setIrdiPR(String irdiPR) {
		this.irdiPR = irdiPR;
	}

	/**
	 * @return the attributeType
	 */
	public String getAttributeType() {
		return attributeType;
	}

	/**
	 * @param attributeType the attributeType to set
	 */
	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the currencyAlphaCode
	 */
	public String getCurrencyAlphaCode() {
		return currencyAlphaCode;
	}

	/**
	 * @param currencyAlphaCode the currencyAlphaCode to set
	 */
	public void setCurrencyAlphaCode(String currencyAlphaCode) {
		this.currencyAlphaCode = currencyAlphaCode;
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
	 * @return the definitionClass
	 */
	public String getDefinitionClass() {
		return definitionClass;
	}

	/**
	 * @param definitionClass the definitionClass to set
	 */
	public void setDefinitionClass(String definitionClass) {
		this.definitionClass = definitionClass;
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
	 * @return the idPR
	 */
	public String getIdPR() {
		return idPR;
	}

	/**
	 * @param idPR the idPR to set
	 */
	public void setIdPR(String idPR) {
		this.idPR = idPR;
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
	 * @return the preferredSymbol
	 */
	public String getPreferredSymbol() {
		return preferredSymbol;
	}

	/**
	 * @param preferredSymbol the preferredSymbol to set
	 */
	public void setPreferredSymbol(String preferredSymbol) {
		this.preferredSymbol = preferredSymbol;
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
	 * @return the sourceOfDefinition
	 */
	public String getSourceOfDefinition() {
		return sourceOfDefinition;
	}

	/**
	 * @param sourceOfDefinition the sourceOfDefinition to set
	 */
	public void setSourceOfDefinition(String sourceOfDefinition) {
		this.sourceOfDefinition = sourceOfDefinition;
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

	/**
	 * @return the unit
	 */
	public PropertyUnit getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(PropertyUnit unit) {
		this.unit = unit;
	}

	/**
	 * @return the values
	 */
	public List<PropertyValue> getValues() {
		return values;
	}

	/**
	 * @param values the values to set
	 */
	public void setValues(List<PropertyValue> values) {
		this.values = values;
	}

}