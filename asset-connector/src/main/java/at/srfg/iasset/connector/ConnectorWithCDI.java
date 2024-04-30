package at.srfg.iasset.connector;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.streaming.DataStreamWriter;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.DataTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.EventPayload;

import at.srfg.iasset.connector.api.ValueConsumer;
import at.srfg.iasset.connector.api.ValueSupplier;
import at.srfg.iasset.connector.component.AASComponent;
import at.srfg.iasset.connector.component.impl.AASFull;
import at.srfg.iasset.connector.featureStore.FeaturePipeline;
import at.srfg.iasset.connector.featureStore.FeatureRegistry;
import at.srfg.iasset.messaging.EventHandler;
import at.srfg.iasset.messaging.EventProducer;
import at.srfg.iasset.messaging.exception.MessagingException;
import at.srfg.iasset.repository.model.AASFaultSubmodel;
import at.srfg.iasset.repository.model.AASPlantStructureSubmodel;
import at.srfg.iasset.repository.model.AASSensorSubmodel;
import at.srfg.iasset.repository.model.AASZenonAlarm;
import at.srfg.iasset.repository.model.Fault;
import at.srfg.iasset.repository.model.PlantElement;
import at.srfg.iasset.repository.model.Sensor;
import at.srfg.iasset.repository.model.ZenonAlarm;
import at.srfg.iasset.repository.model.operation.OperationCallback;
import at.srfg.iasset.repository.model.operation.OperationInvocation;
import at.srfg.iasset.repository.model.operation.OperationInvocationResult;


public class ConnectorWithCDI {
	
	public static void main(String [] args) {
		AASComponent i40Component = AASComponent.create();
		// load
		loadData(i40Component);
		// start the endpoint
//		startEndpoint(i40Component);
		// @Jonas Demon für Zenon-Alarme
//		demoZenonAlarm(i40Component);
		
		// demonstrate operations
//		operationInvocation(i40Component);
		// demonstrate ValueSupplier & ValueConsumer
//		registerValueCallback(i40Component);
		// demonstrate Messaging
		//eventHandling(i40Component);
		// demonstrate FeatureStore
		try {
			featureStore(i40Component);
		} catch (MessagingException | TimeoutException | StreamingQueryException | IOException e) {
			e.printStackTrace();
		}
		// wait for a keystroke 
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// shutdown the component
		AASComponent.close();
	}
	
	private static void startEndpoint(AASComponent i40Component) {
		/*
		 * Start the HTTP-Endpoint 
		 */
		i40Component.startEndpoint();
		// Add an "alias" name for the Shell 
		i40Component.alias(AASFull.AAS_BELT_INSTANCE.getId(), "belt01");
		
	}
	private static void loadData(AASComponent i40Component) {
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
		
		i40Component.add(AASFull.AAS_BELT_INSTANCE.getId(), AASFaultSubmodel.SUBMODEL_FAULT1);
		i40Component.add(AASFull.AAS_BELT_INSTANCE.getId(), AASSensorSubmodel.SUBMODEL_SENSOR);
		
		
		// load the semantic integration pattern for reporting faults (from the repository
		//i40Component.loadPattern(AASFaultSubmodel.SUBMODEL_FAULT1.getId());
		// load the semantic integration pattern for sensor demo 
//		i40Component.loadPattern(AASSensorSubmodel.SUBMODEL_SENSOR.getId());	
		
		// load the semantic integration pattern for exchanging plat structure requests
		i40Component.loadPattern(AASPlantStructureSubmodel.SUBMODEL_PLANT_STRUCTURE_REQUEST_OPERATION);
		
		
		// @JONAS BEISPIEL
		// @Jonas: load AAS for Zenon including the submodel
		i40Component.add(AASZenonAlarm.ZENON_AAS);
		i40Component.add(AASZenonAlarm.ZENON_AAS.getId(), AASZenonAlarm.ZENON_SUBMODEL);
		// 

	}
	private static void operationInvocation(AASComponent i40Component) {
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

						Double d = invocation.getInput("doubleValue", Double.class);
						
						PlantElement plant = invocation.getInput("plantElement", PlantElement.class);
						
						List<PlantElement> structure = new ArrayList<>();
						PlantElement plant1= new PlantElement();
						plant1.setName("Plant Element1");
						plant1.setDescription("Plant1 Description");
						plant1.setIdentifiers(Collections.singletonList("plant1-identifier"));
						structure.add(plant1);
						PlantElement plant2= new PlantElement();
						plant2.setName("Plant Element1");
						plant2.setDescription("Plant1 Description");
						plant2.setIdentifiers(Collections.singletonList("plant2-identifier"));
						structure.add(plant2);
						invocation.setOutput("plantStructure", structure );
						invocation.setOutput("doubleValue", 654321.0);
						// success
						return true;
					}
				});

		OperationInvocationResult invocation = i40Component
				.getOperationRequest("http://iasset.salzburgresarch.at/common/plantStructure")
				.setInput("lastChange", Instant.now())
				.setInput("doubleValue", 12345.6)
				.setInput("plantElement", new PlantElement())
				// invoke the operation
				.invoke();
