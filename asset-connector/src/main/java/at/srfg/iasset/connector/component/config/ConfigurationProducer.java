package at.srfg.iasset.connector.component.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Inject;

/**
 * Read the <code>application.properties</code> file from the classpath and produce values that can be injected with @{@link Configurable}.
 * <p>
 * It's a simple and lightweight alternative to the Apache DeltaSpike Configuration Mechanism.
 *
 * @author cassiomolin
 */
@ApplicationScoped
public class ConfigurationProducer {

    private Properties properties;
    
    private Properties sysProperies; 
    
    @Inject
    private Logger logger;

    @PostConstruct
    public void init() {
    	sysProperies = System.getProperties();

        properties = new Properties();
        InputStream stream = ConfigurationProducer.class.getResourceAsStream("/application.properties");

        if (stream == null) {
            throw new RuntimeException("Cannot find application.properties configuration file.");
        }

        try {
            this.properties.load(stream);
        } catch (final IOException e) {
            throw new RuntimeException("Configuration file cannot be loaded.");
        }
        // check for additional properties
        loadAdditionalProperties();
    }
    private void loadAdditionalProperties() {
        File jarFile = new File(ConfigurationProducer.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        File appProps = new File(jarFile.getParent().concat("/app.properties"));
        if ( appProps.exists()) {
        	try {
                logger.info("Using configuration file: "+ appProps.getAbsolutePath());
				this.properties.load(new FileInputStream(appProps.getAbsolutePath()));
			} catch (IOException e) {
                logger.error("Error loading configuration file: "+ appProps.getAbsolutePath());
			}
        }
        else {
        	logger.info("No configuration file found, please provide properties file here: " + appProps.getAbsolutePath() );
        }
    }

    @Produces
    @Configurable
    public String produceString(InjectionPoint ip) {
    	return getProperty(ip);
//    	return properties.getProperty(getKey(ip));
    }

    @Produces
    @Configurable
    public Integer produceInteger(InjectionPoint ip) {
    	return Integer.valueOf(getProperty(ip));
//        return Integer.valueOf(properties.getProperty(getKey(ip)));
    }

    @Produces
    @Configurable
    public Long produceLong(InjectionPoint ip) {
    	return Long.valueOf(getProperty(ip));
//        return Long.valueOf(properties.getProperty(getKey(ip)));
    }

    @Produces
    @Configurable
    public Boolean produceBoolean(InjectionPoint ip) {
    	return Boolean.valueOf(getProperty(ip));
//        return Boolean.valueOf(this.properties.getProperty(getKey(ip)));
    }
    private String getProperty(InjectionPoint ip) {
    	String key = getKey(ip);
    	return sysProperies.getProperty(key, properties.getProperty(key));
    }
    private String getKey(InjectionPoint ip) {
    	Configurable classConfigurable = ip.getMember().getDeclaringClass().getAnnotation(Configurable.class);
    	if ( classConfigurable != null) {
    		String prefix = classConfigurable.value(); 
    		if ( !prefix.endsWith(".")) {
    			prefix = prefix.concat(".");
    		}
    		return prefix + ip.getAnnotated().getAnnotation(Configurable.class).value();
    	}
        return ip.getAnnotated().getAnnotation(Configurable.class).value();
    }
}