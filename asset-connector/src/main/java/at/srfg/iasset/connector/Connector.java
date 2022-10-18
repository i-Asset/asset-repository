package at.srfg.iasset.connector;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import at.srfg.iasset.connector.component.impl.HttpComponent;
import at.srfg.iasset.connector.environment.LocalEnvironment;
import at.srfg.iasset.connector.environment.LocalServiceEnvironment;
import at.srfg.iasset.connector.environment.ModelListener;

public class Connector implements LocalEnvironment {
	

	/**
	 * The URL to the Server
	 */
	private final URI repositoryURL; 
	/**
	 * The local service port where the endpoint is created
	 */
	private int localServicePort = 5050;
	
	private HttpComponent endpoint;
	
	private LocalServiceEnvironment serviceEnvironment = new LocalServiceEnvironment();
	
	public Connector(URI repositoryURL) {
		
		this.repositoryURL = repositoryURL;
	}
	/**
	 * Enable the communication endpoints
	 * @return
	 */
	public Connector start() {
		return start(localServicePort);
	}
	public Connector start(int port) {
		if ( this.endpoint == null ) {
			endpoint = new HttpComponent(repositoryURL, serviceEnvironment);
		}
		if ( ! endpoint.isStarted()) {
			endpoint.start();
		}
		return this;
		
	}
	
	public void stop() {
		if ( endpoint.isStarted()) {
			endpoint.stop();
		}
	}
	
	public static void main(String [] args) {
		try {
			
			Connector connector = new Connector( new URI("http://localhost:8080/"));
			connector.start();
			connector.aliasForShell("test", "https://acplt.org/Test_AssetAdministrationShell");
			connector.setValueConsumer(
					"https://acplt.org/Test_AssetAdministrationShell", 
					"https://acplt.org/Test_Submodel", 
					"ExampleSubmodelCollectionOrdered.ExampleDecimalProperty", 
					new Consumer<String>() {

						@Override
						public void accept(String t) {
							// TODO Auto-generated method stub
							
						}
					});
			connector.setValueSupplier(
					"https://acplt.org/Test_AssetAdministrationShell", 
					"https://acplt.org/Test_Submodel", 
					"ExampleSubmodelCollectionOrdered.ExampleDecimalProperty", 
					new Supplier<String>() {

						@Override
						public String get() {
							return LocalDateTime.now().toString();
						}


					});
			connector.register("https://acplt.org/Test_AssetAdministrationShell");
			System.in.read();
			connector.stop();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void aliasForShell(String alias, String aasIdentifier) {
		
		endpoint.addShellHandler(alias, aasIdentifier);
	}
	public void register(String aasIdentifier) {
		endpoint.register(aasIdentifier);
	}
	public void unregister(String aasIdentifier) {
		endpoint.unregister(aasIdentifier);
	}
	public void addModelListener(ModelListener listener) {
		((LocalEnvironment)serviceEnvironment).addModelListener(listener);
	}
	public void removeModelListener(ModelListener listener) {
		((LocalEnvironment)serviceEnvironment).removeModelListener(listener);
	}
	@Override
	public void setValueConsumer(String aasIdentifier, String submodelIdentifier, String path, Consumer<String> consumer) {
		((LocalEnvironment)serviceEnvironment).setValueConsumer(aasIdentifier, submodelIdentifier, path, consumer);
		
	}
	@Override
	public void setValueSupplier(String aasIdentifier, String submodelIdentifier, String path, Supplier<String> consumer) {
		((LocalEnvironment)serviceEnvironment).setValueSupplier(aasIdentifier, submodelIdentifier, path, consumer);

		
	}
	@Override
	public void setOperationFunction(String aasIdentifier, String submodelIdentifier, String path,
			Function<Map<String, Object>, Object> function) {
		((LocalEnvironment)serviceEnvironment).setOperationFunction(aasIdentifier, submodelIdentifier, path, function);		
	}


}
