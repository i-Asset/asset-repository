package at.srfg.iasset.repository.api;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.eclipse.digitaltwin.aas4j.v3.model.ConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.DataSpecificationContent;
import org.eclipse.digitaltwin.aas4j.v3.model.DataTypeIec61360;
import org.eclipse.digitaltwin.aas4j.v3.model.KeyTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.LevelType;
import org.eclipse.digitaltwin.aas4j.v3.model.ReferenceTypes;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultConceptDescription;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultDataSpecificationIec61360;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultEmbeddedDataSpecification;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultExternalReference;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultKey;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultLangString;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultValueList;
import org.eclipse.digitaltwin.aas4j.v3.model.impl.DefaultValueReferencePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.repository.api.model.Result;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
public class ConceptDescriptionsApiController implements ConceptDescriptionsApi {

	private static final Logger log = LoggerFactory.getLogger(ConceptDescriptionsApiController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@org.springframework.beans.factory.annotation.Autowired
	public ConceptDescriptionsApiController(ObjectMapper aasMapper, HttpServletRequest request) {
		this.objectMapper = aasMapper;
		this.request = request;
	}

	public ResponseEntity<Result> deleteConceptDescriptionById(
			@Parameter(in = ParameterIn.PATH, description = "The Concept Description’s unique id (UTF8-BASE64-URL-encoded)", required = true, schema = @Schema()) @PathVariable("cdIdentifier") String cdIdentifier) {
		String accept = request.getHeader("Accept");
		Result result = new Result().success(Boolean.TRUE);
		return new ResponseEntity<Result>(result, HttpStatus.OK);
	}

	public ResponseEntity<List<ConceptDescription>> getAllConceptDescriptions(
			@Parameter(in = ParameterIn.QUERY, description = "The Concept Description’s IdShort", schema = @Schema()) @Valid @RequestParam(value = "idShort", required = false) String idShort,

			@Parameter(in = ParameterIn.QUERY, description = "IsCaseOf reference (UTF8-BASE64-URL-encoded)", schema = @Schema()) @Valid @RequestParam(value = "isCaseOf", required = false) String isCaseOf,

			@Parameter(in = ParameterIn.QUERY, description = "DataSpecification reference (UTF8-BASE64-URL-encoded)", schema = @Schema()) @Valid @RequestParam(value = "dataSpecificationRef", required = false) String dataSpecificationRef) {
		//
		idShort = (idShort == null ? null : new String(Base64.getDecoder().decode(idShort)));
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			ConceptDescription cDesk = new DefaultConceptDescription.Builder()

					.category("PARAMETER").id("http://myConceptIdentifier.org/id").idShort(idShort)
					.displayName(new DefaultLangString.Builder().language("de").text("ConceptDescription").build())
					.isCaseOf(
							new DefaultExternalReference.Builder()
									.key(new DefaultKey.Builder().type(KeyTypes.GLOBAL_REFERENCE)
											.value("0173-1#01-AFW236#003").build())
									.build())
					.embeddedDataSpecification(new DefaultEmbeddedDataSpecification.Builder()
							.dataSpecification(new DefaultExternalReference.Builder()
									.key(new DefaultKey.Builder().type(KeyTypes.GLOBAL_REFERENCE)
											.value("http://semanticLookup/classification").build())
									.build())
							.dataSpecificationContent(new DefaultDataSpecificationIec61360.Builder()
									.preferredNames(Arrays.asList(
											new DefaultLangString.Builder().text("Test Specification").language("de")
													.build(),
											new DefaultLangString.Builder()
													.text("TestSpecification").language("en-us").build()))
									.dataType(DataTypeIec61360.REAL_MEASURE)
									.definition(new DefaultLangString.Builder()
											.text("Dies ist eine Data Specification für Testzwecke").language("de")
											.build())
									.definition(new DefaultLangString.Builder()
											.text("This is a DataSpecification for testing purposes").language("en-us")
											.build())
									.shortName(new DefaultLangString.Builder().text("Test Spec").language("de").build())
									.shortName(
											new DefaultLangString.Builder().text("TestSpec").language("en-us").build())
									.unit("SpaceUnit")
									.unitId(new DefaultExternalReference.Builder()
											.key(new DefaultKey.Builder()
													.type(KeyTypes.GLOBAL_REFERENCE)
													.value("http://acplt.org/Units/SpaceUnit").build())
											.build())
									.sourceOfDefinition("http://acplt.org/DataSpec/ExampleDef").symbol("SU")
									.valueFormat("string").value("TEST").levelType(LevelType.MIN)
//                                    .levelType(LevelType.MAX)
									.valueList(new DefaultValueList.Builder()
											.valueReferencePair(new DefaultValueReferencePair.Builder()
													.value("http://acplt.org/ValueId/ExampleValueId")
													.valueId(new DefaultExternalReference.Builder()
															.key(new DefaultKey.Builder()
																	.type(KeyTypes.GLOBAL_REFERENCE)
																	.value("http://acplt.org/ValueId/ExampleValueId")
																	.build())
															.build())
													// TODO valueType
													.build())
											.valueReferencePair(new DefaultValueReferencePair.Builder()
													.value("http://acplt.org/ValueId/ExampleValueId2")
													.valueId(new DefaultExternalReference.Builder()
															.key(new DefaultKey.Builder()
																	.type(KeyTypes.GLOBAL_REFERENCE)
																	.value("http://acplt.org/ValueId/ExampleValueId2")
																	.build())
															.build())
													// TODO valueType
													.build())
											.build())
									.build())
							.build())
					.build();

			return new ResponseEntity<List<ConceptDescription>>(Arrays.asList(cDesk), HttpStatus.OK);
		}

		return new ResponseEntity<List<ConceptDescription>>(HttpStatus.NOT_IMPLEMENTED);
	}

