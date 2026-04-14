
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

/**
 * Generated class for Ski Data Response
 * (urn:samm:at.srfg.passat.ski:1.0.0#SkiDataResponse). Response from the
 * manufacturer
 */
@Generated(value = "esmf-sdk 2.14.0", date = "2026-02-13T13:20:04.819+01")

public class SkiDataResponse {

	@Valid

	@NotNull
	private PerformanceData edgePerformance;

	@Valid

	@NotNull
	private EdgeParameter baseEdgeParameter;

	@Valid

	@NotNull
	private EdgeParameter sideEdgeParameter;

	@Valid

	@NotNull
	private ProductData productData;

	@Valid

	@NotNull
	private IdentificationData identification;

	@Valid

	@NotNull
	private SkiDataRequest serviceRequest;

	@JsonCreator
	public SkiDataResponse(@JsonProperty(value = "edgePerformance") PerformanceData edgePerformance,
			@JsonProperty(value = "baseEdgeParameter") EdgeParameter baseEdgeParameter,
			@JsonProperty(value = "sideEdgeParameter") EdgeParameter sideEdgeParameter,
			@JsonProperty(value = "productData") ProductData productData,
			@JsonProperty(value = "identification") IdentificationData identification,
			@JsonProperty(value = "serviceRequest") SkiDataRequest serviceRequest) {
		super(

		);
		this.edgePerformance = edgePerformance;
		this.baseEdgeParameter = baseEdgeParameter;
		this.sideEdgeParameter = sideEdgeParameter;
		this.productData = productData;
		this.identification = identification;
		this.serviceRequest = serviceRequest;
	}

	/**
	 * Returns Edge Performance
	 *
	 * @return {@link #edgePerformance}
	 */
	public PerformanceData getEdgePerformance() {
		return this.edgePerformance;
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
	 * Returns Product Data
	 *
	 * @return {@link #productData}
	 */
	public ProductData getProductData() {
		return this.productData;
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
	 * Returns Service Request Data
	 *
	 * @return {@link #serviceRequest}
	 */
	public SkiDataRequest getServiceRequest() {
		return this.serviceRequest;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final SkiDataResponse that = (SkiDataResponse) o;
		return Objects.equals(edgePerformance, that.edgePerformance)
				&& Objects.equals(baseEdgeParameter, that.baseEdgeParameter)
				&& Objects.equals(sideEdgeParameter, that.sideEdgeParameter)
				&& Objects.equals(productData, that.productData) && Objects.equals(identification, that.identification)
				&& Objects.equals(serviceRequest, that.serviceRequest);
	}

	@Override
	public int hashCode() {
		return Objects.hash(edgePerformance, baseEdgeParameter, sideEdgeParameter, productData, identification,
				serviceRequest);
	}
}
