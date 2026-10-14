
package at.srfg.passat.model.samm.generated;

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
 * Generated class for Result Entity
 * (urn:samm:at.srfg.passat.ski:1.0.0#SkiDataResult).
 *
 */
@Generated(value = "esmf-sdk 2.14.0", date = "2026-02-13T13:20:04.819+01")

public class SkiDataResult {

	@NotNull
	private XMLGregorianCalendar resultDateTime;

	@Valid

	@NotNull
	private SkiDataRequest serviceRequest;

	@Valid

	@NotNull
	private EdgeParameter baseEdgeParameter;

	@Valid

	@NotNull
	private EdgeParameter sideEdgeParameter;

	@Valid

	@NotNull
	private IdentificationData identification;

	@NotNull
	private Boolean resultHasDeviations;

	@NotNull
	private String resultDeviationDescription;

	@JsonCreator
	public SkiDataResult(@JsonProperty(value = "resultDateTime") XMLGregorianCalendar resultDateTime,
			@JsonProperty(value = "serviceRequest") SkiDataRequest serviceRequest,
			@JsonProperty(value = "baseEdgeParameter") EdgeParameter baseEdgeParameter,
			@JsonProperty(value = "sideEdgeParameter") EdgeParameter sideEdgeParameter,
			@JsonProperty(value = "identification") IdentificationData identification,
			@JsonProperty(value = "resultHasDeviations") Boolean resultHasDeviations,
			@JsonProperty(value = "resultDeviationDescription") String resultDeviationDescription) {
		super(

		);
		this.resultDateTime = resultDateTime;
		this.serviceRequest = serviceRequest;
		this.baseEdgeParameter = baseEdgeParameter;
		this.sideEdgeParameter = sideEdgeParameter;
		this.identification = identification;
		this.resultHasDeviations = resultHasDeviations;
		this.resultDeviationDescription = resultDeviationDescription;
	}

	/**
	 * Returns Maintenance Date & Time
	 *
	 * @return {@link #resultDateTime}
	 */
	public XMLGregorianCalendar getResultDateTime() {
		return this.resultDateTime;
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
	 * Returns Base Edge Parameter
	 *
	 * @return {@link #baseEdgeParameter}
	 */
	public EdgeParameter getBaseEdgeParameter() {
		return this.baseEdgeParameter;
	}

	/**
	 * Returns Side Edge parameter
	 *
	 * @return {@link #sideEdgeParameter}
	 */
	public EdgeParameter getSideEdgeParameter() {
		return this.sideEdgeParameter;
	}

	/**
	 * Returns Ski Data Identification
	 *
	 * @return {@link #identification}
	 */
	public IdentificationData getIdentification() {
		return this.identification;
	}

	/**
	 * Returns Result with deviations
	 *
	 * @return {@link #resultHasDeviations}
	 */
	public Boolean isResultHasDeviations() {
		return this.resultHasDeviations;
	}

	/**
	 * Returns Deviation Description
	 *
	 * @return {@link #resultDeviationDescription}
	 */
	public String getResultDeviationDescription() {
		return this.resultDeviationDescription;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final SkiDataResult that = (SkiDataResult) o;
		return Objects.equals(resultDateTime, that.resultDateTime)
				&& Objects.equals(serviceRequest, that.serviceRequest)
				&& Objects.equals(baseEdgeParameter, that.baseEdgeParameter)
				&& Objects.equals(sideEdgeParameter, that.sideEdgeParameter)
				&& Objects.equals(identification, that.identification)
				&& Objects.equals(resultHasDeviations, that.resultHasDeviations)
				&& Objects.equals(resultDeviationDescription, that.resultDeviationDescription);
	}

	@Override
	public int hashCode() {
		return Objects.hash(resultDateTime, serviceRequest, baseEdgeParameter, sideEdgeParameter, identification,
				resultHasDeviations, resultDeviationDescription);
	}
}
