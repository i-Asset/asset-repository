package at.srfg.iasset.connector.component.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TurtleFileResolver {


    Model metaModel;
    /**
    * Initializes the properties by reading and uniforming them.
    *
    * This method is called by the container only. It's not supposed to be invoked by the client directly.
    *
    * @throws IOException
    * in case of any property file access problem
    * @throws URISyntaxException
    */
    @PostConstruct
    public void init() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
        	
        	List<File> turtleFiles = getMetaModelFiles(cl);
        	
        	metaModel = new TreeModel();
        	
        	for (File file : turtleFiles) {
        		FileInputStream fs = new FileInputStream(file);
        		Model parsed = Rio.parse(fs, RDFFormat.TURTLE);
        		
        		metaModel.addAll(parsed);
        	}
        } catch (Exception e) {
        	
        }
    }

    /**
    * Gets flat-file properties files accessible from the root of the given classloader.
    *
    * @param cl
    * classpath to be used when scanning for files.
    *
    * @return found property files.
    *
    * @throws IOException
    * if there was a problem while accessing resources using the <code>cl</code>.
    */
    List<File> getMetaModelFiles(ClassLoader cl) throws IOException {
        List<File> result = new ArrayList<>();

        Enumeration<URL> resources = cl.getResources("");

        while (resources.hasMoreElements()) {
            File resource = getFileFromURL(resources.nextElement());

            File[] files = resource.listFiles(new TurtleFileFilter());
            result.addAll(Arrays.asList(files));
        }

        return result;
    }

    /**
    * Converts URL resource to a File. Makes sure that invalid URL characters (e.g. whitespaces) won't
    * prevent us from accessing the valid file location.
    *
    * @param url
    * URL to be transformed
    *
    * @return File pointing to the given <code>url</code>.
    */
    File getFileFromURL(URL url) {
        File result;

        try {
            result = new File(url.toURI());
        } catch (URISyntaxException e) {
            result = new File(url.getPath());
        }

        return result;
    }

}