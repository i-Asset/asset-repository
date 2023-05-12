package at.srfg.iasset.semantic.eclass.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * The primary key class for the classification_class_property database table.
 * 
 */
@Embeddable
public class ClassificationClassPropertyPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="irdicc", insertable=false, updatable=false)
	private String irdiCC;

	@Column(name="irdipr", insertable=false, updatable=false)
	private String irdiPR;

	public ClassificationClassPropertyPK() {
	}
	public String getIrdiCC() {
		return this.irdiCC;
	}
	public void setIrdiCC(String irdicc) {
		this.irdiCC = irdicc;
	}
	public String getIrdiPR() {
		return this.irdiPR;
	}
	public void setIrdiPR(String irdipr) {
		this.irdiPR = irdipr;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ClassificationClassPropertyPK)) {
			return false;
		}
		ClassificationClassPropertyPK castOther = (ClassificationClassPropertyPK)other;
		return 
			this.irdiCC.equals(castOther.irdiCC)
			&& this.irdiPR.equals(castOther.irdiPR);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.irdiCC.hashCode();
		hash = hash * prime + this.irdiPR.hashCode();
		
		return hash;
	}
}