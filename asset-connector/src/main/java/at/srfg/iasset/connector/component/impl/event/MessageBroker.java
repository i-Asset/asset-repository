package at.srfg.iasset.connector.component.impl.event;

public class MessageBroker {
	private String hosts;
	private BrokerType brokerType;
	enum BrokerType {
		MQTT, KAFKA
	}
	public String getHosts() {
		return hosts;
	}
	public void setHosts(String hosts) {
		this.hosts = hosts;
	}
	public BrokerType getBrokerType() {
		return brokerType;
	}
	public void setBrokerType(BrokerType brokerType) {
		this.brokerType = brokerType;
	}

}
