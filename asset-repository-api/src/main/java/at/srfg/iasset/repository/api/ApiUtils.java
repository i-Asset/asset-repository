package at.srfg.iasset.repository.api;

import java.util.Base64;

public class ApiUtils {

	public static String base64Decode(String input) {
		return new String(Base64.getDecoder().decode(input));
	}
	

}
