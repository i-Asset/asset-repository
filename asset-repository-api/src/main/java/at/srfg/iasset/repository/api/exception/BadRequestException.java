package at.srfg.iasset.repository.api.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RepositoryException {

	public BadRequestException(String text) {
		super(HttpStatus.BAD_REQUEST, text);
	}

	private static final long serialVersionUID = 1L;
	

}
