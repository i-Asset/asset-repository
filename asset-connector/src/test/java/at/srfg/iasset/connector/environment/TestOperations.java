package at.srfg.iasset.connector.environment;


import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.eclipse.digitaltwin.aas4j.v3.model.Operation;
import org.eclipse.digitaltwin.aas4j.v3.model.OperationRequest;
import org.eclipse.digitaltwin.aas4j.v3.model.OperationRequestValue;
import org.eclipse.digitaltwin.aas4j.v3.model.OperationResultValue;
import org.eclipse.digitaltwin.aas4j.v3.model.OperationVariable;
import org.eclipse.digitaltwin.aas4j.v3.model.Property;
import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementCollection;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElementList;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultOperationRequest;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultOperationRequestValue;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultOperationResultValue;
import org.junit.jupiter.api.Test;

import at.srfg.iasset.repository.config.AASModelHelper;
import at.srfg.iasset.repository.connectivity.rest.ClientFactory;
import at.srfg.iasset.repository.model.AASPlantStructureSubmodel;
import at.srfg.iasset.repository.model.PlantElement;
import at.srfg.iasset.repository.model.helper.ValueHelper;
import at.srfg.iasset.repository.model.helper.value.exception.ValueMappingException;
import at.srfg.iasset.repository.utils.SubmodelUtils;

public class TestOperations {
	
	private ObjectMapper objectMapper = ClientFactory.getObjectMapper();

	@Test
	public void testOperationRequest() throws JsonProcessingException, ValueMappingException {
		Submodel submodel = AASPlantStructureSubmodel.SUBMODEL_PLANT_STRUCTURE_REQUEST_OPERATION;
		Optional<Operation> op = SubmodelUtils.getSubmodelElementAt(submodel, "getPlantStructure",Operation.class);
		if ( op.isPresent()) {
			//
			String dateTimeString = LocalDateTime.now().toString();
			String string = UUID.randomUUID().toString();
			String intVal = Integer.valueOf(new Random(10).nextInt()).toString();
			String doubleVal = Double.valueOf(new Random(100).nextDouble()).toString();
			
			//
			Operation operation = op.get();
			OperationRequest request = new DefaultOperationRequest.Builder().build();
			OperationRequestValue valueRequest = new DefaultOperationRequestValue.Builder().build();
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
					request.getInputArguments().add(ov);
					valueRequest.getInputArguments().put(ov.getValue().getIdShort(), ValueHelper.toValue(ov.getValue()));
				}
				if (ov.getValue() instanceof SubmodelElementCollection ) {
					SubmodelElementCollection c = (SubmodelElementCollection) ov.getValue();
					for (SubmodelElement e : c.getValue() ) {
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
								aList.getValue().add(listElement);
							}
						}
					}
					request.getInputArguments().add(ov);
					valueRequest.getInputArguments().put(ov.getValue().getIdShort(), ValueHelper.toValue(ov.getValue()));
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
					request.getInputArguments().add(ov);
					valueRequest.getInputArguments().put(ov.getValue().getIdShort(), ValueHelper.toValue(ov.getValue()));
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
			for ( String key : value2.getInputArguments().keySet()) {
				JsonNode fromObject = objectMapper.convertValue(value2.getInputArguments().get(key), JsonNode.class);
				Optional<OperationVariable> element = operation.getInputVariables().stream().filter(new Predicate<OperationVariable>() {

					@Override
					public boolean test(OperationVariable t) {
						// TODO Auto-generated method stub
						return t.getValue().getIdShort().equals(key);
					}}).findFirst();
				if ( element.isPresent() ) {
					ValueHelper.applyValue(element.get().getValue(), fromObject);
				}
				
			}
			for (int i = 0; i < value2.getInputArguments().size(); i++) {
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
			OperationResultValue valueResult = new DefaultOperationResultValue.Builder().build();
			
			List<PlantElement> plants = new ArrayList<>();
			PlantElement plant = new PlantElement();
			plant.setName("demoName");
			plant.setDescription("My Plant");
			plant.setIdentifiers(Collections.singletonList("identifier"));
			plants.add(plant);

			valueResult.getOutputArguments().put("plantStructure", plants);
			
			String valueOut = objectMapper.writeValueAsString(valueResult);
			System.out.println(valueOut);
			
			OperationResultValue mappedResult = objectMapper.readValue(valueOut, OperationResultValue.class);
			mappedResult.getOutputArguments().get(0);
			
		}
		
	}

	
}
