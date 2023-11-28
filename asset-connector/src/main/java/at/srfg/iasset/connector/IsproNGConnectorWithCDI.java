package at.srfg.iasset.connector;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import at.srfg.iasset.connector.isproNG.*;
import at.srfg.iasset.repository.utils.ReferenceUtils;
import com.google.gson.Gson;
import jakarta.inject.Inject;
import org.eclipse.digitaltwin.aas4j.v3.model.EventPayload;

import at.srfg.iasset.connector.api.ValueConsumer;
import at.srfg.iasset.connector.api.ValueSupplier;
import at.srfg.iasset.connector.component.AASComponent;
import at.srfg.iasset.connector.component.impl.AASFull;
import at.srfg.iasset.messaging.EventHandler;
import at.srfg.iasset.messaging.EventProducer;
import at.srfg.iasset.messaging.exception.MessagingException;
import at.srfg.iasset.repository.model.AASFaultSubmodel;
import at.srfg.iasset.repository.model.AASPlantStructureSubmodel;
import at.srfg.iasset.repository.model.AASZenonAlarm;
import at.srfg.iasset.repository.model.Fault;
import at.srfg.iasset.repository.model.PlantElement;
import at.srfg.iasset.repository.model.ZenonAlarm;
import at.srfg.iasset.repository.model.operation.OperationCallback;
import at.srfg.iasset.repository.model.operation.OperationInvocation;
import at.srfg.iasset.repository.model.operation.OperationInvocationResult;
import org.eclipse.digitaltwin.aas4j.v3.model.KeyTypes;

//6fe93c2ebc8930c6b0dd8d21348bc637b9898e92
//https://iasset.sensornet.salzburgresearch.at/mongoadmin/db/asset/assetAdministrationShellDescriptor
//Nicht portierte sache (faultCause) ist noch in der File IsproNGConnector.txt!!
public class IsproNGConnectorWithCDI {
    private AASComponent i40Component = AASComponent.create();
    private IsproNGPublicAPIConnector isproAPI;
    @Inject
    private IsproNGSettings settings;
    public IsproNGConnectorWithCDI() throws IOException {
        isproAPI = new IsproNGPublicAPIConnector(
                "http://192.168.48.64/IsproWebAPI",
                "tKhc8Dg3kxR68fE9r2stbW_IXXvZap02D6ZbphDLOUcb"); // Server von Salzburg Research
    }

    public static void main(String [] args) throws IOException {
        IsproNGConnectorWithCDI connector = new IsproNGConnectorWithCDI();
        connector.LoadData();
        // start the endpoint
//		connector.StartEndpoint(i40Component);
        // @Jonas Demon für Zenon-Alarme
        //connector.DemoZenonAlarm();

        // demonstrate operations
        connector.OperationInvocation();
        // demonstrate ValueSupplier & ValueConsumer
        //connector.RegisterValueCallback();
        // demonstrate Messaging
        connector.EventHandlingForIspro();
        connector.EventHandling();
        //
        // wait for a keystroke
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // shutdown the component
        AASComponent.close();
    }

