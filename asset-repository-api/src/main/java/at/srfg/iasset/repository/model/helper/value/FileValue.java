package at.srfg.iasset.repository.model.helper.value;

public class FileValue extends DataElementValue {
	private String value; 
	
	private String mimeType;
	
	public FileValue() {
	}
	public FileValue(String contentType, String value) {
		this.mimeType = contentType;
		this.value = value;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
}
