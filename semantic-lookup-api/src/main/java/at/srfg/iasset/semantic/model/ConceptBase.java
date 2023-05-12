package at.srfg.iasset.semantic.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import at.srfg.iasset.semantic.model.skos.ConceptLabel;
import at.srfg.iasset.semantic.model.skos.LabelAware;
import at.srfg.iasset.semantic.model.skos.SKOSLabel.LabelType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * The persistent class for the classification_class database table.
 * 
 */
@JsonInclude(value = Include.NON_EMPTY)
@JsonTypeInfo(
		property="conceptType",
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
	@Type(value=ConceptClass.class, name="ConceptClass"),
	@Type(value=ConceptProperty.class, name="ConceptProperty"),
	@Type(value=ConceptPropertyUnit.class, name="ConceptPropertyUnit"),
	@Type(value=ConceptPropertyValue.class, name="ConceptPropertyValue"),
})
@Entity
@Table(name="concept_base", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"concept_id"}),
		@UniqueConstraint(columnNames = {"name_space", "local_name"})
})
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="concept_type")

public abstract class ConceptBase implements Serializable, LabelAware<ConceptLabel> {
	public static enum ConceptType {
		ConceptClass,
		ConceptProperty,
		ConceptPropertyUnit,
		ConceptPropertyValue;
		
	}
	public static final String CONCEPT_CLASS = "ConceptClass";
	public static final String PROPERTY = "ConceptProperty";
	public static final String PROPERTY_UNIT = "ConceptPropertyUnit";
	public static final String PROPERTY_VALUE = "ConceptPropertyValue";
	private static final long serialVersionUID = 1L;
	/**
	 * The internal Primary Key for each {@link ConceptBase}
	 */
	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="identifier")
	private Long identifier;

	/**
	 * key information - id is either an IRDI, e.g. the combination of 'supplier#elementtype-localName#versionnumber' 
	 * where element type is (for eclass)
	 * <ul>
	 * <li>01 - class
	 * <li>02 - property
	 * <li>05 - unit
	 * <li>07 - value
	 * </ul>
	 * e.g. for eclass something like: <code>0173-1#01-AAA000#003</code>.
	 * or it is a valid URI, e.g. the combination of the namespace&localName, e.g. something like
	 * <code>https://namespace.org/identifier</code> 
	 */
//	@Id
	@Column(name="concept_id", nullable = false, length=100)
	private String conceptId;
	/**
	 * Type of the id element (either IRDI or URI)
	 */
	@Column(name="concept_id_type")
	private IdType idType;
	/**
	 * The supplier (for idtype IRDI)
	 */
	@Column(name="supplier", length=100)
	private String supplier;
	
	@Column(name="name_space", length=100)
	private String nameSpace;
	@Column(name="local_name", length=50)
	private String localName;
	@Column(name="version_number", length=3)
	private String versionNumber;
	@Column(name="revision_number", length=2)
	private String revisionNumber;
	@Column(name="version_date")
	private LocalDate versionDate;

	/**
	 * 
	 */
	@Column(name="short_name", length=50)
	private String shortName;
	/**
	 * Readonly attribute, the type of the element
	 * @see #getModelType()
	 */
	@Column(name="concept_type", insertable = false, updatable = false)
	private String conceptType;

	/**
	 * Notes belonging this element
	 */
	@Column(name="note", length=1023)
	private String note;
	@Column(name="remark", length=1023)
	private String remark;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "concept", fetch = FetchType.LAZY)
	private Collection<ConceptLabel> labels;
	
	public ConceptBase() {
	}
	
	protected ConceptBase(String id) {
		if ( id == null || id.length() == 0) {
//		if ( String.isNullOrEmpty(id)) {
			throw new IllegalArgumentException("Identifier must be provided!");
		}
		IdType idType = IdType.getType(id);
		switch (idType) {
		case IRDI:
			setSupplier(IdPart.Supplier.getFrom(id));
			setLocalName(String.format("%s-%s", IdPart.CodeSpaceIdentifier.getFrom(id),IdPart.ItemCode.getFrom(id)));
			setVersionNumber(IdPart.VersionIdentifier.getFrom(id));
			setNameSpace(String.format("urn:%s:", IdPart.Supplier.getFrom(id)));
			break;
		case IRI:
			setSupplier(IdPart.Domain.getFrom(id));
			setNameSpace(IdPart.Namespace.getFrom(id));
			setLocalName(IdPart.LocalName.getFrom(id));
			break;
		}
		setIdType(idType);
		setConceptId(id);
//		setId(id);
	}
	protected ConceptBase(String nameSpace, String localName) {
		this(nameSpace+localName);
	}

	@JsonIgnore
	public Long getId() {
		return identifier;
	}


	public void setId(Long id) {
		this.identifier = id;
	}


	public IdType getIdType() {
		return idType;
	}


	protected void setIdType(IdType idType) {
		this.idType = idType;
	}


	public String getSupplier() {
		return supplier;
	}


	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}


	public String getNameSpace() {
		return nameSpace;
	}


	public void setNameSpace(String namespace) {
		this.nameSpace = namespace;
	}


	public String getLocalName() {
		return localName;
	}


	public void setLocalName(String localName) {
		this.localName = localName;
	}


	public String getVersionNumber() {
		return versionNumber;
	}


	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}


	public String getRevisionNumber() {
		return revisionNumber;
	}


	public void setRevisionNumber(String revisionNumber) {
		this.revisionNumber = revisionNumber;
	}

	@JsonFormat(pattern = "yyyy-MM-dd")
	public LocalDate getVersionDate() {
		return versionDate;
	}


	public void setVersionDate(LocalDate versionDate) {
		this.versionDate = versionDate;
	}


	public String getShortName() {
		return shortName;
	}


	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	@JsonIgnore
	public ConceptType getBaseType() {
		return ConceptType.valueOf(conceptType != null ? conceptType : getClass().getSimpleName());
	}
	public String getConceptType() {
		return conceptType != null ? conceptType : getClass().getSimpleName();
	}