    public void EventHandlingForIspro() {
        isproAPI.addListener(new Consumer<String>() {
            @Override
            public void accept(String s) {
                EventProducer<Fault> faultProducer = i40Component.getEventProducer("http://iasset.salzburgresearch.at/semantic/fault", Fault.class);

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
    }

    public void EventHandling() {
        try {
            i40Component.registerCallback("http://iasset.salzburgresearch.at/semantic/fault", new EventHandler<Fault>() {

                @Override
                public void onEventMessage(EventPayload eventPayload, Fault payload) {
                    if(payload.assetId !=null && payload.assetId != "")                            {
                        System.out.println(payload.getFaultId() + " " + payload.getShortText()) ;
                        IsproNGMaintenanceAlert alert = new IsproNGMaintenanceAlert(payload.shortText,payload.longText,payload.priority, payload.faultId,"",payload.assetId);
                        System.out.println(isproAPI.GenerateMaintenanceAlert(alert));
                    }

                }
            });



            /* // test message
            EventProducer<Fault> faultProducer = i40Component.getEventProducer("http://iasset.salzburgresearch.at/semantic/fault", Fault.class);
            Thread.sleep(5000);
            Fault theFault = new Fault();
            theFault.setAssetId(AASFull.AAS_BELT_INSTANCE.getId());
            theFault.setFaultId(AASFull.SUBMODEL_BELT_EVENT_INSTANCE.getId());
            theFault.setShortText("This is a short description!");
            faultProducer.sendEvent(theFault);*/

        } catch (MessagingException e) {
            // show error messages
            e.printStackTrace();
        }

    }

    public void StartEndpoint() {
        /*
         * Start the HTTP-Endpoint
         */
        i40Component.startEndpoint();
        // Add an "alias" name for the Shell
        i40Component.alias(AASFull.AAS_BELT_INSTANCE.getId(), "belt01");

    }
    public void LoadData() {
        i40Component.addListener(new ModelChangeLogger());
        // obtain a method invocation request object and apply parameters


        // add the AAS Instance to the I40 Component
        i40Component.add(AASFull.AAS_BELT_INSTANCE);
        // add the PROPERTIES Submodel to the I40 Component's AAS
        i40Component.add(AASFull.AAS_BELT_INSTANCE.getId(), AASFull.SUBMODEL_BELT_PROPERTIES_INSTANCE);
        // add the EVENT Submodel to the I40 Component's AAS
        i40Component.add(AASFull.AAS_BELT_INSTANCE.getId(), AASFull.SUBMODEL_BELT_EVENT_INSTANCE);
        // add the OPERATIONS Submodel to the I40 Component's AAS
        i40Component.add(AASFull.AAS_BELT_INSTANCE.getId(), AASFull.SUBMODEL_BELT_OPERATIONS_INSTANCE);
        i40Component.add(AASFull.AAS_BELT_INSTANCE.getId(), AASPlantStructureSubmodel.SUBMODEL_PLANT_STRUCTURE_REQUEST_OPERATION);


        // load the semantic integration pattern for reporting faults (from the repository
        i40Component.loadPattern(AASFaultSubmodel.SUBMODEL_FAULT1.getId());
        // load the semantic integration pattern for exchanging plat structure requests
        i40Component.loadPattern(AASPlantStructureSubmodel.SUBMODEL_PLANT_STRUCTURE_REQUEST_OPERATION);


        // @JONAS BEISPIEL
        // @Jonas: load AAS for Zenon including the submodel
        i40Component.add(AASZenonAlarm.ZENON_AAS);
        i40Component.add(AASZenonAlarm.ZENON_AAS.getId(), AASZenonAlarm.ZENON_SUBMODEL);
        //

    }
    public void OperationInvocation() {
        i40Component.register(AASFull.AAS_BELT_INSTANCE.getId());
        /*
         * Test/Check the execution of operations
         */
        i40Component.registerCallback(
                AASFull.AAS_BELT_INSTANCE.getId(),
                AASPlantStructureSubmodel.SUBMODEL_PLANT_STRUCTURE_REQUEST_OPERATION.getId(),
                "getPlantStructure",
                new OperationCallback() {

                    @Override
                    public boolean execute(OperationInvocation invocation) {
                        IsproNGPlantStructure[] p = isproAPI.GetObjectPlantStructure();
                        List<PlantElement> structure = new ArrayList<>();
                        for(IsproNGPlantStructure plant : p) {
                            PlantElement plant1= new PlantElement();
                            plant1.setName(plant.getName());
                            plant1.setDescription(plant.getForeignKey1());
                            plant1.setIdentifiers(Collections.singletonList(Integer.toString(plant.getId())));
                            structure.add(plant1);
                        }
                        invocation.setOutput("plantStructure", structure );

                        // success
                        return true;
                    }
                });

        /*
        //Abrufen der Anlagenstrktur
        OperationInvocationResult invocation = i40Component
                .getOperationRequest("http://iasset.salzburgresarch.at/common/plantStructure")
                .setInput("lastChange", Instant.now())
                .setInput("doubleValue", 12345.6)
                .setInput("plantElement", new PlantElement())
                // invoke the operation
                .invoke();

        Object objectResult = invocation.getResult("plantStructure");
        Double d = invocation.getResult("doubleValue", Double.class);
        List<PlantElement> plantList = invocation.getResultList("plantStructure", PlantElement.class);
        System.out.println(plantList.size());*/

    }

    public void DemoZenonAlarm() {
        i40Component.register(AASZenonAlarm.ZENON_AAS.getId());
        /*
         * Test/Check the execution of operations
         */
        i40Component.registerCallback(
                AASZenonAlarm.ZENON_AAS.getId(),
                AASZenonAlarm.ZENON_SUBMODEL.getId(),
                "zenonAlarm",
                new OperationCallback() {

                    @Override
                    public boolean execute(OperationInvocation invocation) {
                        Instant timeFrom = invocation.getInput("timeFrom", Instant.class);
                        Instant timeTo = invocation.getInput("timeTo", Instant.class);

                        List<ZenonAlarm> alarme = new ArrayList<>();
                        // ZENON Abfrage Beginn
                        ZenonAlarm a1 = new ZenonAlarm();
                        a1.setVariable("variable");
                        a1.setAlarmClass("zenon Class A");
                        a1.setAlarmGroup("zenob Group 1");
                        a1.setAlarmText("Demo Alarm, to be replaced");
                        a1.setTimeComes(Instant.now().minusMillis(20000));
                        a1.setTimeGoes(Instant.now());
                        // success
                        alarme.add(a1);
                        // ZENON ABFRAGE End
                        invocation.setOutput("result", alarme);
                        return true;
                    }
                });

        OperationInvocationResult invocation = i40Component
                .getOperationRequest("http://iasset.salzburgresearch.at/zenon/alarm")
                .setInput("timeFrom", Instant.now().minusMillis(30000))
                .setInput("timeTo", Instant.now())
                // invoke the operation
                .invoke();

        Object objectResult = invocation.getResult("result");
        List<ZenonAlarm> plantList = invocation.getResultList("result", ZenonAlarm.class);
        System.out.println(plantList.size());

    }
    public void RegisterValueCallback() {

        i40Component.registerCallback(
                // name the AAS
                AASFull.AAS_BELT_INSTANCE.getId(),
                // name the Submodel
                AASFull.SUBMODEL_BELT_PROPERTIES_INSTANCE.getId(),
                // name the SubmodelElement by it's path!
                "beltData.distance",
                // define the callback routine
                new ValueSupplier<Double>() {

                    @Override
                    public Double get() {
                        // provide a new value
                        return Math.random();

                    }
                });
        i40Component.registerCallback(
                // name the AAS
                AASFull.AAS_BELT_INSTANCE.getId(),
                // name the Submodel
                AASFull.SUBMODEL_BELT_PROPERTIES_INSTANCE.getId(),
                // name the SubmodelElement by it's path!
                "beltData.distance",
                // define the callback routine
                new ValueConsumer<Double>() {

                    @Override
                    public void accept(Double newValue) {
                        i40Component.info("New Value for {}: {}", "beltData.distance", newValue);

                    }
                });
        Double random = Math.random();

        i40Component.setElementValue(
                // name the AAS
                AASFull.AAS_BELT_INSTANCE.getId(),
                // name the Submodel
                AASFull.SUBMODEL_BELT_PROPERTIES_INSTANCE.getId(),
                // name the SubmodelElement by it's path!
                "beltData.distance",
                random
        );
        Double value = i40Component.getElementValue(
                // name the AAS
                AASFull.AAS_BELT_INSTANCE.getId(),
                // name the Submodel
                AASFull.SUBMODEL_BELT_PROPERTIES_INSTANCE.getId(),
                // name the SubmodelElement by it's path!
                "beltData.distance",
                Double.class
        );
        i40Component.info("Initial: {}, Returned: {}", random, value);
        System.out.println(String.format("Initial: %s, Returned: %s", random, value));

    }

}
