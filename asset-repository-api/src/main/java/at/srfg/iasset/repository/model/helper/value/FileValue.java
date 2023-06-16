package at.srfg.iasset.repository.model.helper.value;

public class FileValue extends DataElementValue {
	private String value; 
	
	private String contentType;
	
	public FileValue() {
	}
	public FileValue(String contentType, String value) {
		this.contentType = contentType;
		this.value = value;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String mimeType) {
		this.contentType = mimeType;
	}
	
}
