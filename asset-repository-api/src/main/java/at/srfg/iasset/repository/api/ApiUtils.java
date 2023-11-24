package at.srfg.iasset.repository.api;

import java.util.Base64;

public class ApiUtils {

	public static String base64Decode(String input) {
		return new String(Base64.getDecoder().decode(input));
	}
	public static String base64Encode(String decode) {
		return new String(Base64.getEncoder().encode(decode.getBytes()));
	}

}
