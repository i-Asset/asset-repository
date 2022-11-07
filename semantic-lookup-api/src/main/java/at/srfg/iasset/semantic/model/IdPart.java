package at.srfg.iasset.semantic.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Enumeration storing the corresponding group-numbers for accessing parts of the 
 * parsed identifier.  
 * <p>
 * Use the respective {@link IdPart}'s getFrom method to obtain the contained part.
 * Example:
 * {@link LocalName#getFrom(id)} to obtain the localName from a URI.
 * </p>
 * This class relies on the regex patterns defined in {@link IdType#pattern()}. 
 * When the pattern changes, the group numbers will change too!!!
 * @author dglachs
 *
 */
public enum IdPart {
	
	// IRI - URL Related REGEX Groups
	LocalName(IdType.IRI, 11),
	Namespace(IdType.IRI, 0, IdPart.LocalName),
	Protocol(IdType.IRI, 2),
	Domain(IdType.IRI, 3),
	Port(IdType.IRI, 6),
	// 
	LegalEntity(IdType.IRI,1),
	// IRDI related REGEX groups
	/**
	 * Registration Authority Identifier (RAI)
	 * consisting of {@link IdPart#InternationalCodeDesignator}, 
	 * {@link IdPart#OrganizationIdentifier} and
	 * {@link IdPart#AdditionalInformation}.
	 * <p>
	 * The IRDI <b>0173-1#01-AFW236#002</b> resolves the
	 * supplier <code>0173-1</code>
	 * </p>
	 */
	Supplier(IdType.IRDI, 1),
	/**
	 * The international code designatoras part of the Registration
	 * Authority identifier. Examples are 0173 (eClass), 01
	 * <p>
	 * The IRDI <b>0173-1#01-AFW236#002</b> resolves the
	 * ICD <code>0173</code>. 
	 * </p>
	 * 
	 */
	InternationalCodeDesignator(IdType.IRDI, 3),
	/**
	 * The organization identifier as part of the Registration
	 * Authority identifier.
	 * <p>
	 * The IRDI <b>0173-1#01-AFW236#002</b> resolves the
	 * Organization Identifier <code>1</code>
	 * </p>
	 * 
	 */
	OrganizationIdentifier(IdType.IRDI,4),
	/**
	 * Optional additional information as part of the
	 * {@link IdPart#Supplier}
	 */
	AdditionalInformation(IdType.IRDI,5),
	/**
	 * Combination of {@link IdPart#CodeSpaceIdentifier} and 
	 * {@link IdPart#ItemCode}
	 */
	DataIdentifier(IdType.IRDI, 8),
	/**
	 * Code Space Identifier, such as 
	 * <ul>
	 * <li>01 - Class
	 * <li>02 - Property
	 * <li>05 - Unit of Measure
	 * <li>07 - Property Value
	 * </ul>
	 * @see {@link http://wiki.eclass.eu/wiki/IRDI}
	 */
	CodeSpaceIdentifier(IdType.IRDI, 10),
	/**
	 * 6-Digit Item Code
	 */
	ItemCode(IdType.IRDI, 11),
	VersionIdentifier(IdType.IRDI, 13)
	;
	private IdPart(IdType idType, int group) {
		this.idType = idType;
		this.groupNo = group;
		this.subtract = null;
	}
	private IdPart(IdType idType, int group, IdPart subtract) {
		this.idType = idType;
		this.groupNo = group;
		this.subtract = subtract;
	}
	private IdType idType;
	private IdPart subtract;
	private int groupNo;
	private int getNo() {
		return groupNo;
	}
	public String getFrom(String id) {
		Pattern p = idType.pattern(id) ;
		if ( p != null) {
			Matcher m = p.matcher(id);
			if ( m.find()) {
				return extractFrom(m);
			}
		}
//		for (Pattern p : idType.pattern()) {
//		}
		return null;
//		Matcher matcher = this.idType.pattern().matcher(id);
//		if ( matcher.find() ) {
//			return extractFrom(matcher);
//		}
//		return null;
	}
	private String extractFrom(Matcher matcher) {
		String part = matcher.group(getNo());
		if ( subtract != null) {
			return matcher.group().substring(0, matcher.start(subtract.getNo()));
//			String toSubtract = subtract.extractFrom(matcher);
//			if ( part.endsWith(toSubtract)) {
//				return part.substring(0, part.length()-toSubtract.length());
//			}
		}
		return part;
	}
}