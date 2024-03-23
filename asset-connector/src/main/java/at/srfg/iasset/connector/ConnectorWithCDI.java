package at.srfg.iasset.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import at.srfg.iasset.repository.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.eclipse.digitaltwin.aas4j.v3.model.EventPayload;

import at.srfg.iasset.connector.api.ValueConsumer;
import at.srfg.iasset.connector.api.ValueSupplier;
import at.srfg.iasset.connector.component.AASComponent;
import at.srfg.iasset.connector.component.impl.AASFull;
import at.srfg.iasset.messaging.EventHandler;
import at.srfg.iasset.messaging.EventProducer;
import at.srfg.iasset.messaging.exception.MessagingException;
import at.srfg.iasset.repository.model.operation.OperationCallback;
import at.srfg.iasset.repository.model.operation.OperationInvocation;
import at.srfg.iasset.repository.model.operation.OperationInvocationResult;
import org.springframework.cglib.proxy.NoOp;


public class ConnectorWithCDI {
	
	public static void main(String [] args) {
		AASComponent i40Component = AASComponent.create();
		// load
		loadData(i40Component);
		// start the endpoint
//		startEndpoint(i40Component);
		// @Jonas Demon für Zenon-Alarme
		demoZenonVariable(i40Component);
		demoZenonAlarm(i40Component);
		demoZenonArchives(i40Component);

		// demonstrate operations
		//operationInvocation(i40Component);
		// demonstrate ValueSupplier & ValueConsumer
		registerValueCallback(i40Component);
		// demonstrate Messaging
		//eventHandling(i40Component);
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
	
//	private static void startEndpoint(AASComponent i40Component) {
//		/*
//		 * Start the HTTP-Endpoint
//		 */
//		i40Component.startEndpoint();
//		// Add an "alias" name for the Shell
//		i40Component.alias(AASFull.AAS_BELT_INSTANCE.getId(), "belt01");
//
//	}
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
		
		
		// load the semantic integration pattern for reporting faults (from the repository
		i40Component.loadPattern(AASFaultSubmodel.SUBMODEL_FAULT1.getId());
		// load the semantic integration pattern for exchanging plat structure requests
		i40Component.loadPattern(AASPlantStructureSubmodel.SUBMODEL_PLANT_STRUCTURE_REQUEST_OPERATION);

		
		// @JONAS BEISPIEL
		// @Jonas: load AAS for Zenon including the submodel
		i40Component.add(AASZenonAlarm.ZENON_AAS_ALARMS);
		i40Component.add(AASZenonAlarm.ZENON_AAS_ALARMS.getId(), AASZenonAlarm.ZENON_SUBMODEL_ALARMS);

		i40Component.add(AASZenonVariable.ZENON_AAS_VARS);
		i40Component.add(AASZenonVariable.ZENON_AAS_VARS.getId(), AASZenonVariable.ZENON_SUBMODEL_VARS);

		i40Component.add(AASZenonArchive.ZENON_AAS_ARCHIVES);
		i40Component.add(AASZenonArchive.ZENON_AAS_ARCHIVES.getId(), AASZenonArchive.ZENON_SUBMODEL_ARCHIVES);
		//

	}

	public static String trimDateTime(String toTrim) {
		LocalDateTime localDateTime = LocalDateTime.parse(toTrim, DateTimeFormatter.ISO_DATE_TIME);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");
		String formatted = localDateTime.format(formatter);
		return formatted;
	}
	public static String trimString(String original, int maxLength) {
		if (original.length() > maxLength) {
			return (original.substring(0, maxLength) + " ...");
		} else {
			return original;
		}
	}

