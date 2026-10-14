package at.srfg.iasset.repository.model.helper.rdf.mapper;

import java.io.StringWriter;
import java.time.LocalDate;
import java.util.Date;

import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.BasicWriterSettings;
import org.eclipse.rdf4j.rio.jsonld.JSONLDMode;
import org.eclipse.rdf4j.rio.jsonld.JSONLDSettings;

public class OrderedListExample {
    public static void main(String[] args) {
        // Create a ValueFactory for building RDF terms
        ValueFactory vf = SimpleValueFactory.getInstance();

        // Create an empty RDF model
        Model model = new LinkedHashModel();
        model.setNamespace("ex", "http://example.org/");
        
        // Define subject and predicate
        IRI subject = vf.createIRI("http://example.org/person/Alice");
        IRI hasHobbies = vf.createIRI("http://example.org/hasHobbies");
        IRI kindOfSport = vf.createIRI("http://example.org/kindOfSport");
        IRI passionSince = vf.createIRI("http://example.org/passionSince");
        
        BNode sport1 = vf.createBNode();
        model.add(sport1, kindOfSport, vf.createLiteral("Cycling"));
        model.add(sport1, passionSince,vf.createLiteral(LocalDate.of(2001, 2, 13)));

        BNode sport2 = vf.createBNode();
        model.add(sport2, kindOfSport, vf.createLiteral("Curling"));
        model.add(sport2, passionSince,vf.createLiteral(new Date()));

        // Create RDF list elements
        Literal hobby1 = vf.createLiteral("Reading");
        Literal hobby2 = vf.createLiteral("Hiking");
        Literal hobby3 = vf.createLiteral("Cooking");

        // Create RDF Collection (rdf:List) and add it to the model
        BNode listHead = vf.createBNode();
        model.add(subject, hasHobbies, listHead);

        // Build RDF list manually: (Reading Hiking Cooking)
        BNode node1 = listHead;
        model.add(node1, RDF.FIRST, sport1);
        BNode node2 = vf.createBNode();
        model.add(node1, RDF.REST, node2);

        model.add(node2, RDF.FIRST, sport2);
        BNode node3 = vf.createBNode();
        model.add(node2, RDF.REST, RDF.NIL);

//        model.add(node3, RDF.FIRST, hobby3);
//        model.add(node3, RDF.REST, RDF.NIL); // End of list

        // Serialize model to JSON-LD
        StringWriter out = new StringWriter();
        try {
            Rio.write(model, out, RDFFormat.JSONLD,
                    Rio.createWriter(RDFFormat.JSONLD, out)
                            .getWriterConfig()
                            .set(JSONLDSettings.JSONLD_MODE, JSONLDMode.COMPACT)
                            .set(BasicWriterSettings.PRETTY_PRINT, true)
                            .set(JSONLDSettings.COMPACT_ARRAYS, true)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Output JSON-LD
        System.out.println(out.toString());
    }
}
