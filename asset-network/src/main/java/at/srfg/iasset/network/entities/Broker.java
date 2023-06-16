package at.srfg.iasset.network.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="itwin_broker")
public class Broker {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Integer id;
	
	@Column(name="display_name", length=64, nullable = false)
	private String label;
	
	@Column(name="hosts", length=64, nullable = false)
	private String host;
	
	@Column(name="broker_type", nullable=false)
	@Enumerated(EnumType.STRING)
	private BrokerType brokerType;
	
	@Embedded
	private BrokerAuthentication authentication;
	
	@JsonIgnore
    @OneToMany(mappedBy = "broker", cascade = CascadeType.ALL)
    private List<NetworkingSystem> networks;


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public String getHost() {
		return host;
	}


	public void setHost(String host) {
		this.host = host;
	}


	public BrokerType getBrokerType() {
		return brokerType;
	}


	public void setBrokerType(BrokerType brokerType) {
		this.brokerType = brokerType;
	}


	public BrokerAuthentication getAuthentication() {
		return authentication;
	}


	public void setAuthentication(BrokerAuthentication authentication) {
		this.authentication = authentication;
	}


	public List<NetworkingSystem> getNetworks() {
		return networks;
	}


	public void setNetworks(List<NetworkingSystem> networks) {
		this.networks = networks;
	}

}
