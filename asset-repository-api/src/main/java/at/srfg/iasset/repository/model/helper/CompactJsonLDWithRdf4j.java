package at.srfg.iasset.repository.model.helper;

import java.io.StringWriter;

import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.BasicWriterSettings;
import org.eclipse.rdf4j.rio.helpers.JSONLDMode;
import org.eclipse.rdf4j.rio.helpers.JSONLDSettings;

public class CompactJsonLDWithRdf4j {

    public static void main(String[] args) {
        // Beispiel-Model aufbauen
        ValueFactory vf = SimpleValueFactory.getInstance();
        Model model = new TreeModel();

        String ex = "http://example.com/";
        model.add(
                vf.createIRI(ex, "person/123"),
                vf.createIRI("http://schema.org/name"),
                vf.createLiteral("Max Mustermann")
        );
        BNode bnode = vf.createBNode();
        model.add(
                vf.createIRI(ex, "person/123"),
                vf.createIRI("http://schema.org/edgeParam"), 
                bnode
//                vf.createIRI("https://example.com/edgeParam/123")
        );
        model.add( 
                bnode,
                RDF.TYPE,
                vf.createIRI("http://schema.org/EdgeParameter")
    		);
        model.add( 
                bnode,
                vf.createIRI("http://schema.org/angleTip"),
                vf.createLiteral("0.8")
    		);
        model.add( 
                bnode,
                vf.createIRI("http://schema.org/angleWaist"),
                vf.createLiteral("0.8")
    		);
        model.add( 
                bnode,
                vf.createIRI("http://schema.org/angleTail"),
                vf.createLiteral("0.8")
    		);
        model.setNamespace("schema", "http://schema.org/");
        // JSON-LD compact Writer konfigurieren
        StringWriter sw = new StringWriter();
        RDFWriter writer = Rio.createWriter(RDFFormat.JSONLD, sw);
        
//        writer.handleNamespace("schema", "http://schema.org/");
//        writer.handleNamespace("ex", ex);
        // JSON-LD Modus: COMPACT
        writer.getWriterConfig().set(JSONLDSettings.JSONLD_MODE, JSONLDMode.COMPACT);

        // Optional: hübsche Ausgabe und kompakte Arrays
        writer.getWriterConfig().set(BasicWriterSettings.PRETTY_PRINT, true);
        writer.getWriterConfig().set(JSONLDSettings.COMPACT_ARRAYS, true);
        
//        // Optional: Namespaces für kompakte Prefixe im Kontext
//        writer.startRDF();
//        // Schreiben
//        writer.endRDF();
        
        Rio.write(model, writer);

        String jsonLd = sw.toString();
        System.out.println(jsonLd);
    }
}
