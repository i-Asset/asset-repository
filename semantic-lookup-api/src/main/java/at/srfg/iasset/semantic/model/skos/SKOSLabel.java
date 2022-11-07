package at.srfg.iasset.semantic.model.skos;

import java.util.Locale;

public interface SKOSLabel {
	/**
	 * Type of (SKOS) labels, one of
	 * <ul>
	 * <li>prefLabel
	 * <li>altLabel
	 * <li>hiddenLabel
	 * <li>definition
	 * <li>comment
	 * </ul>
	 * @author dglachs
	 *
	 */
	enum LabelType {
		prefLabel,
		altLabel,
		hiddenLabel,
		definition,
		comment,
		;
	}
	/**
	 * retrieve the type of the multilingual label
	 * @return
	 */
	public LabelType getLabelType();
	public void setLabelType(LabelType labelType);
	public void setLocale(Locale locale);
	public Locale getLocale();
	public void setLabel(String label);
	public String getLabel();
}
