package at.srfg.iasset.repository.api.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RepositoryException {

	public NotFoundException(String text) {
		super(HttpStatus.NOT_FOUND, text);
	}
	public NotFoundException(String aasIdentifier, String submodelIdentifier, String path) {
		super(HttpStatus.NOT_FOUND, String.format("The element is not available: AAS(%s), Submodel(%s), Path(%s)", aasIdentifier, submodelIdentifier, path));
	}
	public NotFoundException(String submodelIdentifier, String path) {
		super(HttpStatus.NOT_FOUND, String.format("The element is not available: Submodel(%s), Path(%s)",  submodelIdentifier, path));
	}

	private static final long serialVersionUID = 1L;
	

}