	private static void demoZenonVariable(AASComponent i40Component) {
		i40Component.register(AASZenonVariable.ZENON_AAS_VARS.getId());
		/*
		 * Test/Check the execution of operations
		 */
		i40Component.registerCallback(
				AASZenonVariable.ZENON_AAS_VARS.getId(),
				AASZenonVariable.ZENON_SUBMODEL_VARS.getId(),
				"zenonVariable",
				new OperationCallback() {

					@Override
					public boolean execute(OperationInvocation invocation) {
						System.out.println("execute invoked (zenonDemoVariable)");


						try {
							System.out.println("Getting zenon variables data...");

							URL webtoolBackend = new URL("http://localhost:5046/variables");
							HttpURLConnection con = (HttpURLConnection) webtoolBackend.openConnection();
							con.setRequestMethod("GET");
							System.out.println("Backend response status-code: " + con.getResponseCode());

							BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
							String inputLine;
							StringBuffer response = new StringBuffer();
							while ((inputLine = in.readLine()) != null) {
								//System.out.println(inputLine);
								response.append(inputLine);
							}
							in.close();
							System.out.println("Raw backend response (variables json):  " + trimString(response.toString(), 150));

							/* Json String to Object */
							String jsonString = response.toString();
							ObjectMapper mapper = new ObjectMapper();
							mapper.registerModule(new JavaTimeModule());
							GraphQLResponse_variables zenonResponse = mapper.readValue(jsonString, GraphQLResponse_variables.class);



							// Extracted alarms to objects
							List<ZenonVariable> variables = new ArrayList<>();

							for(int i = 0; i < zenonResponse.getData().getVariablesData().size(); ++i){
								ZenonVariable response_Variables  = zenonResponse.getData().getVariablesData().get(i);
								ZenonVariable variablesObj = new ZenonVariable();

								variablesObj.setVariableName(response_Variables.getVariableName());
								variablesObj.setDisplayName(response_Variables.getDisplayName());
								variablesObj.setIdentification(response_Variables.getIdentification());
								variablesObj.setDescription(response_Variables.getDescription());
								variablesObj.setDataType(response_Variables.getDataType());
								variablesObj.setResourcesLabel(response_Variables.getResourcesLabel());
								variablesObj.setMeasuringUnit(response_Variables.getMeasuringUnit());


								// success
								variables.add(variablesObj);

							}
							// ZENON ABFRAGE End
							invocation.setOutput("result", variables);

						} catch (Exception e) {
							e.printStackTrace();
						}

						return true;
					}
				});

		OperationInvocationResult invocation = i40Component
				.getOperationRequest("http://iasset.salzburgresearch.at/zenon/variable")
				// invoke the operation
				.invoke();


		Object objectResult = invocation.getResult("result");
		List<ZenonVariable> varList = invocation.getResultList("result", ZenonVariable.class);
		System.out.println(varList.size() + " variables read from backend.\r\n");

	}

	private static void demoZenonArchives(AASComponent i40Component) {
		i40Component.register(AASZenonArchive.ZENON_AAS_ARCHIVES.getId());
		/*
		 * Test/Check the execution of operations
		 */
		i40Component.registerCallback(
				AASZenonArchive.ZENON_AAS_ARCHIVES.getId(),
				AASZenonArchive.ZENON_SUBMODEL_ARCHIVES.getId(),
				"zenonArchive",
				new OperationCallback() {

					@Override
					public boolean execute(OperationInvocation invocation) {
						System.out.println("execute invoked (demoZenonArchives)");


						try {
							System.out.println("Getting zenon archives data...");

							String archive = invocation.getInput("archive", String.class);
							String startTime = trimDateTime(invocation.getInput("startTime", String.class));
							String endTime = trimDateTime(invocation.getInput("endTime", String.class));

							URL webtoolBackend = new URL("http://localhost:5046/archive?archive=" + archive + "&startTime=" + startTime + "&endtime=" + endTime );
							HttpURLConnection con = (HttpURLConnection) webtoolBackend.openConnection();
							con.setRequestMethod("GET");
							System.out.println("Backend response status-code: " + con.getResponseCode());

							BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
							String inputLine;
							StringBuffer response = new StringBuffer();
							while ((inputLine = in.readLine()) != null) {
								//System.out.println(inputLine);
								response.append(inputLine);
							}
							in.close();
							System.out.println("Raw backend response (alarms json):  " + trimString(response.toString(), 150));

							/* Json String to Object */
							String jsonString = response.toString();
							ObjectMapper mapper = new ObjectMapper();
							mapper.registerModule(new JavaTimeModule());
							GraphQLResponse_archives zenonResponse = mapper.readValue(jsonString, GraphQLResponse_archives.class);

							System.out.println(zenonResponse);


							List<ZenonArchive> archives = new ArrayList<>();

							for(int i = 0; i < zenonResponse.getData().getArchiveData().size(); ++i){
								archives.add(zenonResponse.getData().getArchiveData().get(i));
							}

							// ZENON ABFRAGE End
							invocation.setOutput("result", archives);

						} catch (Exception e) {
							e.printStackTrace();
						}

						return true;
					}
				});

		OperationInvocationResult invocation = i40Component
				.getOperationRequest("http://iasset.salzburgresearch.at/zenon/archive")
				.setInput("archive", "OS")
				.setInput("startTime", Instant.now().minusSeconds(60))
				.setInput("endTime", Instant.now())
				// invoke the operation
				.invoke();


		Object objectResult = invocation.getResult("result");
		List<ZenonArchive> archiveList = invocation.getResultList("result", ZenonArchive.class);
		System.out.println(archiveList.size() + " archives read from backend.\r\n");
	}

