package at.srfg.iasset.connector.featureStore;

import uk.me.berndporr.iirj.Butterworth;

public class IIR_Filters{
	
	double srate;
	
	public IIR_Filters(double srate) {
		this.srate = srate;
	}
	
	public Butterworth registerLowPass(int order, double cutOff) {
		
        Butterworth lp = new Butterworth();
        lp.lowPass(order, srate, cutOff);
		return lp;
	}
	
	public Butterworth registerHighpass(int order, double cutOff) {
	
        Butterworth hp = new Butterworth();
        hp.highPass(order, srate, cutOff);
		return hp;
	}
	
	public Butterworth registerBandpass(int order, double lowCutoff, double highCutoff) {
		if (lowCutoff >= highCutoff) {
            throw new IllegalArgumentException("Lower Cutoff Frequency cannot be more than the Higher Cutoff Frequency");
        }
        double centreFreq = (highCutoff + lowCutoff)/2.0;
        double width = Math.abs(highCutoff - lowCutoff);
        Butterworth bp = new Butterworth();
        bp.bandPass(order, srate, centreFreq, width);
		return bp;
	}
	
	public Butterworth registerBandstop1(int order, double lowCutoff, double highCutoff) {
		if (lowCutoff >= highCutoff) {
            throw new IllegalArgumentException("Lower Cutoff Frequency cannot be more than the Higher Cutoff Frequency");
        }
        double centreFreq = (highCutoff + lowCutoff)/2.0;
        double width = Math.abs(highCutoff - lowCutoff);
        Butterworth bs = new Butterworth();
        bs.bandStop(order, srate, centreFreq, width);
		return bs;
	}
	
}
