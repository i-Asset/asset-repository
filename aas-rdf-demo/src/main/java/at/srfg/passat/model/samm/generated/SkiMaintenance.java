
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
 * Generated class for Ski Maintenance
 * (urn:samm:at.srfg.passat.ski:1.0.0#SkiMaintenance). Aspect model for a ski
 * maintenance process (aka "ski service"). This Aspect Model was developed by
 * Salzburg Research (SRFG, https://www.salzburgresearch.at) in the project
 * PASSAT (Digital Product Passport Austria & Beyond,
 * https://digialer-produkt-pass.at)
 */
@Generated(value = "esmf-sdk 2.14.0", date = "2026-02-13T13:20:04.819+01")
public class SkiMaintenance {

	@Valid

	@NotNull
	private SkiDataResponse serviceData;

	@Valid

	@NotNull
	private SkiDataRequest serviceRequest;

	@Valid

	@NotNull
	private SkiDataResult serviceResult;

	@Valid

	@NotNull
	private SkiDataError serviceError;

	@JsonCreator
	public SkiMaintenance(@JsonProperty(value = "serviceData") SkiDataResponse serviceData,
			@JsonProperty(value = "serviceRequest") SkiDataRequest serviceRequest,
			@JsonProperty(value = "serviceResult") SkiDataResult serviceResult,
			@JsonProperty(value = "serviceError") SkiDataError serviceError) {
		super(

		);
		this.serviceData = serviceData;
		this.serviceRequest = serviceRequest;
		this.serviceResult = serviceResult;
		this.serviceError = serviceError;
	}

	/**
	 * Returns Ski Service Data
	 *
	 * @return {@link #serviceData}
	 */
	public SkiDataResponse getServiceData() {
		return this.serviceData;
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
	 * Returns Ski Service Result
	 *
	 * @return {@link #serviceResult}
	 */
	public SkiDataResult getServiceResult() {
		return this.serviceResult;
	}

	/**
	 * Returns Error Handling
	 *
	 * @return {@link #serviceError}
	 */
	public SkiDataError getServiceError() {
		return this.serviceError;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final SkiMaintenance that = (SkiMaintenance) o;
		return Objects.equals(serviceData, that.serviceData) && Objects.equals(serviceRequest, that.serviceRequest)
				&& Objects.equals(serviceResult, that.serviceResult) && Objects.equals(serviceError, that.serviceError);
	}

	@Override
	public int hashCode() {
		return Objects.hash(serviceData, serviceRequest, serviceResult, serviceError);
	}
}
