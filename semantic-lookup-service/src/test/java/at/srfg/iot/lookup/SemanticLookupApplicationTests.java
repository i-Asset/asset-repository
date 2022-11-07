//package at.srfg.iot.lookup;
//
//import java.util.Locale;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import at.srfg.iot.common.datamodel.semanticlookup.model.ConceptClass;
//import at.srfg.iot.lookup.dependency.SemanticIndexing;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class SemanticLookupApplicationTests {
//	@Autowired
//	private SemanticIndexing indexer;
//
//	
//	@Test
//	public void contextLoads() {
//	}
//	@Test
//	public void testConceptDescription() {
//		ConceptClass cc = new ConceptClass("http://uri.com");
//		cc.setPreferredLabel(Locale.GERMAN, "Bezeichnung");
//		cc.setPreferredLabel(Locale.ENGLISH, "Label");
//		
//		cc.addAlternateLabel(Locale.GERMAN, "Name");
//		cc.addHiddenLabel(Locale.GERMAN, "Rufname");
//		
//		
//		cc.getLabels();
//		
//	}
//
//}
