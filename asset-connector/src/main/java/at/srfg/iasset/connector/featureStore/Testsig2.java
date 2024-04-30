package at.srfg.iasset.connector.featureStore;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Testsig2 implements Runnable {
		
	private Boolean keepalive;
	private int srate; // in seconds
	private ArrayStream stream;
	
	public Testsig2(Integer srate, ArrayStream stream) {
		this.keepalive = true;
		this.srate = srate;
		this.stream = stream;
		
		// Testsig ECG
		
	}	
	
	public void stop() {
		keepalive = false;
	}

	@Override
	public void run() {
		try(BufferedReader br = new BufferedReader(new FileReader("src/main/resources/testsig_thor.csv"))){
			String line = br.readLine(); // skip Header
			while(keepalive & (line = br.readLine()) != null)
				try {
					String[] values = line.split(",");
					stream.pushBuffer( new Object[] { values[0], values[1], values[2], values[3], values[4] } );
					Thread.sleep(srate);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
}