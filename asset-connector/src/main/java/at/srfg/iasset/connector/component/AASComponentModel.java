package at.srfg.iasset.connector.component;

import at.srfg.iasset.connector.environment.LocalEnvironment;
/**
 * Interface allowing for Dependency injection of
 * the aas model!
 */
public interface AASComponentModel {
	
	default void loadData(LocalEnvironment environment) {
		
	}
	default void injectLogic(LocalEnvironment environment) {
		
	}
	
}