//
//	public Set<String> getLanguages() {
//		return getDescriptionMap().keySet();
//	}
//	public void setLanguages(Collection<String> languages) {
//		// languages are maintained from the description map
//	}
//
//	public String getPreferredName(String language) {
//		ConceptBaseDescription desc = getDescription(language);
//		if ( desc != null) {
//			return desc.getPreferredName();
//		}
//		return null;
//	}
//	public String getDefinition(String language) {
//		ConceptBaseDescription desc = getDescription(language);
//		if ( desc != null) {
//			return desc.getDefinition();
//		}
//		return null;
//	}


	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}
//	@JsonIgnore
//	public Map<String, ConceptBaseDescription> getDescriptionMap() {
//		return descriptionMap;
//	}
//	public void setDescriptionMap(Map<String, ConceptBaseDescription> descriptionMap) {
//		this.descriptionMap = descriptionMap;
//	}
//	/**
//	 * Helper method storing a language dependent description or
//	 * label for the element
//	 * @param language The ISO language code 
//	 * @param preferredName for the language
//	 * @param definition additional definition
//	 */
//	public void setDescription(String language, String preferredName, String definition) {
//		if ( language != null) {
//			ConceptBaseDescription desc = descriptionMap.get(language);
//			if ( desc != null ) {
//				desc.setPreferredName(preferredName);
//				desc.setDefinition(definition);
//			}
//			else {
//				if ( preferredName !=null) {
//					this.descriptionMap.put(language, new ConceptBaseDescription(this, language, preferredName, definition));
//				}
//			}
//		}
//	}
//	public void setPreferredName(String language, String name) {
//		if ( language != null) {
//			ConceptBaseDescription desc = descriptionMap.get(language);
//			if ( desc != null ) {
//				desc.setPreferredName(name);
//			}
//			else {
//				this.descriptionMap.put(language, new ConceptBaseDescription(this, language, name));
//			}
//		}
//	}
//	/**
//	 * Helper method providing access to the element's {@link ConceptBaseDescription} by language
//	 * @param language the ISO Language code, e.g. de, en 
//	 * @return the description
//	 */
//	public ConceptBaseDescription getDescription(String language) {
//		return this.descriptionMap.get(language);
//	}
//	/**
//	 * Getter for the description
//	 * @return
//	 */
//	public Collection<ConceptBaseDescription> getDescription() {
//		if (this.descriptionMap == null) {
//			return null;
//		}
//		return this.descriptionMap.values();
//	}
//	public void setDescription(Collection<ConceptBaseDescription> desc) {
//		if (this.descriptionMap == null) {
//			this.descriptionMap = new HashMap<>();
//		}
//		if ( desc!=null && !desc.isEmpty()) {
//			// add the provided descriptions
//			desc.stream().forEach(new Consumer<ConceptBaseDescription>() {
//
//				@Override
//				public void accept(ConceptBaseDescription t) {
//					setDescription(t.getLanguage(), t.getPreferredName(), t.getDefinition());
//					
//				}
//			});
//		}
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((conceptId == null) ? 0 : conceptId.hashCode());
		result = prime * result + ((conceptType == null) ? 0 : conceptType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConceptBase other = (ConceptBase) obj;
		if (conceptId == null) {
			if (other.conceptId != null)
				return false;
		} else if (!conceptId.equals(other.conceptId))
			return false;
		if (conceptType == null) {
			if (other.conceptType != null)
				return false;
		} else if (!conceptType.equals(other.conceptType))
			return false;
		return true;
	}

	public String getConceptId() {
		return conceptId;
	}

	public void setConceptId(String conceptId) {
		this.conceptId = conceptId;
	}

	@Override
	public Collection<ConceptLabel> getLabels() {
		if ( labels == null ) {
			labels = new ArrayList<ConceptLabel>();
		}
		return labels;
	}

	@Override
	public void setLabels(Collection<ConceptLabel> labels) {
		this.labels = labels;
		
	}

	@Override
	public ConceptLabel newLabel(Locale locale, LabelType labelType, String label) {
		return new ConceptLabel(this, locale, labelType, label);
	}


}