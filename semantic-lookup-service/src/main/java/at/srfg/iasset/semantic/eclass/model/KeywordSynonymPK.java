package at.srfg.iasset.semantic.eclass.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * The primary key class for the keyword_synonym database table.
 * 
 */
@Embeddable
public class KeywordSynonymPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	@Column(name="irditarget")
	private String irdiTarget;
	@Column(name="irdikwirdisy")
	private String irdikwirdisy;

	public KeywordSynonymPK() {
	}
	public String getIrdiTarget() {
		return this.irdiTarget;
	}
	public void setIrdiTarget(String irditarget) {
		this.irdiTarget = irditarget;
	}
	public String getIrdikwirdisy() {
		return this.irdikwirdisy;
	}
	public void setIrdikwirdisy(String irdikwirdisy) {
		this.irdikwirdisy = irdikwirdisy;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof KeywordSynonymPK)) {
			return false;
		}
		KeywordSynonymPK castOther = (KeywordSynonymPK)other;
		return 
			this.irdiTarget.equals(castOther.irdiTarget)
			&& this.irdikwirdisy.equals(castOther.irdikwirdisy);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.irdiTarget.hashCode();
		hash = hash * prime + this.irdikwirdisy.hashCode();
		
		return hash;
	}
}