package at.srfg.iasset.messaging.exception;

public class MessagingException extends Exception {
	private static final long serialVersionUID = 1L;
	public MessagingException(String message) {
		super(message);
	}
	public MessagingException(Throwable throwable) {
		super(throwable);
	}
}

