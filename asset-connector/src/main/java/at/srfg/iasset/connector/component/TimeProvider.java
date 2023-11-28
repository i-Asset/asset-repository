package at.srfg.iasset.connector.component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class TimeProvider {
    public String getCurrentTime() {
        LocalDateTime now =  LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
}
