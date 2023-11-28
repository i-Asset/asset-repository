package at.srfg.iasset.repository.api;

import java.io.IOException;
import java.util.List;

import org.eclipse.digitaltwin.aas4j.v3.model.Submodel;
import org.eclipse.digitaltwin.aas4j.v3.model.SubmodelElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.srfg.iasset.repository.model.operation.OperationRequest;
import at.srfg.iasset.repository.model.operation.OperationResult;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@jakarta.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-08-06T18:15:09.134Z[GMT]")
@RestController
public class SubmodelsApiController implements SubmodelsApi {

    private static final Logger log = LoggerFactory.getLogger(SubmodelsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public SubmodelsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Void> deleteSubmodelById(@Parameter(in = ParameterIn.PATH, description = "The Submodel’s unique id (UTF8-BASE64-URL-encoded)", required=true, schema=@Schema()) @PathVariable("submodelIdentifier") String submodelIdentifier) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> deleteSubmodelElementByPathSubmodelRepo(@Parameter(in = ParameterIn.PATH, description = "The Submodel’s unique id (UTF8-BASE64-URL-encoded)", required=true, schema=@Schema()) @PathVariable("submodelIdentifier") String submodelIdentifier,@Parameter(in = ParameterIn.PATH, description = "IdShort path to the submodel element (dot-separated)", required=true, schema=@Schema()) @PathVariable("idShortPath") String idShortPath) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<SubmodelElement>> getAllSubmodelElementsSubmodelRepo(@Parameter(in = ParameterIn.PATH, description = "The Submodel’s unique id (UTF8-BASE64-URL-encoded)", required=true, schema=@Schema()) @PathVariable("submodelIdentifier") String submodelIdentifier,@Parameter(in = ParameterIn.QUERY, description = "Determines the structural depth of the respective resource content" ,schema=@Schema(allowableValues={ "deep", "core" }
, defaultValue="deep")) @Valid @RequestParam(value = "level", required = false, defaultValue="deep") String level,@Parameter(in = ParameterIn.QUERY, description = "Determines the request or response kind of the resource" ,schema=@Schema(allowableValues={ "normal", "metadata", "value", "reference", "path" }
, defaultValue="normal")) @Valid @RequestParam(value = "content", required = false, defaultValue="normal") String content,@Parameter(in = ParameterIn.QUERY, description = "Determines to which extent the resource is being serialized" ,schema=@Schema(allowableValues={ "withBlobValue", "withoutBlobValue" }
)) @Valid @RequestParam(value = "extent", required = false) String extent) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<SubmodelElement>>(objectMapper.readValue("[ \"\", \"\" ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<SubmodelElement>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<SubmodelElement>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<Submodel>> getAllSubmodels(@Parameter(in = ParameterIn.QUERY, description = "The value of the semantic id reference (BASE64-URL-encoded)" ,schema=@Schema()) @Valid @RequestParam(value = "semanticId", required = false) String semanticId,@Parameter(in = ParameterIn.QUERY, description = "The Submodel’s idShort" ,schema=@Schema()) @Valid @RequestParam(value = "idShort", required = false) String idShort) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Submodel>>(objectMapper.readValue("[ \"\", \"\" ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Submodel>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Submodel>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Resource> getFileByPathSubmodelRepo(@Parameter(in = ParameterIn.PATH, description = "The Submodel’s unique id (UTF8-BASE64-URL-encoded)", required=true, schema=@Schema()) @PathVariable("submodelIdentifier") String submodelIdentifier,@Parameter(in = ParameterIn.PATH, description = "IdShort path to the submodel element (dot-separated)", required=true, schema=@Schema()) @PathVariable("idShortPath") String idShortPath) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Resource>(objectMapper.readValue("\"\"", Resource.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Resource>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Resource>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<OperationResult> getOperationAsyncResultSubmodelRepo(@Parameter(in = ParameterIn.PATH, description = "The Submodel’s unique id (UTF8-BASE64-URL-encoded)", required=true, schema=@Schema()) @PathVariable("submodelIdentifier") String submodelIdentifier,@Parameter(in = ParameterIn.PATH, description = "IdShort path to the submodel element (dot-separated), in this case an operation", required=true, schema=@Schema()) @PathVariable("idShortPath") String idShortPath,@Parameter(in = ParameterIn.PATH, description = "The returned handle id of an operation’s asynchronous invocation used to request the current state of the operation’s execution (UTF8-BASE64-URL-encoded)", required=true, schema=@Schema()) @PathVariable("handleId") String handleId,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema(allowableValues={ "normal", "value" }
, defaultValue="normal")) @Valid @RequestParam(value = "content", required = false, defaultValue="normal") String content) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<OperationResult>(objectMapper.readValue("{\n  \"outputArguments\" : [ null, null ],\n  \"requestId\" : \"requestId\",\n  \"executionResult\" : {\n    \"success\" : true,\n    \"messages\" : [ {\n      \"code\" : \"code\",\n      \"messageType\" : \"Undefined\",\n      \"text\" : \"text\",\n      \"timestamp\" : \"timestamp\"\n    }, {\n      \"code\" : \"code\",\n      \"messageType\" : \"Undefined\",\n      \"text\" : \"text\",\n      \"timestamp\" : \"timestamp\"\n    } ]\n  },\n  \"executionState\" : \"Initiated\",\n  \"inoutputArguments\" : [ {\n    \"value\" : \"\"\n  }, {\n    \"value\" : \"\"\n  } ]\n}", OperationResult.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<OperationResult>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<OperationResult>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Submodel> getSubmodelById(@Parameter(in = ParameterIn.PATH, description = "The Submodel’s unique id (UTF8-BASE64-URL-encoded)", required=true, schema=@Schema()) @PathVariable("submodelIdentifier") String submodelIdentifier) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Submodel>(objectMapper.readValue("\"\"", Submodel.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Submodel>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Submodel>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<SubmodelElement> getSubmodelElementByPathSubmodelRepo(@Parameter(in = ParameterIn.PATH, description = "The Submodel’s unique id (UTF8-BASE64-URL-encoded)", required=true, schema=@Schema()) @PathVariable("submodelIdentifier") String submodelIdentifier,@Parameter(in = ParameterIn.PATH, description = "IdShort path to the submodel element (dot-separated)", required=true, schema=@Schema()) @PathVariable("idShortPath") String idShortPath,@Parameter(in = ParameterIn.QUERY, description = "Determines the structural depth of the respective resource content" ,schema=@Schema(allowableValues={ "deep", "core" }
, defaultValue="deep")) @Valid @RequestParam(value = "level", required = false, defaultValue="deep") String level,@Parameter(in = ParameterIn.QUERY, description = "Determines the request or response kind of the resource" ,schema=@Schema(allowableValues={ "normal", "metadata", "value", "reference", "path" }
, defaultValue="normal")) @Valid @RequestParam(value = "content", required = false, defaultValue="normal") String content,@Parameter(in = ParameterIn.QUERY, description = "Determines to which extent the resource is being serialized" ,schema=@Schema(allowableValues={ "withBlobValue", "withoutBlobValue" }
)) @Valid @RequestParam(value = "extent", required = false) String extent) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<SubmodelElement>(objectMapper.readValue("\"\"", SubmodelElement.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<SubmodelElement>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<SubmodelElement>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<OperationResult> invokeOperationSubmodelRepo(@Parameter(in = ParameterIn.PATH, description = "The Submodel’s unique id (UTF8-BASE64-URL-encoded)", required=true, schema=@Schema()) @PathVariable("submodelIdentifier") String submodelIdentifier,@Parameter(in = ParameterIn.PATH, description = "IdShort path to the submodel element (dot-separated), in this case an operation", required=true, schema=@Schema()) @PathVariable("idShortPath") String idShortPath,@Parameter(in = ParameterIn.DEFAULT, description = "Operation request object", required=true, schema=@Schema()) @Valid @RequestBody OperationRequest body,@Parameter(in = ParameterIn.QUERY, description = "Determines whether an operation invocation is performed asynchronously or synchronously" ,schema=@Schema( defaultValue="false")) @Valid @RequestParam(value = "async", required = false, defaultValue="false") Boolean async,@Parameter(in = ParameterIn.QUERY, description = "Determines the request or response kind of the resource" ,schema=@Schema(allowableValues={ "normal", "metadata", "value", "reference", "path" }
, defaultValue="normal")) @Valid @RequestParam(value = "content", required = false, defaultValue="normal") String content) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<OperationResult>(objectMapper.readValue("{\n  \"outputArguments\" : [ null, null ],\n  \"requestId\" : \"requestId\",\n  \"executionResult\" : {\n    \"success\" : true,\n    \"messages\" : [ {\n      \"code\" : \"code\",\n      \"messageType\" : \"Undefined\",\n      \"text\" : \"text\",\n      \"timestamp\" : \"timestamp\"\n    }, {\n      \"code\" : \"code\",\n      \"messageType\" : \"Undefined\",\n      \"text\" : \"text\",\n      \"timestamp\" : \"timestamp\"\n    } ]\n  },\n  \"executionState\" : \"Initiated\",\n  \"inoutputArguments\" : [ {\n    \"value\" : \"\"\n  }, {\n    \"value\" : \"\"\n  } ]\n}", OperationResult.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<OperationResult>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<OperationResult>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Submodel> postSubmodel(@Parameter(in = ParameterIn.DEFAULT, description = "Submodel object", required=true, schema=@Schema()) @Valid @RequestBody Submodel body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Submodel>(objectMapper.readValue("\"\"", Submodel.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Submodel>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Submodel>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<SubmodelElement> postSubmodelElementByPathSubmodelRepo(@Parameter(in = ParameterIn.PATH, description = "The Submodel’s unique id (UTF8-BASE64-URL-encoded)", required=true, schema=@Schema()) @PathVariable("submodelIdentifier") String submodelIdentifier,@Parameter(in = ParameterIn.PATH, description = "IdShort path to the submodel element (dot-separated)", required=true, schema=@Schema()) @PathVariable("idShortPath") String idShortPath,@Parameter(in = ParameterIn.DEFAULT, description = "Requested submodel element", required=true, schema=@Schema()) @Valid @RequestBody SubmodelElement body,@Parameter(in = ParameterIn.QUERY, description = "Determines the structural depth of the respective resource content" ,schema=@Schema(allowableValues={ "deep", "core" }
, defaultValue="deep")) @Valid @RequestParam(value = "level", required = false, defaultValue="deep") String level,@Parameter(in = ParameterIn.QUERY, description = "Determines the request or response kind of the resource" ,schema=@Schema(allowableValues={ "normal", "metadata", "value", "reference", "path" }
, defaultValue="normal")) @Valid @RequestParam(value = "content", required = false, defaultValue="normal") String content,@Parameter(in = ParameterIn.QUERY, description = "Determines to which extent the resource is being serialized" ,schema=@Schema(allowableValues={ "withBlobValue", "withoutBlobValue" }
)) @Valid @RequestParam(value = "extent", required = false) String extent) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<SubmodelElement>(objectMapper.readValue("\"\"", SubmodelElement.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<SubmodelElement>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<SubmodelElement>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<SubmodelElement> postSubmodelElementSubmodelRepo(@Parameter(in = ParameterIn.PATH, description = "The Submodel’s unique id (UTF8-BASE64-URL-encoded)", required=true, schema=@Schema()) @PathVariable("submodelIdentifier") String submodelIdentifier,@Parameter(in = ParameterIn.DEFAULT, description = "Requested submodel element", required=true, schema=@Schema()) @Valid @RequestBody SubmodelElement body,@Parameter(in = ParameterIn.QUERY, description = "Determines the structural depth of the respective resource content" ,schema=@Schema(allowableValues={ "deep", "core" }
, defaultValue="deep")) @Valid @RequestParam(value = "level", required = false, defaultValue="deep") String level,@Parameter(in = ParameterIn.QUERY, description = "Determines the request or response kind of the resource" ,schema=@Schema(allowableValues={ "normal", "metadata", "value", "reference", "path" }
, defaultValue="normal")) @Valid @RequestParam(value = "content", required = false, defaultValue="normal") String content,@Parameter(in = ParameterIn.QUERY, description = "Determines to which extent the resource is being serialized" ,schema=@Schema(allowableValues={ "withBlobValue", "withoutBlobValue" }
)) @Valid @RequestParam(value = "extent", required = false) String extent) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<SubmodelElement>(objectMapper.readValue("\"\"", SubmodelElement.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<SubmodelElement>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<SubmodelElement>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> putFileByPathSubmodelRepo(@Parameter(in = ParameterIn.PATH, description = "The Submodel’s unique id (UTF8-BASE64-URL-encoded)", required=true, schema=@Schema()) @PathVariable("submodelIdentifier") String submodelIdentifier,@Parameter(in = ParameterIn.PATH, description = "IdShort path to the submodel element (dot-separated)", required=true, schema=@Schema()) @PathVariable("idShortPath") String idShortPath,@Parameter(in = ParameterIn.DEFAULT, description = "", required=true,schema=@Schema()) @RequestParam(value="fileName", required=true)  String fileName,@Parameter(description = "file detail") @Valid @RequestPart("file") MultipartFile file) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> putSubmodelById(@Parameter(in = ParameterIn.PATH, description = "The Submodel’s unique id (UTF8-BASE64-URL-encoded)", required=true, schema=@Schema()) @PathVariable("submodelIdentifier") String submodelIdentifier,@Parameter(in = ParameterIn.DEFAULT, description = "Submodel object", required=true, schema=@Schema()) @Valid @RequestBody Submodel body) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> putSubmodelElementByPathSubmodelRepo(@Parameter(in = ParameterIn.PATH, description = "The Submodel’s unique id (UTF8-BASE64-URL-encoded)", required=true, schema=@Schema()) @PathVariable("submodelIdentifier") String submodelIdentifier,@Parameter(in = ParameterIn.PATH, description = "IdShort path to the submodel element (dot-separated)", required=true, schema=@Schema()) @PathVariable("idShortPath") String idShortPath,@Parameter(in = ParameterIn.DEFAULT, description = "Requested submodel element", required=true, schema=@Schema()) @Valid @RequestBody SubmodelElement body,@Parameter(in = ParameterIn.QUERY, description = "Determines the structural depth of the respective resource content" ,schema=@Schema(allowableValues={ "deep", "core" }
, defaultValue="deep")) @Valid @RequestParam(value = "level", required = false, defaultValue="deep") String level,@Parameter(in = ParameterIn.QUERY, description = "Determines the request or response kind of the resource" ,schema=@Schema(allowableValues={ "normal", "metadata", "value", "reference", "path" }
, defaultValue="normal")) @Valid @RequestParam(value = "content", required = false, defaultValue="normal") String content,@Parameter(in = ParameterIn.QUERY, description = "Determines to which extent the resource is being serialized" ,schema=@Schema(allowableValues={ "withBlobValue", "withoutBlobValue" }
)) @Valid @RequestParam(value = "extent", required = false) String extent) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

}
