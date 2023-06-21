package at.srfg.iasset.connector;

import java.io.FileInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Supplier;

import at.srfg.iasset.connector.isproNG.IsproNGErrorCause;
import at.srfg.iasset.connector.isproNG.IsproNGMaintenanceAlert;
import at.srfg.iasset.connector.isproNG.IsproNGPublicAPIConnector;
import at.srfg.iasset.connector.isproNG.IsproNGStStamm;
import at.srfg.iasset.messaging.exception.MessagingException;
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

    private String currentStringValue = "123.5";
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

            // TODO: connector mit Anlagenname, -nummer, Standort, Enterprise initialisieren
            // TODO: connector mit API-Token initialisieren, der muss sich dann das Token abholen!
            // TODO: separate Directory & Repository
            // TODO: persistenz & reload
            IsproNGConnector connector = new IsproNGConnector();
            IsproNGPublicAPIConnector isproAPI = new IsproNGPublicAPIConnector("http://localhost:2518","1n09E-8zjE35kmYw2RhnI767wI7koyf8VHEi4V-tVrgb");
            // start the http endpoint for this Connector at port 5050
            connector.getLocalEnvironment().addAdministrationShell(AASFull.AAS_BELT_INSTANCE);
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
                    // fire only when these references are in the payload
                    // multiple references allowed
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
                    //fault.longText = stStamm.getNotizSuche();
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

            IsproNGErrorCause[] causes = isproAPI.GetObjectErrorCause();
            createFaultTest(connector);

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
