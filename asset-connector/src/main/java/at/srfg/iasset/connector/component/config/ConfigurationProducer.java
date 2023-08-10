package at.srfg.iasset.connector.component.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;

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

    @PostConstruct
    public void init() {

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
    }

    @Produces
    @Configurable
    public String produceString(InjectionPoint ip) {
        return properties.getProperty(getKey(ip));
    }

    @Produces
    @Configurable
    public Integer produceInteger(InjectionPoint ip) {
        return Integer.valueOf(properties.getProperty(getKey(ip)));
    }

    @Produces
    @Configurable
    public Long produceLong(InjectionPoint ip) {
        return Long.valueOf(properties.getProperty(getKey(ip)));
    }

    @Produces
    @Configurable
    public Boolean produceBoolean(InjectionPoint ip) {
        return Boolean.valueOf(this.properties.getProperty(getKey(ip)));
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