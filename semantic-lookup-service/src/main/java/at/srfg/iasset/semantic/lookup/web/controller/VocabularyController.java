//package at.srfg.iasset.semantic.lookup.web.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import at.srfg.iasset.semantic.api.SemanticLookupService;
//import at.srfg.iasset.semantic.lookup.service.onto.OntologyService;
//import at.srfg.iasset.semantic.model.ConceptClass;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import springfox.documentation.annotations.ApiIgnore;
//
//@Api
//@RestController
//public class VocabularyController {
//	@Autowired
//	private OntologyService onto;
//	/**
//	 * Read a {@link ConceptClass} from the semantic lookup service
//	 * @param identifier The URI or IRDI  of the concept class
//	 * @see SemanticLookupService#getConcept(String)
//	 * @return
//	 */
//	@ApiOperation(
//			value = "Upload a vocabulary",
//			notes = "Read the ConceptClass with it's full URI or IRDI")
//	@RequestMapping(
//			method = RequestMethod.POST,
//			path="/vocabulary", consumes = {"application/rdf+xml", "application/turtle"})
//	public Boolean upload(
//			@ApiIgnore
//			@RequestHeader(value="Content-Type", required = false, defaultValue = "application/rdf+xml")
//			String mimeType,
//			@RequestParam("nameSpace") 
//			List<String> nameSpace,
//			@RequestBody 
//			String content) {
//		
//		onto.upload(mimeType, content, nameSpace);
//		return true;
//	}
//	@ApiOperation(
//			value = "Delete entire nameSpaces from the semantic lookup ",
//			notes = "The namespaces are deleted in ConceptClass, Property, PropertyValue and PropertyUnit")
//	@RequestMapping(
//			method = RequestMethod.DELETE,
//			path="/vocabulary")
//	public Boolean delete(
//			@RequestParam("nameSpace") 
//			List<String> nameSpace) {
//		
//		return onto.delete(nameSpace);
//	}
//}
