package at.srfg.iasset.connector.component.endpoint.config.exception;

/**
 * Thrown if errors occur during the authentication process.
 *
 * @author cassiomolin
 */
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
