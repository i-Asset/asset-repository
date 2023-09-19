package at.srfg.iasset.repository.model;

import java.util.Date;

public class Fault {
	public String assetId;
	public String subElementPath;
	public String faultId;
	public Date timestampCreated;
	public Date timestampFinished;
	public ErrorCode errorCode;
	public String faultCode;
	public String shortText;
	public String longText;
	public int priority;
	public String senderUserId;
	public String maintencanceUserId;
	public String status;
	
	/**
	 * @return the errorCode
	 */
	public ErrorCode getErrorCode() {
		return errorCode;
	}
	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getAssetId() {
		return assetId;
	}
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	public String getSubElementPath() {
		return subElementPath;
	}
	public void setSubElementPath(String subElementPath) {
		this.subElementPath = subElementPath;
	}
	public String getFaultId() {
		return faultId;
	}
	public void setFaultId(String faultId) {
		this.faultId = faultId;
	}
	public Date getTimestampCreated() {
		return timestampCreated;
	}
	public void setTimestampCreated(Date timestampCreated) {
		this.timestampCreated = timestampCreated;
	}
	public Date getTimestampFinished() {
		return timestampFinished;
	}
	public void setTimestampFinished(Date timestampFinished) {
		this.timestampFinished = timestampFinished;
	}
	public String getFaultCode() {
		return faultCode;
	}
	public void setFaultCode(String faultCode) {
		this.faultCode = faultCode;
	}
	public String getShortText() {
		return shortText;
	}
	public void setShortText(String shortText) {
		this.shortText = shortText;
	}
	public String getLongText() {
		return longText;
	}
	public void setLongText(String longText) {
		this.longText = longText;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getSenderUserId() {
		return senderUserId;
	}
	public void setSenderUserId(String senderUserId) {
		this.senderUserId = senderUserId;
	}
	public String getMaintencanceUserId() {
		return maintencanceUserId;
	}
	public void setMaintencanceUserId(String maintencanceUserId) {
		this.maintencanceUserId = maintencanceUserId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
