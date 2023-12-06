package at.srfg.iasset.repository.api.exception;

import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends RepositoryException {

	public InternalServerErrorException(String text) {
		super(HttpStatus.INTERNAL_SERVER_ERROR, text);
	}

	private static final long serialVersionUID = 1L;
	

}
