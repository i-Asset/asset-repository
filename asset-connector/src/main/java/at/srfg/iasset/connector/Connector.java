package at.srfg.iasset.connector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.eclipse.aas4j.v3.model.EventPayload;
import org.eclipse.aas4j.v3.model.KeyTypes;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.ReferenceTypes;
import org.eclipse.aas4j.v3.model.impl.DefaultKey;
import org.eclipse.aas4j.v3.model.impl.DefaultReference;

import com.fasterxml.jackson.databind.JsonNode;

import at.srfg.iasset.connector.component.impl.AASFull;
import at.srfg.iasset.connector.environment.LocalEnvironment;
import at.srfg.iasset.connector.environment.LocalServiceEnvironment;
import at.srfg.iasset.messaging.EventHandler;
import at.srfg.iasset.messaging.EventProducer;
import at.srfg.iasset.repository.component.ModelListener;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.connectivity.rest.ClientFactory;
import at.srfg.iasset.repository.model.AASFaultSubmodel;
import at.srfg.iasset.repository.model.ErrorCode;
import at.srfg.iasset.repository.model.Fault;
import at.srfg.iasset.repository.utils.ReferenceUtils;


public class Connector {

	private String currentStringValue = "123.5";
	private LocalServiceEnvironment serviceEnvironment;

	public Connector() {
		this.serviceEnvironment = new LocalServiceEnvironment();
	}


	public void stop() {
		serviceEnvironment.shutdownEndpoint();
		serviceEnvironment.getEventProcessor().stopEventProcessing();
	}

	public ServiceEnvironment getServiceEnvironment() {
		return serviceEnvironment;

	}
	public LocalEnvironment getLocalEnvironment() {
		return serviceEnvironment;
	}

	public static void main(String [] args) {

		try {

			// TODO: connector mit Anlagenname, -nummer, Standort, Enterprise initialisieren
			// TODO: connector mit API-Token initialisieren, der muss sich dann das Token abholen!
			// TODO: separate Directory & Repository
			// TODO: persistenz & reload
			Connector connector = new Connector();
			connector.getLocalEnvironment().addAdministrationShell(AASFull.AAS_BELT_INSTANCE);
			// load submodel with operations (getErrorCause)
			connector.getLocalEnvironment().addSubmodel(AASFull.AAS_BELT_INSTANCE.getId(), AASFaultSubmodel.SUBMODEL_FAULT_OPERATIONS);
			connector.getLocalEnvironment().addSubmodel(AASFull.AAS_BELT_INSTANCE.getId(), AASFull.SUBMODEL_BELT_PROPERTIES_INSTANCE);
			connector.getLocalEnvironment().addSubmodel(AASFull.AAS_BELT_INSTANCE.getId(), AASFull.SUBMODEL_BELT_EVENT_INSTANCE);
			connector.getLocalEnvironment().addSubmodel(AASFull.AAS_BELT_INSTANCE.getId(), AASFull.SUBMODEL_BELT_OPERATIONS_INSTANCE);
			// load fault submodel
			// FIXME: improve
			Reference pattern = new DefaultReference.Builder()
					.type(ReferenceTypes.MODEL_REFERENCE)
					.key(new DefaultKey.Builder()
							.type(KeyTypes.SUBMODEL)
							.value(AASFaultSubmodel.SUBMODEL_FAULT1.getId())
							.build())
					.build();
			// resolve the pattern
			Optional<Referable> optPattern = connector.getLocalEnvironment().resolveReference(pattern);
			if ( optPattern.isEmpty() ) {
				return;
			}

			// start the http endpoint for this Connector at port 5050
			connector.getLocalEnvironment().startEndpoint(5050);

			// create
			connector.getLocalEnvironment().addHandler("https://acplt.org/Test_AssetAdministrationShell", "test");

			connector.getLocalEnvironment().setValueConsumer(
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
			connector.getLocalEnvironment().setValueSupplier(
					"https://acplt.org/Test_AssetAdministrationShell",
					"https://acplt.org/Test_Submodel",
					"ExampleSubmodelCollectionOrdered.ExampleDecimalProperty",
					new Supplier<String>() {

						@Override
						public String get() {
							return connector.currentStringValue;
						}


					});
			// register function to be invoked when "getErrorCause" operation is
			// invoked via HTTP-Request
			connector.getLocalEnvironment().setOperationFunction(AASFull.AAS_BELT_INSTANCE.getId(), AASFaultSubmodel.SUBMODEL_FAULT_OPERATIONS.getId(), "getErrorCause", new Function<Object,Object>() {
				// TODO: make function call "type safe" with generics
				//       distinguish between function calls with ValueOnly and Full meta data
				@Override
				public Object apply(Object t) {  // inputvariable
					// 
					JsonNode node = ClientFactory.getObjectMapper().valueToTree(t);
					Map<String, Object> parameterMap = ClientFactory.getObjectMapper().convertValue(t, Map.class);
					System.out.println(parameterMap.get("assetIdentifier"));
					// graphql-Abfrage
					// output erstellen
					List<ErrorCode> codes = new ArrayList<>();
					codes.add(new ErrorCode("c1", "Label C1"));
					codes.add(new ErrorCode("c2", "Label C2"));
					codes.add(new ErrorCode("c3", "Label C3"));
					return codes; // output variable
				}});
			
			
			
			// used to read OPC-UA values
			connector.getLocalEnvironment().setValueSupplier(
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
			// TODO: allow registering a Handler with multiple References
			//       fire event only when ALL references are present!
			connector.getLocalEnvironment().registerEventHandler(
					new EventHandler<Fault>() {

						@Override
						public void onEventMessage(EventPayload eventPayload, Fault payload) {
							System.out.println(payload.getFaultId() + " " + payload.getShortText()) ;

						}

						@Override
						public Class<Fault> getPayloadType() {
							return Fault.class;
						}

						@Override
						public Reference getSemanticId() {
							return ReferenceUtils.asGlobalReference("http://iasset.salzburgresearch.at/semantic/fault");
						}
					},
					// fire only when these references are in the payload
					// multiple references allowed
					ReferenceUtils.asGlobalReferences("http://iasset.salzburgresearch.at/semantic/fault")
					);




			EventProducer<Fault> faultProducer = connector.getLocalEnvironment().getMessageProducer(
					// ModelReference
					ReferenceUtils.asGlobalReference(KeyTypes.GLOBAL_REFERENCE, "http://iasset.salzburgresearch.at/semantic/fault"),
					Fault.class);


			Fault f = new Fault();
			f.setFaultId("12345");
			f.setAssetId("assetId");
			f.setSenderUserId("berni");
			f.setShortText("this is a short");
			faultProducer.sendEvent(f);

			System.in.read();
			// shutdown
			connector.stop();
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
}
