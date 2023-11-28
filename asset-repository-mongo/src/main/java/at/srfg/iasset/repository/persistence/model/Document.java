package at.srfg.iasset.repository.persistence.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.digitaltwin.aas4j.v3.model.Identifiable;
import org.springframework.data.annotation.Id;

public abstract class Document<T extends Identifiable> {
	
	private T data;
	
	private Instant lastUpdate;
	@Id
	private String id;
	
	private String owner;
	
	private List<String> category = new ArrayList<String>(); 
	
	public final void setData(T data) {
		this.id = data.getId();
		this.data = data;
		this.lastUpdate = Instant.now();
	}
	public final T getData() {
		return data;
	}
	public final Instant getLastUpdate() {
		return lastUpdate;
	}
	public final String getId() {
		return id;
	}

}
