package at.srfg.iasset.semantic.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlTransient;
@Entity
@Table(name="concept_class")
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name="identifier")
public class ConceptClass extends ConceptBase {
	/**
	 * Serializable
	 */
	private static final long serialVersionUID = 1L;
	@Column(name="level")
	private int level;
	
	@Column(name="coded_name", length = 50)
	private String codedName;
	
	@Column(name="category", length=50)
	private String category;
//	@JsonIgnore
	@XmlTransient
	@ManyToOne(cascade = {CascadeType.REFRESH})
	@JoinColumn(name="parent_id", referencedColumnName = "identifier")
	private ConceptClass parentElement;
	@JsonIgnore
	@XmlTransient
	@OneToMany(mappedBy = "parentElement", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
	private List<ConceptClass> childElements;
	@JsonIgnore
	@XmlTransient
	@OneToMany(mappedBy = "conceptClass", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
	private List<ConceptClassProperty> classProperty;

	public ConceptClass() {
		// default
	}
	public ConceptClass(String id) {
		super(id);
	}
	public ConceptClass(ConceptClass parent, String id) {
		this(id);
		setParentElement(parent);
	}

	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getCodedName() {
		return codedName;
	}
	public void setCodedName(String codedName) {
		this.codedName = codedName;
	}


	public ConceptClass getParentElement() {
		return parentElement;
	}


	public void setParentElement(ConceptClass parentElement) {
		this.parentElement = parentElement;
	}


	public List<ConceptClass> getChildElements() {
		if ( childElements == null ) {
			childElements = new ArrayList<>();
		}
		return childElements;
	}


	public void setChildElements(List<ConceptClass> childElements) {
		this.childElements = childElements;
	}
	
	/**
	 * Helper method to maintain the 
	 * @param child
	 */
	protected void addChild(ConceptClass child) {
		// keep track of the parent/child relationship
		child.setParentElement(this);
		// maintain the childElements collection
		if (this.childElements == null) {
			this.childElements = new ArrayList<ConceptClass>();
		}
		this.childElements.add(child);
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


	// add the keywords ... 
}
