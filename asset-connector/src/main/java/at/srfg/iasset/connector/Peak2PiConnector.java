package at.srfg.iasset.connector;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Peak2PiConnector {
private String host;
	
	public Supplier<String> testsupplier;
	
	public Peak2PiConnector(String host) {
		this.host = host;
		testsupplier = () -> this.OEE("oee");
	}
	
	
	
	
	public String OEE(String subItem) {
		String result;
		Map<String,Object> ooeResult = this.getApiPath("/api/oee");
		System.out.println("read " + subItem);
		if(ooeResult.containsKey(subItem)) {
			result = ooeResult.get(subItem).toString();
			return result;
		}
		
		return null;
		
	}
	
	public String Quantity(String subItem) {
		String result;
		Map<String,Object> ooeResult = this.getApiPath("/api/oee");
		System.out.println("read " + subItem);
		if(ooeResult.containsKey(subItem)) {
			result = ooeResult.get(subItem).toString();
			return result;
		}
		
		return null;
		
	}
	
	public void SetProduct(String product) {
		
		HashMap<String, String> values = new HashMap<String, String>() {{
            put("product", product);
        }};

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
		try {
			requestBody = objectMapper
			        .writeValueAsString(values);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		String url = this.host + "/api/product";
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.version(Version.HTTP_1_1)
				.header("Content-Type", "application/json")
		      .uri(URI.create(url))
		      .POST(BodyPublishers.ofString(requestBody))
		      .build();
		try {
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			System.out.println(response.body());
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String GetProductByKey(String key) {
		String result;
		Map<String,Object> ooeResult = this.getApiPath("/api/product");
		if(ooeResult.containsKey(key)) {
			result = ooeResult.get(key).toString();
			return result;
		}
		
		return "";
	}
	
	public String GetProduct() {
		String result;
		Map<String,Object> ooeResult = this.getApiPath("/api/product");
		if(ooeResult.containsKey("prodId")) {
			result = ooeResult.get("prodId").toString();
			return result;
		}
		
		return "";
	}
	
	private String request(String suburl) {
		String url = this.host + suburl;
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.version(Version.HTTP_1_1)
		      .uri(URI.create(url))
		      .build();
		try {
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			
			return response.body();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private Map<String,Object> getApiPath(String suburl) {
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> result;
		try {
			result = mapper.readValue(this.request(suburl), HashMap.class);
			System.out.println(result);
			return result;
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		     
}
}
