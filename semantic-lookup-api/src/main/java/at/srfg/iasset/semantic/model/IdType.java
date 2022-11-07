package at.srfg.iasset.semantic.model;
/**
 * 
 * enumeration for the (valid) identifier types.
 *
 */

import java.util.regex.Pattern;
/**
 * Enumeration defining the allowed identifier types. Each
 * type can be detected based on a regular expression.  
 * <p>
 * 
 * @author dglachs
 * @see https://www.regextester.com
 *
 */
public enum IdType {
	IRI("^((https?|ftp):\\/\\/)?([a-z0-9-]+(\\.[a-z0-9-]+)+)(:(\\d{4}))?(\\/)?(([a-z0-9-\\.]+[\\/#])+)?((.*))?$", // matches URL
		"^(urn:[a-z0-9][a-z0-9-]{0,31}):(\\/)?((\\w+([\\/#:]))+()?()?()?()+)?((.*))?$"),	   	 // matches URN
	
	// matches against URI or an URN - must start with the protocol! 
	// URI("^([a-z]+):(\\/\\/)?((((\\w+):?)+)|((((\\w+)\\.?)+)((:(\\d+))?)))((\\/\\w+)+)?(([\\/#])(\\w+)?(\\S+)?)?$"),	
	// Unquoted: ^(((\d+)[-\/](\w+))(([-\/](\w+)?)+)?)#(((\w{2})-)?(\w+))(#(\d+))?$
	// matches against a IRDI (eClass, IEC CDD)
	IRDI("^(((\\d+)[-\\/](\\w+))(([-\\/](\\w+)?)+)?)#(((\\w{2})-)?(\\w+))(#(\\d+))?$"),
	// Unquoted: ^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}$
	// UUID("^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}$"),
	// matches word characters: [a-zA-Z_0-9]
	// IdShort("^(\\w+)$"),
	// NO regex - use only as fallback 
	// Custom(""),
	;	
	private Pattern[] pattern;
	private IdType(String ...regex ) {
		this.pattern = new Pattern[regex.length];
		for (int i = 0; i< pattern.length;i++) {
			this.pattern[i] = Pattern.compile(regex[i]);
			
		}
	}
	protected Pattern[] pattern() {
		return pattern;
	}
	protected Pattern pattern(String s) {
		for (Pattern p : pattern) {
			if (p.asPredicate().test(s) ) {
				return p;
			}
		}
		return null;
	}
	protected boolean test(String s) {
		for (Pattern p : pattern) {
			if (p.asPredicate().test(s) ) {
				return true;
			}
		}
		return false;
	}

	public static IdType getType(String id) {
		for (IdType e : values()) {
			if ( e.test(id)) {
				return e;
			}
		}
		throw new IllegalArgumentException("Not a valid identifier, use IRDI or IRI!!!");
	}

}
