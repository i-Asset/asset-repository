package at.srfg.iasset.connector.component;

public interface Component {
	
	/**
	 * Start a I40 Component/Device, eg. start the REST service providing
	 * external access to this device
	 */
	public void start();
	/**
	 * Stop servicing the component
	 */
	public void stop();
	
	public boolean isStarted();
	
	public void addShellHandler(String alias, String aasIdentifier);
	boolean removeShellHandler(String alias);


}