	private static void demoZenonAlarm(AASComponent i40Component) {
		i40Component.register(AASZenonAlarm.ZENON_AAS_ALARMS.getId());
		/*
		 * Test/Check the execution of operations
		 */
		i40Component.registerCallback(
				AASZenonAlarm.ZENON_AAS_ALARMS.getId(),
				AASZenonAlarm.ZENON_SUBMODEL_ALARMS.getId(),
				"zenonAlarm",
				new OperationCallback() {

					@Override
					public boolean execute(OperationInvocation invocation) {
						System.out.println("execute invoked (demoZenonAlarm)");


						try {
							System.out.println("Getting zenon alarm data...");

							String timeString = invocation.getInput("timeTo", String.class);

							// Right now, can pass seconds in 'timeTo' parameter.
							// Alarms will be extracted between time of request and 'timeTo'.
							// Adjustment would have to also be made in backend (see URL).
							Instant timeToTime = Instant.parse(timeString);
							Instant currentTime = Instant.now();
							long secondsDifference = ChronoUnit.SECONDS.between(timeToTime, currentTime);
							int fromSeconds = Math.abs(Math.toIntExact(secondsDifference));  // use when not testing

							URL webtoolBackend = new URL("http://localhost:5046/alarmdata?fromSeconds=" + fromSeconds);
							HttpURLConnection con = (HttpURLConnection) webtoolBackend.openConnection();
							con.setRequestMethod("GET");
							System.out.println("backend response status-code: " + con.getResponseCode());

							BufferedReader in = new BufferedReader(
									new InputStreamReader(con.getInputStream()));
							String inputLine;
							StringBuffer response = new StringBuffer();
							while ((inputLine = in.readLine()) != null) {
								//System.out.println(inputLine);
								response.append(inputLine);
							}
							in.close();
							System.out.println("Raw backend response (alarms json):  " + trimString(response.toString(), 150));

							/* Json String to Object */
							String jsonString = response.toString();
							ObjectMapper mapper = new ObjectMapper();
							mapper.registerModule(new JavaTimeModule());
							GraphQLResponse_alarmData zenonResponse = mapper.readValue(jsonString, GraphQLResponse_alarmData.class);
							//System.out.println(zenonResponse);



							// Extracted alarms to objects
							List<ZenonAlarm> alarme = new ArrayList<>();

							for(int i = 0; i < zenonResponse.getData().getAlarmData().size(); ++i){
								ZenonAlarm response_AlarmData  = zenonResponse.getData().getAlarmData().get(i);

								// not really needed - see demoZenonArchives()
								String alarmData_Variable = response_AlarmData.getVariable();
								String alarmData_AlarmText = response_AlarmData.getAlarmText();
								String alarmData_AlarmGroup = response_AlarmData.getAlarmGroup();
								String alarmData_AlarmClass  = response_AlarmData.getAlarmClass();
								Instant alarmData_TimeComes = response_AlarmData.getTimeComes();
								Instant alarmData_TimeGoes = response_AlarmData.getTimeGoes();

								ZenonAlarm alarmObj = new ZenonAlarm();
								alarmObj.setVariable(alarmData_Variable);
								alarmObj.setAlarmClass(alarmData_AlarmClass);
								alarmObj.setAlarmGroup(alarmData_AlarmGroup);
								alarmObj.setAlarmText(alarmData_AlarmText);
								alarmObj.setTimeComes(alarmData_TimeComes);
								alarmObj.setTimeGoes(alarmData_TimeGoes);

								// success
								alarme.add(alarmObj);

							}
							// ZENON ABFRAGE End
							invocation.setOutput("result", alarme);

						} catch (Exception e) {
							System.out.print("Exception in demoZenonAlarm: ");
							e.printStackTrace();
						}

						return true;
					}
				});

		OperationInvocationResult invocation = i40Component
				.getOperationRequest("http://iasset.salzburgresearch.at/zenon/alarm")
				.setInput("timeFrom", Instant.now().minusSeconds(10))  // not used currently
				.setInput("timeTo", Instant.now().minusSeconds(5))  // alarms from the last 5 seconds
				// invoke the operation
				.invoke();


		Object objectResult = invocation.getResult("result");
		List<ZenonAlarm> alarmList = invocation.getResultList("result", ZenonAlarm.class);
		System.out.println(alarmList.size() + " alarms read from backend.\r\n");
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
			
			EventProducer<Fault> faultProducer = i40Component.getEventProducer("http://iasset.salzburgresearch.at/semantic/fault", Fault.class);
			Thread.sleep(5000);
			Fault theFault = new Fault();
			theFault.setAssetId(AASFull.AAS_BELT_INSTANCE.getId());
			theFault.setFaultId(AASFull.SUBMODEL_BELT_EVENT_INSTANCE.getId());
			theFault.setShortText("This is a short description!");
			faultProducer.sendEvent(theFault);
		
		} catch (MessagingException | InterruptedException e) {
			// show error messages
			e.printStackTrace();
		}

	}
}