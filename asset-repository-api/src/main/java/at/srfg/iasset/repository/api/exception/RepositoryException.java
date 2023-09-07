package at.srfg.iasset.repository.api.exception;

import org.springframework.http.HttpStatus;

public class RepositoryException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public final HttpStatus status;

	public HttpStatus getStatus() {
		return status;
	}

	public RepositoryException(HttpStatus status, String text) {
		super(text);
		this.status = status;
	}
}
