package at.srfg.iasset.network.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name ="itwin_network")
public class NetworkingSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="display_name", length = 64, nullable = false)
    private String name;
    
    @Column(name="work_center", length = 128, nullable = false)
    private String workCenter;
    @Column(name="station", length = 128, nullable = false)
    private String station;
    
    @Column(name="network_name", length=256, unique=true, nullable=true)
    private String network;
    
    @Column(name="description", length = 1024)
    private String description;

    @ManyToOne
    @JoinColumn(name="enterprise_id", insertable = false, updatable = false)
    private Enterprise enterprise;
    
    @ManyToOne
    @JoinColumn(name="broker_id", insertable =false, updatable = false)
    private Broker broker;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Enterprise getEnterprise() {
		return enterprise;
	}

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String systemName) {
		this.network = systemName;
	}

	public String getWorkCenter() {
		return workCenter;
	}

	public void setWorkCenter(String workCenter) {
		this.workCenter = workCenter;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public Broker getBroker() {
		return broker;
	}

	public void setBroker(Broker broker) {
		this.broker = broker;
	}

}
