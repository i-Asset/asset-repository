
package at.srfg.passat.model.samm.generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.processing.Generated;

/**
 * Generated class for Error Message
 * (urn:samm:at.srfg.passat.ski:1.0.0#ErrorMessage).
 *
 */
@Generated(value = "esmf-sdk 2.14.0", date = "2026-02-13T13:20:04.819+01")

public class ErrorMessage {

	@NotNull
	private String errorCode;

	@NotNull
	private String errorMessage;

	@JsonCreator
	public ErrorMessage(@JsonProperty(value = "errorCode") String errorCode,
			@JsonProperty(value = "errorMessage") String errorMessage) {
		super(

		);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	/**
	 * Returns Error Code
	 *
	 * @return {@link #errorCode}
	 */
	public String getErrorCode() {
		return this.errorCode;
	}

	/**
	 * Returns Error Message
	 *
	 * @return {@link #errorMessage}
	 */
	public String getErrorMessage() {
		return this.errorMessage;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final ErrorMessage that = (ErrorMessage) o;
		return Objects.equals(errorCode, that.errorCode) && Objects.equals(errorMessage, that.errorMessage);
	}

	@Override
	public int hashCode() {
		return Objects.hash(errorCode, errorMessage);
	}
}
