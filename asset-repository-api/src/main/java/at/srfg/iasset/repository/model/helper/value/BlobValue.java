package at.srfg.iasset.repository.model.helper.value;

public class BlobValue extends DataElementValue {
	private byte[] value; 
	
	private String mimeType;
	
	public BlobValue() {
	}
	public BlobValue(String contentType, byte[] value) {
		this.mimeType = contentType;
		this.value = value;
	}
	public byte[] getValue() {
		return value;
	}
	public void setValue(byte[] value) {
		this.value = value;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
}
