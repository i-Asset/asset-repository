
package at.srfg.passat.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.processing.Generated;

/**
 * Generated class for Error Message
 * (urn:samm:at.srfg.passat.ski:1.0.0#SkiDataError). Error Message
 */
@Generated(value = "esmf-sdk 2.13.0", date = "2026-02-13T09:21:15.348+01")

public class SkiDataError {

	@Valid

	@NotNull
	private SkiDataRequest serviceRequest;

	@Valid

	@NotNull
	private ErrorMessage error;

	@JsonCreator
	public SkiDataError(@JsonProperty(value = "serviceRequest") SkiDataRequest serviceRequest,
			@JsonProperty(value = "error") ErrorMessage error) {
		super(

		);
		this.serviceRequest = serviceRequest;
		this.error = error;
	}

	/**
	 * Returns Service Request Data
	 *
	 * @return {@link #serviceRequest}
	 */
	public SkiDataRequest getServiceRequest() {
		return this.serviceRequest;
	}

	/**
	 * Returns Error Object
	 *
	 * @return {@link #error}
	 */
	public ErrorMessage getError() {
		return this.error;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final SkiDataError that = (SkiDataError) o;
		return Objects.equals(serviceRequest, that.serviceRequest) && Objects.equals(error, that.error);
	}

	@Override
	public int hashCode() {
		return Objects.hash(serviceRequest, error);
	}
}
