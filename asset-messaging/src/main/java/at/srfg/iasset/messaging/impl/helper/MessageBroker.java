package at.srfg.iasset.messaging.impl.helper;

public class MessageBroker {
	private String hosts;
	private BrokerType brokerType;
	public enum BrokerType {
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