	public class MyCustomSpec implements DataSpecificationContent {

	}

	public ResponseEntity<ConceptDescription> getConceptDescriptionById(
			@Parameter(in = ParameterIn.PATH, description = "The Concept Description’s unique id (UTF8-BASE64-URL-encoded)", required = true, schema = @Schema()) @PathVariable("cdIdentifier") String cdIdentifier) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try {
				return new ResponseEntity<ConceptDescription>(objectMapper.readValue("\"\"", ConceptDescription.class),
						HttpStatus.NOT_IMPLEMENTED);
			} catch (IOException e) {
				log.error("Couldn't serialize response for content type application/json", e);
				return new ResponseEntity<ConceptDescription>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<ConceptDescription>(HttpStatus.NOT_IMPLEMENTED);
	}

	public ResponseEntity<ConceptDescription> postConceptDescription(
			@Parameter(in = ParameterIn.DEFAULT, description = "Concept Description object", required = true, schema = @Schema()) @Valid @RequestBody ConceptDescription body) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try {
				return new ResponseEntity<ConceptDescription>(objectMapper.readValue("\"\"", ConceptDescription.class),
						HttpStatus.NOT_IMPLEMENTED);
			} catch (IOException e) {
				log.error("Couldn't serialize response for content type application/json", e);
				return new ResponseEntity<ConceptDescription>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<ConceptDescription>(HttpStatus.NOT_IMPLEMENTED);
	}

	public ResponseEntity<Void> putConceptDescriptionById(
			@Parameter(in = ParameterIn.PATH, description = "The Concept Description’s unique id (UTF8-BASE64-URL-encoded)", required = true, schema = @Schema()) @PathVariable("cdIdentifier") String cdIdentifier,
			@Parameter(in = ParameterIn.DEFAULT, description = "Concept Description object", required = true, schema = @Schema()) @Valid @RequestBody ConceptDescription body) {
		String accept = request.getHeader("Accept");
		return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
	}

}
