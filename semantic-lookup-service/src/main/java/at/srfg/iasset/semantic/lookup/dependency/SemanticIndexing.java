//package at.srfg.iasset.semantic.lookup.dependency;
//
//import java.util.List;
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import at.srfg.iot.common.solr.model.model.common.ClassType;
//import at.srfg.iot.common.solr.model.model.common.PropertyType;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
///**
// * Redefinition - Feign does not support multiple inheritance
// * @author dglachs
// *
// */
//@FeignClient(name = "indexing-service")
//public interface SemanticIndexing {
//	
//	/**
//	 * Store a new or update an existing {@link ClassType} object
//	 * @param prop
//	 * @return The stored version of the classType
//	 * @throws Exception
//	 */
//	@RequestMapping(
//			method = RequestMethod.POST,
//			path="/classType")
//	public ClassType setClassType(
////			@RequestHeader(value = "Authorization") 
////			String bearerToken,
//			@RequestBody ClassType prop) throws Exception;
//	/**
//	 * Remove the provided class from the index
//	 * @param uri
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(
//			method = RequestMethod.DELETE,
//			path="/classType")
//	public boolean deleteClassType(
//			@RequestParam(name = "id") List<String> uri) throws Exception ;
//	/**
//	 * Store a new or update an existing {@link PropertyType} object
//	 * @param prop
//	 * @return The stored version of the PropertyType
//	 * @throws Exception
//	 */
//	@RequestMapping(
//			method = RequestMethod.POST,
//			path="/propertyType")
//	public PropertyType setPropertyType(
////			@RequestHeader(value = "Authorization") 
////			String bearerToken,
//			@RequestBody PropertyType prop) throws Exception;
//	/**
//	 * Remove the provided property from the index
//	 * @param uri
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(
//			method = RequestMethod.DELETE,
//			path="/propertyType")
//	public boolean deletePropertyType(
//			@RequestParam(name = "id") 
//			List<String> uri) throws Exception;
//	/**
//	 * Remove the provided class from the index
//	 * @param uri
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(
//			method = RequestMethod.DELETE,
//			path="/namespace")
//	@ApiOperation(
//			value = "Delete all concepts of the provided namespaces. Deletes from multiple collections", 
//			notes = "All concepts are removed!")
//	public long deleteConcepts(
//			@ApiParam("The nameSpace to remove")
//			@RequestParam(name = "nameSpace") String uri) throws Exception ;
//	
//}
