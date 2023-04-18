package at.srfg.iasset.connector;

import java.io.FileInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.aas4j.v3.model.AssetAdministrationShell;
import org.eclipse.aas4j.v3.model.EventPayload;
import org.eclipse.aas4j.v3.model.KeyTypes;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.Submodel;

import at.srfg.iasset.connector.component.ConnectorEndpoint;
import at.srfg.iasset.connector.component.ConnectorMessaging;
import at.srfg.iasset.connector.component.event.Callback;
import at.srfg.iasset.connector.component.event.EventHandler;
import at.srfg.iasset.connector.component.event.EventProducer;
import at.srfg.iasset.connector.component.impl.AASFull;
import at.srfg.iasset.connector.environment.LocalEnvironment;
import at.srfg.iasset.connector.environment.LocalServiceEnvironment;
import at.srfg.iasset.repository.component.ModelListener;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.model.AASFaultSubmodel;
import at.srfg.iasset.repository.model.Fault;
import at.srfg.iasset.repository.utils.ReferenceUtils;

public class Connector implements LocalEnvironment {
	
	private String currentStringValue = "123.5";
	private LocalServiceEnvironment serviceEnvironment;
	
	public Connector(URI repositoryURL) {
		this.serviceEnvironment = new LocalServiceEnvironment(repositoryURL);
	}

	
	public void stop() {
		serviceEnvironment.shutdownEndpoint();
		serviceEnvironment.getEventProcessor().stopEventProcessing();
	}
	
	public ServiceEnvironment getServiceEnvironment() {
		return serviceEnvironment;
		
	}

	public static void main(String [] args) {
		
		try {
			String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			String connectorPath = rootPath + "connector.properties";
			
			Properties connectorProperties = new Properties();
			connectorProperties.load(new FileInputStream(connectorPath));
			
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
//			connector.setOperationFunction("id", "submodel", "path", new Function<Object, Object>() {
//
//				@Override
//				public Object apply(Object t) {
//					
//					return null;
//				}
//			});
			// sample for belt data
			// currently no write via AAS planned!
			
//			connector.setValueConsumer(
//					"http://iasset.salzburgresearch.at/labor/beltInstance", 
//					"http://iasset.salzburgresearch.at/labor/beltInstance/properties", 
//					"beltData.state", 
//					new Consumer<String>() {
//
//						@Override
//						public void accept(final String t) {
//							// replace with OPC-UA Write
//							System.out.println("New Value provided: " + t);
//							connector.currentStringValue = t;
//							
//						}
//					});
			
			// used to read OPC-UA values
			connector.setValueSupplier(
					"http://iasset.salzburgresearch.at/labor/beltInstance", 
					"http://iasset.salzburgresearch.at/labor/beltInstance/properties", 
					// path
					"beltData.state", 
					new Supplier<String>() {

						@Override
						public String get() {
							// replace with OPC-UA Read
							return connector.currentStringValue;
						}


					});
			connector.register("https://acplt.org/Test_AssetAdministrationShell");
			// 
			connector.register(AASFull.AAS_BELT_INSTANCE.getId());
			
			connector.registerEventHandler(					
					AASFaultSubmodel.SUBMODEL_FAULT1.getId(), 
					ReferenceUtils.asGlobalReference(KeyTypes.GLOBAL_REFERENCE, "http://iasset.salzburgresearch.at/semantic/fault"), 
					new EventHandler<Fault>() {
		
						@Override
						public void onEventMessage(EventPayload eventPayload, Fault payload) {
							System.out.println(payload.getFaultId() + " " + payload.getShortText()) ;
							
						}
		
						@Override
						public Class<Fault> getPayloadType() {
							return Fault.class;
						}});
			
			
		

			EventProducer<Fault> faultProducer = connector.getMessageProducer(
					AASFaultSubmodel.SUBMODEL_FAULT1.getId(), 
					ReferenceUtils.asGlobalReference(KeyTypes.GLOBAL_REFERENCE, "http://iasset.salzburgresearch.at/semantic/fault"), 
					Fault.class);
			
			Fault f = new Fault();
			f.setFaultId("12345");
			f.setAssetId("assetId");
			f.setSenderUserId("im am the user");
			f.setShortText("this is a short");
			faultProducer.sendEvent(f);
			faultProducer.sendEvent(f, new Callback<Fault>() {
				
				@Override
				public void deliveryComplete(Fault payload) {
					// The object has been delivered
					System.out.println(payload.getAssetId());
					
				}
			})
;			System.in.read();
//			connector.getEventProcessor().stopEventProcessing();
			connector.stop();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
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
	public ConnectorMessaging getEventProcessor() {
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
		serviceEnvironment.getEventProcessor().registerHandler(reference, listener);
	}

	@Override
	public <T> EventProducer<T> getMessageProducer(Reference reference, Class<T> clazz) {
		return getEventProcessor().getProducer(reference, clazz);
	}

	@Override
	public <T> EventProducer<T> getMessageProducer(String submodelIdentifier, Reference reference, Class<T> clazz) {
		return serviceEnvironment.getMessageProducer(submodelIdentifier, reference, clazz);
	}
	@Override
	public <T> void registerEventHandler(String submodelIdentifier, Reference reference, EventHandler<T> clazz) {
		serviceEnvironment.registerEventHandler(submodelIdentifier, reference, clazz);
	}

	@Override
	public Object executeOperation(String aasIdentifier, String submodelIdentifier, String path, Object parameter) {
		return serviceEnvironment.invokeOperation(aasIdentifier, submodelIdentifier, path, parameter);
	}


	@Override
	public void addAdministrationShell(AssetAdministrationShell shell) {
		serviceEnvironment.addAdministrationShell(shell);
		
	}


	@Override
	public void addSubmodel(String aasIdentifer, Submodel submodel) {
		serviceEnvironment.addSubmodel(aasIdentifer, submodel);
		
	}
	
}
