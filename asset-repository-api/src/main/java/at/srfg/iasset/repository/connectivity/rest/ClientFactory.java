package at.srfg.iasset.repository.connectivity.rest;

import org.eclipse.digitaltwin.aas4j.v3.model.Operation;
import org.eclipse.digitaltwin.aas4j.v3.model.Property;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.repository.config.AASJacksonMapperProvider;
import at.srfg.iasset.repository.model.custom.InstanceOperation;
import at.srfg.iasset.repository.model.custom.InstanceProperty;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.ext.ContextResolver;
/**
 * Class creating a 
 * @author dglachs
 *
 */
public class ClientFactory {
	private static ClientFactory instance;
	
	
	
	private final AASJacksonMapperProvider provider;
//
	private ClientFactory() {
		this.provider = new AASJacksonMapperProvider();
		// keep custom instances
		this.provider.useImplementation(Property.class, InstanceProperty.class);
		this.provider.useImplementation(Operation.class, InstanceOperation.class);
	}
	public static ObjectMapper getObjectMapper() {
		return getInstance().getMapper();

	}
	private ObjectMapper getMapper() {
		return this.provider.getMapper();
	}

	public static ContextResolver<ObjectMapper> getContextResolver() {
		return getInstance().provider;
	}
	public static <T> void useImplementation(Class<T> aasInterface, Class<? extends T> implementation) {
		getInstance().provider.useImplementation(aasInterface, implementation);
	}
	public static ClientFactory getInstance() {
		if ( instance == null ) {
			instance = new ClientFactory();
		}
		return instance;
		
	}
	public Client getClient() {
		return getClient(null);
	}
	public Client getClient(HttpAuthenticationFeature auth) { 
//		String host = RESTConnector.getDefault().getHost();
//		
//		KeyStore keyStore = loadKeystore(host);
		// 
		ClientConfig config = new ClientConfig();
		if ( auth != null ) {
			config.register(auth);
		}
//		// register the objectmapper
		config.register(provider);
		
		return JerseyClientBuilder.newBuilder()
				.withConfig(config)
				// optionally add truststore 
//				.trustStore(keyStore)
				.build();
	}}
