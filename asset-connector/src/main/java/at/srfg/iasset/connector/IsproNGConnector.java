package at.srfg.iasset.connector;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import at.srfg.iasset.connector.isproNG.IsproNGCause;
import at.srfg.iasset.connector.isproNG.IsproNGMaintenanceAlert;
import at.srfg.iasset.connector.isproNG.IsproNGPublicAPIConnector;
import at.srfg.iasset.connector.isproNG.IsproNGStStamm;
import at.srfg.iasset.messaging.exception.MessagingException;
import at.srfg.iasset.repository.connectivity.rest.ClientFactory;
import at.srfg.iasset.repository.model.ErrorCode;
import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.aas4j.v3.model.EventPayload;
import org.eclipse.aas4j.v3.model.KeyTypes;
import org.eclipse.aas4j.v3.model.Referable;
import org.eclipse.aas4j.v3.model.Reference;
import org.eclipse.aas4j.v3.model.ReferenceTypes;
import org.eclipse.aas4j.v3.model.impl.DefaultKey;
import org.eclipse.aas4j.v3.model.impl.DefaultReference;

import at.srfg.iasset.connector.component.impl.AASFull;
import at.srfg.iasset.connector.environment.LocalEnvironment;
import at.srfg.iasset.connector.environment.LocalServiceEnvironment;
import at.srfg.iasset.messaging.EventHandler;
import at.srfg.iasset.messaging.EventProducer;
import at.srfg.iasset.repository.component.ModelListener;
import at.srfg.iasset.repository.component.ServiceEnvironment;
import at.srfg.iasset.repository.model.AASFaultSubmodel;
import at.srfg.iasset.repository.model.Fault;
import at.srfg.iasset.repository.utils.ReferenceUtils;

import com.google.gson.Gson;

public class IsproNGConnector {
    private LocalServiceEnvironment serviceEnvironment;

    public IsproNGConnector() {
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

            IsproNGConnector connector = new IsproNGConnector();
            IsproNGPublicAPIConnector isproAPI = new IsproNGPublicAPIConnector(
                    connector.getServiceEnvironment().getConfigProperty("connector.isprong.baseUri"),
                    connector.getServiceEnvironment().getConfigProperty("connector.isprong.apikey"));


            connector.getLocalEnvironment().addAdministrationShell(AASFull.AAS_BELT_INSTANCE);
            connector.getLocalEnvironment().addSubmodel(AASFull.AAS_BELT_INSTANCE.getId(), AASFaultSubmodel.SUBMODEL_FAULT_OPERATIONS);
            connector.getLocalEnvironment().addSubmodel(AASFull.AAS_BELT_INSTANCE.getId(), AASFull.SUBMODEL_BELT_PROPERTIES_INSTANCE);
            connector.getLocalEnvironment().addSubmodel(AASFull.AAS_BELT_INSTANCE.getId(), AASFull.SUBMODEL_BELT_EVENT_INSTANCE);
            connector.getLocalEnvironment().addSubmodel(AASFull.AAS_BELT_INSTANCE.getId(), AASFull.SUBMODEL_BELT_OPERATIONS_INSTANCE);

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
            connector.register("https://acplt.org/Test_AssetAdministrationShell");
            connector.register(AASFull.AAS_BELT_INSTANCE.getId());

            connector.getLocalEnvironment().registerEventHandler(
                    new EventHandler<Fault>() {

                        @Override
                        public void onEventMessage(EventPayload eventPayload, Fault payload) {
                            if(payload.assetId !=null && payload.assetId != "")                            {
                                System.out.println(payload.getFaultId() + " " + payload.getShortText()) ;
                                IsproNGMaintenanceAlert alert = new IsproNGMaintenanceAlert(payload.shortText,payload.longText,payload.priority, payload.faultId,"",payload.assetId);
                                System.out.println(isproAPI.GenerateMaintenanceAlert(alert));
                            }
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
                    ReferenceUtils.asGlobalReferences("http://iasset.salzburgresearch.at/semantic/fault")
            );

            isproAPI.addListener(new Consumer<String>() {
                @Override
                public void accept(String s) {
                    EventProducer<Fault> faultProducer = connector.getLocalEnvironment().getMessageProducer(
                            // ModelReference
                            ReferenceUtils.asGlobalReference(KeyTypes.GLOBAL_REFERENCE, "http://iasset.salzburgresearch.at/semantic/fault"),
                            Fault.class);

                    Fault fault = new Fault();
                    Gson gson = new Gson(); // Or use new GsonBuilder().create();
                    IsproNGStStamm stStamm = gson.fromJson(s, IsproNGStStamm.class);

                    fault.shortText = stStamm.getText();
                    fault.assetId = stStamm.getFk1();
                    fault.faultId = stStamm.getIhStoerTFreifeld1Meldung();
                    fault.priority = stStamm.getPriority();
                    fault.maintencanceUserId = stStamm.getMonteur1();
                    fault.timestampFinished = new Date();
                    try {
                        faultProducer.sendEvent(fault);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });

            connector.getLocalEnvironment().setOperationFunction(AASFull.AAS_BELT_INSTANCE.getId(), AASFaultSubmodel.SUBMODEL_FAULT_OPERATIONS.getId(), "getErrorCause", new Function<Object,Object>() {
                // TODO: make function call "type safe" with generics
                //       distinguish between function calls with ValueOnly and Full meta data
                @Override
                public Object apply(Object t) {
                    //
                    JsonNode node = ClientFactory.getObjectMapper().valueToTree(t);
                    Map<String, Object> parameterMap = ClientFactory.getObjectMapper().convertValue(t, Map.class);
                    System.out.println(parameterMap.get("assetIdentifier"));
                    List<ErrorCode> codes = new ArrayList<>();

                    for (IsproNGCause c: isproAPI.GetObjectErrorCause()) {
                        codes.add(new ErrorCode(c.getCode(), c.getDescription()));
                    }
                    return codes;
                }});

            //createFaultTest(connector);

            System.in.read();
            // shutdown
            connector.stop();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void createFaultTest(IsproNGConnector connector)
    {
        EventProducer<Fault> faultProducer = connector.getLocalEnvironment().getMessageProducer(
                // ModelReference
                ReferenceUtils.asGlobalReference(KeyTypes.GLOBAL_REFERENCE, "http://iasset.salzburgresearch.at/semantic/fault"),
                Fault.class);
        Fault f = new Fault();
        f.setFaultId("12345");
        f.setAssetId("IAssetAsset1");
        f.setSenderUserId("im am the user");
        f.setShortText("this is a short");
        try {
            faultProducer.sendEvent(f);
        } catch (MessagingException e) {
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
