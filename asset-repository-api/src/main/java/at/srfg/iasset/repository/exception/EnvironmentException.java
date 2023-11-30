package at.srfg.iasset.repository.exception;

public class EnvironmentException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public EnvironmentException(String message) {
		super(message);
	}
    public EnvironmentException(String message, Throwable cause) {
        super(message, cause);
    }

}
