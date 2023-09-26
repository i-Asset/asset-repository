package at.srfg.iasset.connector.environment;


import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.eclipse.aas4j.v3.model.Operation;
import org.eclipse.aas4j.v3.model.OperationVariable;
import org.eclipse.aas4j.v3.model.Property;
import org.eclipse.aas4j.v3.model.Submodel;
import org.eclipse.aas4j.v3.model.SubmodelElement;
import org.eclipse.aas4j.v3.model.SubmodelElementCollection;
import org.eclipse.aas4j.v3.model.SubmodelElementList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.repository.config.AASModelHelper;
import at.srfg.iasset.repository.connectivity.rest.ClientFactory;
import at.srfg.iasset.repository.model.AASPlantStructureSubmodel;
import at.srfg.iasset.repository.model.InvocationRequest;
import at.srfg.iasset.repository.model.InvocationResult;
import at.srfg.iasset.repository.model.PlantElement;
import at.srfg.iasset.repository.model.helper.ValueHelper;
import at.srfg.iasset.repository.model.operation.OperationRequest;
import at.srfg.iasset.repository.model.operation.OperationRequestValue;
import at.srfg.iasset.repository.model.operation.OperationResultValue;
import at.srfg.iasset.repository.utils.SubmodelUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestOperations {
	
	private ObjectMapper objectMapper = ClientFactory.getObjectMapper();

	@Test
	public void testOperationRequest() throws JsonProcessingException {
		Submodel submodel = AASPlantStructureSubmodel.SUBMODEL_PLANT_STRUCTURE_REQUEST_OPERATION;
		Optional<Operation> op = SubmodelUtils.getSubmodelElementAt(submodel, "getPlantStructure",Operation.class);
		if ( op.isPresent()) {
			//
			String dateTimeString = Instant.now().toString();
			String string = UUID.randomUUID().toString();
			String intVal = Integer.valueOf(new Random(10).nextInt()).toString();
			String doubleVal = Double.valueOf(new Random(100).nextDouble()).toString();
			
			//
			Operation operation = op.get();
			InvocationRequest<OperationVariable> request = new OperationRequest();
			InvocationRequest<Object> valueRequest = new OperationRequestValue();
			// add default values
			for ( OperationVariable ov : operation.getInputVariables() ) {
				if ( ov.getValue() instanceof Property) {
					Property inputParam = (Property) ov.getValue();
					
					switch(inputParam.getValueType()) {
					case DATE_TIME:
						inputParam.setValue(dateTimeString);
						break;
					case STRING:
						inputParam.setValue(string);
						break;
					case INTEGER:
						inputParam.setValue(intVal);
						break;
					case DOUBLE:
						inputParam.setValue(doubleVal);
						break;
					case BOOLEAN:
						inputParam.setValue(Boolean.TRUE.toString());
						break;
					default:
					}
					request.inputArgument(ov);
					valueRequest.inputArgument(ValueHelper.toValue(ov.getValue()));
				}
				if (ov.getValue() instanceof SubmodelElementCollection ) {
					SubmodelElementCollection c = (SubmodelElementCollection) ov.getValue();
					for (SubmodelElement e : c.getValues() ) {
						if (e instanceof Property) {
							Property p = (Property) e;
							switch(p.getIdShort()) {
							case "name": 
								p.setValue("Demo Name");
								break;
							case "description":
								p.setValue("Demo Description");
							}
						}
						if ( e instanceof SubmodelElementList ) {
							SubmodelElementList aList = (SubmodelElementList)e;
							if ( aList.getValueTypeListElement() != null ) {
								Property listElement = AASModelHelper.newElementInstance(Property.class);
								listElement.setValueType(aList.getValueTypeListElement());
								listElement.setValue("Sample");
								listElement.setIdShort(aList.getIdShort()+"(1)");
								aList.getValues().add(listElement);
							}
						}
					}
					request.inputArgument(ov);
					valueRequest.inputArgument(ValueHelper.toValue(ov.getValue()));
				}
			}
			// add default values
			for ( OperationVariable ov : operation.getInoutputVariables() ) {
				if ( ov.getValue() instanceof Property) {
					Property inputParam = (Property) ov.getValue();
					
					switch(inputParam.getValueType()) {
					case DATE_TIME:
						inputParam.setValue(dateTimeString);
						break;
					case STRING:
						inputParam.setValue(string);
						break;
					case INTEGER:
						inputParam.setValue(intVal);
						break;
					case DOUBLE:
						inputParam.setValue(doubleVal);
						break;
					case BOOLEAN:
						inputParam.setValue(Boolean.TRUE.toString());
						break;
					default:
					}
					request.inoutputArgument(ov);
					valueRequest.inoutputArgument(ValueHelper.toValue(ov.getValue()));
				}
			}
			
			String output = objectMapper.writeValueAsString(request);
			System.out.println(output);
			String valueOutput = objectMapper.writeValueAsString(valueRequest);
			System.out.println(valueOutput);
			
			for ( OperationVariable ov : operation.getInputVariables() ) {
				if ( ov.getValue() instanceof Property) {
					Property inputParam = (Property) ov.getValue();
					inputParam.setValue(null);
				}
			}
			// add default values
			for ( OperationVariable ov : operation.getInoutputVariables() ) {
				if ( ov.getValue() instanceof Property) {
					Property inputParam = (Property) ov.getValue();
					inputParam.setValue(null);
				}
			}

			OperationRequestValue value2 = objectMapper.readValue(valueOutput, OperationRequestValue.class);
			
			for (int i = 0; i < value2.getInputArguments().size(); i++) {
				JsonNode fromObject = objectMapper.convertValue(value2.getInputArgument(i), JsonNode.class);
				OperationVariable element = operation.getInputVariables().get(i);
				
				ValueHelper.applyValue(element.getValue(), fromObject);
			}
			for ( OperationVariable ov : operation.getInputVariables() ) {
				if ( ov.getValue() instanceof Property) {
					Property inputParam = (Property) ov.getValue();
					
					switch(inputParam.getValueType()) {
					case DATE_TIME:
						assertTrue(inputParam.getValue().equals(dateTimeString));
						break;
					case STRING:
						assertTrue(inputParam.getValue().equals(string));
						break;
					case INTEGER:
						assertTrue(inputParam.getValue().equals(intVal));
						break;
					case DOUBLE:
						assertTrue(inputParam.getValue().equals(doubleVal));
						break;
					case BOOLEAN:
						inputParam.setValue(Boolean.TRUE.toString());
						break;
					default:
					}
				}
			}
			// add default values
			for ( OperationVariable ov : operation.getInoutputVariables() ) {
				if ( ov.getValue() instanceof Property) {
					Property inputParam = (Property) ov.getValue();
					
					switch(inputParam.getValueType()) {
					case DATE_TIME:
						assertTrue(inputParam.getValue().equals(dateTimeString));
						break;
					case STRING:
						assertTrue(inputParam.getValue().equals(string));
						break;
					case INTEGER:
						assertTrue(inputParam.getValue().equals(intVal));
						break;
					case DOUBLE:
						assertTrue(inputParam.getValue().equals(doubleVal));
						break;
					case BOOLEAN:
						inputParam.setValue(Boolean.TRUE.toString());
						break;
					default:
					}
				}
			}
		}
		
	}
	@Test
	public void testOperationResult() throws JsonProcessingException {
		
		Submodel submodel = AASPlantStructureSubmodel.SUBMODEL_PLANT_STRUCTURE_REQUEST_OPERATION;
		Optional<Operation> op = SubmodelUtils.getSubmodelElementAt(submodel, "getPlantStructure",Operation.class);
		if ( op.isPresent()) {
			Operation operation = op.get();
			InvocationResult<Object> valueResult = new OperationResultValue();
			List<PlantElement> plants = new ArrayList<>();
			PlantElement plant = new PlantElement();
			plant.setName("demoName");
			plant.setDescription("My Plant");
			plant.setIdentifiers(Collections.singletonList("identifier"));
			plants.add(plant);

			valueResult.getOutputArguments().add(plants);
			
			String valueOut = objectMapper.writeValueAsString(valueResult);
			System.out.println(valueOut);
			
			OperationResultValue mappedResult = objectMapper.readValue(valueOut, OperationResultValue.class);
			mappedResult.getOutputArguments().get(0);
			
		}
		
	}

	
}