//  
// 		List<PlantElement> plantStructure = i40Component.getOperationResultList("http://iasset.salzburgresarch.at/common/plantStructure", Instant.now(), PlantElement.class);
 		
		Object objectResult = invocation.getResult("plantStructure");
		Double d = invocation.getResult("doubleValue", Double.class);
		List<PlantElement> plantList = invocation.getResultList("plantStructure", PlantElement.class);
		System.out.println(plantList.size());

	}

	private static void demoZenonAlarm(AASComponent i40Component) {
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
	private static void registerValueCallback(AASComponent i40Component) {
		
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
	private static void eventHandling(AASComponent i40Component) {
		/*
		 * demonstrate the event handling
		 */
		try {
			i40Component.registerCallback("http://iasset.salzburgresearch.at/semantic/fault", new EventHandler<Fault>() {

				@Override
				public void onEventMessage(EventPayload eventPayload, Fault payload) {
					System.out.println(payload.getFaultId() + " " + payload.getShortText()) ;
					
				}
			});
			
			i40Component.registerCallback("http://iasset.salzburgresearch.at/semantic/sensor", new EventHandler<Sensor>() {

				@Override
				public void onEventMessage(EventPayload eventPayload, Sensor payload) {
					System.out.println("Measured the following value: " + payload.getFeatureableValue());
					
				}
			});
			
			
			EventProducer<Fault> faultProducer = i40Component.getEventProducer("http://iasset.salzburgresearch.at/semantic/fault", Fault.class);
			Thread.sleep(5000);
			Fault theFault = new Fault();
			theFault.setAssetId(AASFull.AAS_BELT_INSTANCE.getId());
			theFault.setFaultId(AASFull.SUBMODEL_BELT_EVENT_INSTANCE.getId());
			theFault.setShortText("This is a short description!");
			faultProducer.sendEvent(theFault);
			
			EventProducer<Sensor> sensorProducer = i40Component.getEventProducer("http://iasset.salzburgresearch.at/semantic/sensor", Sensor.class);
			Thread.sleep(5000);
			Sensor theSensor = new Sensor();
			theSensor.setAssetId(AASFull.AAS_BELT_INSTANCE.getId());
			theSensor.setMeasurement("1233456789");
			sensorProducer.sendEvent(theSensor);
			
			
		
		} catch (MessagingException | InterruptedException e) {
			// show error messages
			e.printStackTrace();
		}

	}
	
	private static void featureStore(AASComponent i40Component) throws MessagingException, TimeoutException, StreamingQueryException, IOException {
		
		// --------- Settings --------- //
		final String featureName = "RPeaks";
		final String CHECKPOINT_LOCATION = "/home/sebastian/Documents/checkpoint";
		final String HADOOP_HOME_DIR =  "/home/sebastian/hadoop";
		final List<String> variableNames = new ArrayList<>(
				List.of("Timestamp_str", "field1"));
		final List<DataType> dataTypes = new ArrayList<>(
				List.of(DataTypes.StringType, DataTypes.DoubleType));
		
		// --------- Feature Store setup --------- //
		FeatureRegistry fregist = new FeatureRegistry("testapp", HADOOP_HOME_DIR, CHECKPOINT_LOCATION);
		Dataset<Row> df = fregist.registerEventInput(i40Component, "http://iasset.salzburgresearch.at/semantic/sensor", variableNames, dataTypes);
		 
		// --------- Demo producer setup ---------
		EventProducer<Sensor> sensorProducer = i40Component.getEventProducer("http://iasset.salzburgresearch.at/semantic/sensor", Sensor.class);
		Thread senderThread = new Thread(new FeatureEventSender(sensorProducer));
		senderThread.start();
		
		// --------- Demo feature and sink registration --------- //
		FeaturePipeline pipeline = fregist.registerPipeline(df, featureName);
		FeaturePipeline  results = pipeline;
		DataStreamWriter<Row> writer = fregist.registerConsoleSink(results, CHECKPOINT_LOCATION);
		
		// --------- Test run --------- //
		fregist.runQuery(writer);
		fregist.awaitTermination();
		
	}
	
	
	private static class FeatureEventSender implements Runnable{
		EventProducer<Sensor> sensorProducer;
		
		public FeatureEventSender(EventProducer<Sensor> sensorProducer) {
			this.sensorProducer = sensorProducer;
		}
		

		@Override
		public void run() {
			try(BufferedReader br = new BufferedReader(new FileReader("src/main/resources/testsig_thor.csv"))){
				String line = br.readLine(); // skip Header
				while((line = br.readLine()) != null)
					try {
						String[] values = line.split(",");
						Sensor theSensor = new Sensor();
						theSensor.setAssetId(AASFull.AAS_BELT_INSTANCE.getId());
						theSensor.setTimestampCreated((values[0]));
						theSensor.setMeasurement(values[1]);
						
						sensorProducer.sendEvent(theSensor);				
						
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (MessagingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			
		}
		
	}
	
}
