package at.srfg.iasset.repository.connectivity.rest;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;



public class ServiceFactory {
	private static final String URL_TEMPLATE = "https://%s:%s/%s";
	private static final String APPLICATION_WADL = "/application.wadl";

	public static <T> T getService(String serviceContext, Class<T> clazz) throws Exception {
		String host = "localhost";
		int port = 8181;
		// check with a non-authenticated client whether the service is available
		Client aClient = ClientFactory.getInstance().getClient();
		// check for the application.wadl resource - if not present
		WebTarget target = aClient.target(String.format(URL_TEMPLATE, host, port, serviceContext)+APPLICATION_WADL);
		try {
			// check whether the service is available
			Response resp = target.request().get();
			if ( resp.getStatus() == Status.NOT_FOUND.getStatusCode()) {
				throw new Exception("Service not available");
			}
		} catch ( ProcessingException e ) {
			throw new Exception("Service not available");
			
		}
		// obtain a client with authentication ...
		return ConsumerFactory.createConsumer(
				// construct the URL
				String.format(URL_TEMPLATE, host, port, serviceContext),
				// the Client
				ClientFactory.getInstance().getClient(), 
				// the interface class
				clazz);
 

	}
}
