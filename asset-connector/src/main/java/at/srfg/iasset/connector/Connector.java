package at.srfg.iasset.connector;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.aas4j.v3.model.EventPayload;
import org.eclipse.aas4j.v3.model.Reference;

import at.srfg.iasset.connector.component.ConnectorEndpoint;
import at.srfg.iasset.connector.component.impl.AASFull;
import at.srfg.iasset.connector.environment.LocalEnvironment;
import at.srfg.iasset.connector.environment.LocalServiceEnvironment;
import at.srfg.iasset.repository.component.ModelListener;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.event.EventHandler;
import at.srfg.iasset.repository.event.EventProcessor;
import at.srfg.iasset.repository.event.EventProducer;

public class Connector implements LocalEnvironment {
	
	private String currentStringValue = "123.5";
	private LocalServiceEnvironment serviceEnvironment;
	
	public Connector(URI repositoryURL) {
		this.serviceEnvironment = new LocalServiceEnvironment(repositoryURL);
	}

	
	public void stop() {
		serviceEnvironment.shutdownEndpoint();
	}
	
	public ServiceEnvironment getServiceEnvironment() {
		return serviceEnvironment;
		
	}

	public static void main(String [] args) {
		try {
			Connector connector = new Connector( new URI("http://localhost:8081/"));
			// start the http endpoint for this Connector at port 5050
			connector.startEndpoint(5050);
			// create 
			connector.addHandler("https://acplt.org/Test_AssetAdministrationShell", "test");
			connector.setValueConsumer(
					"https://acplt.org/Test_AssetAdministrationShell", 
					"https://acplt.org/Test_Submodel", 
					"ExampleSubmodelCollectionOrdered.ExampleDecimalProperty", 
					new Consumer<String>() {

						@Override
						public void accept(final String t) {
							System.out.println("New Value provided: " + t);
							connector.currentStringValue = t;
							
						}
					});
			connector.setValueSupplier(
					"https://acplt.org/Test_AssetAdministrationShell", 
					"https://acplt.org/Test_Submodel", 
					"ExampleSubmodelCollectionOrdered.ExampleDecimalProperty", 
					new Supplier<String>() {

						@Override
						public String get() {
							return connector.currentStringValue;
						}


					});
			connector.register("https://acplt.org/Test_AssetAdministrationShell");
			// 
			connector.register(AASFull.AAS_BELT_INSTANCE.getId());
			/*
			 * The event processor should 
			 */
			connector.getEventProcessor().registerHandler(
					"https://acplt.org/Test_AssetAdministrationShell_Missing", 
					"https://acplt.org/Test_Submodel_Missing", 
					"http://acplt.org/Events/ExampleBasicEvent", 
					new EventHandler<String>() {

				@Override
				public void onEventMessage(EventPayload eventPayload, String payload) {
					System.out.println(payload);
					
				}

				@Override
				public Class<String> getPayloadType() {
					return String.class;
				}
			});
			connector.getEventProcessor().sendTestEvent("topic","http://acplt.org/Events/ExampleBasicEvent",  "This is a test payload");
			
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
	
	public void register(String aasIdentifier) {
		serviceEnvironment.register(aasIdentifier);
	}
	public void unregister(String aasIdentifier) {
		serviceEnvironment.unregister(aasIdentifier);
	}
	public void addModelListener(ModelListener listener) {
		serviceEnvironment.addModelListener(listener);
	}
	public void removeModelListener(ModelListener listener) {
		serviceEnvironment.removeModelListener(listener);
	}
	@Override
	public void setValueConsumer(String aasIdentifier, String submodelIdentifier, String path, Consumer<String> consumer) {
		serviceEnvironment.setValueConsumer(aasIdentifier, submodelIdentifier, path, consumer);
		
	}
	@Override
	public void setValueSupplier(String aasIdentifier, String submodelIdentifier, String path, Supplier<String> consumer) {
		serviceEnvironment.setValueSupplier(aasIdentifier, submodelIdentifier, path, consumer);
	}
	@Override
	public void setOperationFunction(String aasIdentifier, String submodelIdentifier, String path,
			Function<Object, Object> function) {
		serviceEnvironment.setOperationFunction(aasIdentifier, submodelIdentifier, path, function);		
	}
	@Override
	public ConnectorEndpoint startEndpoint(int port) {
		return serviceEnvironment.startEndpoint(port);
	}
	@Override
	public EventProcessor getEventProcessor() {
		return serviceEnvironment.getEventProcessor();
	}
	@Override
	public void shutdownEndpoint() {
		serviceEnvironment.shutdownEndpoint();
	}
	@Override
	public void addHandler(String aasIdentifier) {
		serviceEnvironment.addHandler(aasIdentifier);
		
	}
	@Override
	public void addHandler(String aasIdentifier, String alias) {
		serviceEnvironment.addHandler(aasIdentifier, alias);

		
	}
	@Override
	public void removeHandler(String alias) {
		serviceEnvironment.removeHandler(alias);
		
	}


	@Override
	public <T> void addMesssageListener(Reference reference, EventHandler<T> listener) {
		// TODO Auto-generated method stub
		serviceEnvironment.getEventProcessor().registerHandler(currentStringValue, currentStringValue, reference, listener);
		
	}


	@Override
	public <T> EventProducer<T> getMessageProducer(Reference reference, Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Object executeOperaton(String aasIdentifier, String submodelIdentifier, String path, Object parameter) {
		return serviceEnvironment.invokeOperation(aasIdentifier, submodelIdentifier, path, parameter);
	}


}
