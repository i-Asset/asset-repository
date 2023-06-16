package at.srfg.iasset.repository.model.helper.value;

public class BlobValue extends DataElementValue {
	private byte[] value; 
	
	private String contentType;
	
	public BlobValue() {
	}
	public BlobValue(String contentType, byte[] value) {
		this.contentType = contentType;
		this.value = value;
	}
	public byte[] getValue() {
		return value;
	}
	public void setValue(byte[] value) {
		this.value = value;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String mimeType) {
		this.contentType = mimeType;
	}
	
}
