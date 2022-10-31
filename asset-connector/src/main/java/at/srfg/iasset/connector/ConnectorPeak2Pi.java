package at.srfg.iasset.connector;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.aas4j.v3.model.Reference;

import at.srfg.iasset.connector.component.ConnectorEndpoint;
import at.srfg.iasset.connector.environment.LocalEnvironment;
import at.srfg.iasset.connector.environment.LocalServiceEnvironment;
import at.srfg.iasset.connector.environment.ModelListener;
import at.srfg.iasset.repository.component.ServiceEnvironment;

public class ConnectorPeak2Pi implements LocalEnvironment {
	
	private String currentStringValue = "123.5";
	private LocalServiceEnvironment serviceEnvironment;
	
	public ConnectorPeak2Pi(URI repositoryURL) {
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
			Peak2PiConnector p2p = new Peak2PiConnector("http://192.168.1.190:3000");
			ConnectorPeak2Pi peak2piInstance = new ConnectorPeak2Pi( new URI("http://localhost:8081/"));
			// start the http endpoint for this Connector at port 5050
			peak2piInstance.startEndpoint(5050);
			// create 
			peak2piInstance.addHandler("https://peak2pi.info/Peak2Pi_AssetAdministrationShell", "peak2pi");
		
			peak2piInstance.setValueSupplier(
					"https://peak2pi.info/Peak2Pi_AssetAdministrationShell", 
					"http://peak2pi.info/labor/oee#properties", 
					"oeeData.oee", 
					new Supplier<String>() {

						@Override
						public String get() {
							return p2p.OEE("oee");
						}


					});
			
			peak2piInstance.setValueSupplier(
					"https://peak2pi.info/Peak2Pi_AssetAdministrationShell", 
					"http://peak2pi.info/labor/oee#properties", 
					"oeeData.capacity", 
					new Supplier<String>() {

						@Override
						public String get() {
							return p2p.OEE("capacity");
						}


					});
			
			peak2piInstance.setValueSupplier(
					"https://peak2pi.info/Peak2Pi_AssetAdministrationShell", 
					"http://peak2pi.info/labor/oee#properties", 
					"oeeData.quality", 
					new Supplier<String>() {

						@Override
						public String get() {
							return p2p.OEE("quality");
						}


					});
			
			peak2piInstance.setValueSupplier(
					"https://peak2pi.info/Peak2Pi_AssetAdministrationShell", 
					"http://peak2pi.info/labor/oee#properties", 
					"oeeData.efficiency", 
					new Supplier<String>() {

						@Override
						public String get() {
							return p2p.OEE("efficiency");
						}


					});
			
			peak2piInstance.setValueSupplier(
					"https://peak2pi.info/Peak2Pi_AssetAdministrationShell", 
					"http://peak2pi.info/labor/oee#properties", 
					"oeeData.quantity", 
					new Supplier<String>() {

						@Override
						public String get() {
							return p2p.OEE("ist");
						}


					});
			
			peak2piInstance.setValueSupplier(
					"https://peak2pi.info/Peak2Pi_AssetAdministrationShell", 
					"http://peak2pi.info/labor/oee#properties", 
					"oeeData.setvalue", 
					new Supplier<String>() {

						@Override
						public String get() {
							return p2p.OEE("sVal");
						}


					});
			
			peak2piInstance.setValueSupplier(
					"https://peak2pi.info/Peak2Pi_AssetAdministrationShell", 
					"http://peak2pi.info/labor/oee#product", 
					"product.productId", 
					new Supplier<String>() {

						@Override
						public String get() {
							return p2p.GetProductByKey("prodId");
						}


					});
			
			peak2piInstance.setValueSupplier(
					"https://peak2pi.info/Peak2Pi_AssetAdministrationShell", 
					"http://peak2pi.info/labor/oee#product", 
					"product.quantity", 
					new Supplier<String>() {

						@Override
						public String get() {
							return p2p.GetProductByKey("planQuantity");
						}


					});
			
			peak2piInstance.setValueSupplier(
					"https://peak2pi.info/Peak2Pi_AssetAdministrationShell", 
					"http://peak2pi.info/labor/oee#product", 
					"product.validFrom", 
					new Supplier<String>() {

						@Override
						public String get() {
							return p2p.GetProductByKey("validFrom");
						}


					});
			
			peak2piInstance.setValueConsumer(
					"https://peak2pi.info/Peak2Pi_AssetAdministrationShell", 
					"http://peak2pi.info/labor/oee#product", 
					"product.productId", 
					new Consumer<String>() {

						@Override
						public void accept(final String t) {
							System.out.println("New Value provided: " + t);
							p2p.SetProduct(t);
							
						}
					});
			
			
			peak2piInstance.register("http://peak2pi.info/labor/oee#aas");
			
			System.in.read();
			peak2piInstance.stop();
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
			Function<Map<String, Object>, Object> function) {
		serviceEnvironment.setOperationFunction(aasIdentifier, submodelIdentifier, path, function);		
	}
	@Override
	public ConnectorEndpoint startEndpoint(int port) {
		return serviceEnvironment.startEndpoint(port);
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
	public <T> void addMesssageListener(Reference reference, MessageListener<T> listener) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public <T> MessageProducer<T> getMessageProducer(Reference reference, Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}


}
