
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
 * Generated class for Ski Data Request
 * (urn:samm:at.srfg.passat.ski:1.0.0#SkiDataRequest).
 *
 */
@Generated(value = "esmf-sdk 2.14.0", date = "2026-02-13T13:20:04.819+01")

public class SkiDataRequest {

	@NotNull
	private String authenticationToken;

	@NotNull
	private String machineNumber;

	@NotNull
	private String machineType;

	@NotNull
	private URI carrierData;

	@JsonCreator
	public SkiDataRequest(@JsonProperty(value = "authenticationToken") String authenticationToken,
			@JsonProperty(value = "machineNumber") String machineNumber,
			@JsonProperty(value = "machineType") String machineType,
			@JsonProperty(value = "carrierData") URI carrierData) {
		super(

		);
		this.authenticationToken = authenticationToken;
		this.machineNumber = machineNumber;
		this.machineType = machineType;
		this.carrierData = carrierData;
	}

	/**
	 * Returns Authentication Token
	 *
	 * @return {@link #authenticationToken}
	 */
	public String getAuthenticationToken() {
		return this.authenticationToken;
	}

	/**
	 * Returns Machine Number
	 *
	 * @return {@link #machineNumber}
	 */
	public String getMachineNumber() {
		return this.machineNumber;
	}

	/**
	 * Returns Machine Type
	 *
	 * @return {@link #machineType}
	 */
	public String getMachineType() {
		return this.machineType;
	}

	/**
	 * Returns DPP Identifier Data
	 *
	 * @return {@link #carrierData}
	 */
	public URI getCarrierData() {
		return this.carrierData;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final SkiDataRequest that = (SkiDataRequest) o;
		return Objects.equals(authenticationToken, that.authenticationToken)
				&& Objects.equals(machineNumber, that.machineNumber) && Objects.equals(machineType, that.machineType)
				&& Objects.equals(carrierData, that.carrierData);
	}

	@Override
	public int hashCode() {
		return Objects.hash(authenticationToken, machineNumber, machineType, carrierData);
	}
}
