package at.srfg.iasset.connector.isproNG;

public interface IsproNGEventHandler<T> {
    void onEventMessage(T payload);
}