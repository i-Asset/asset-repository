package com.wintersteiger.passat.connector;

import at.srfg.iasset.connector.component.AASComponent;
/**
 * Demonstrate the provision of arbitrary XLSX-Files as JSON REST endpoint!
 */
public class Demonstrator {


    public static void main(String [] args) throws Exception {
    	/**
    	 * Instantiate the I4.0 component. the 
    	 */
        AASComponent.create();
        // 
    	Demonstrator connector = new Demonstrator();
        
        //
        // wait for termination
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
            	connector.close();
            }
        });
        while (true) {
            Thread.sleep(5000);
        }
    }
    public void close() {
        System.out.println("Shutdown");
        // shutdown the component
//    	AASComponent.close();
    }

}

