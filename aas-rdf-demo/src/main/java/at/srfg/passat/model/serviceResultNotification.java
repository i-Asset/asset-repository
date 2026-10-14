
package at.srfg.passat.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.math.BigInteger;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.processing.Generated;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Generated class for Ski Data Result Message
 * (urn:samm:at.srfg.passat.ski:1.0.0#serviceResultNotification). Result Data of
 * the Ski Service Process
 */
@Generated(value = "esmf-sdk 2.13.0", date = "2026-02-13T09:21:15.348+01")
public class serviceResultNotification {

	@Valid

	@NotNull
	private SkiDataResult serviceResult;

	@JsonCreator
	public serviceResultNotification(@JsonProperty(value = "serviceResult") SkiDataResult serviceResult) {
		super(

		);
		this.serviceResult = serviceResult;
	}

	/**
	 * Returns Ski Service Result
	 *
	 * @return {@link #serviceResult}
	 */
	public SkiDataResult getServiceResult() {
		return this.serviceResult;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final serviceResultNotification that = (serviceResultNotification) o;
		return Objects.equals(serviceResult, that.serviceResult);
	}

	@Override
	public int hashCode() {
		return Objects.hash(serviceResult);
	}
}
