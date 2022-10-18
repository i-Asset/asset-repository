package at.srfg.iasset.connector.component;

public interface ConnectorEndpoint {
	
	/**
	 * Start a I40 Component/Device, eg. start the REST service providing
	 * external access to this device
	 */
	public void start();
	public void start(int port);
	public void start(int port, String context);
	/**
	 * Stop servicing the component
	 */
	public void stop();
	
	public boolean isStarted();
	
	public void addShellHandler(String alias, String aasIdentifier);
	boolean removeShellHandler(String alias);
	
	void register(String aasIdentifier);

}
