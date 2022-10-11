package at.srfg.iasset.repository.api.exception;

import org.springframework.http.HttpStatus;

public class RepositoryNotFoundException extends RepositoryException {

	private static final long serialVersionUID = 1L;

	public RepositoryNotFoundException(String msg) {
		super(HttpStatus.NOT_FOUND, msg);
	}
}
