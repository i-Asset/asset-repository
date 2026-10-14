
package at.srfg.passat.model;

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
 * Generated class for Edge grinding parameter
 * (urn:samm:at.srfg.passat.ski:1.0.0#EdgeParameter). Angles for the grinding
 * process (tip, tail, front)
 */
@Generated(value = "esmf-sdk 2.13.0", date = "2026-02-13T09:21:15.348+01")

public class EdgeParameter {

	@NotNull
	private Double angleTail;

	@NotNull
	private Double angleWaist;

	@NotNull
	private Double angleTip;

	@JsonCreator
	public EdgeParameter(@JsonProperty(value = "angleTail") Double angleTail,
			@JsonProperty(value = "angleWaist") Double angleWaist, @JsonProperty(value = "angleTip") Double angleTip) {
		super(

		);
		this.angleTail = angleTail;
		this.angleWaist = angleWaist;
		this.angleTip = angleTip;
	}

	/**
	 * Returns Grinding Angle Tail
	 *
	 * @return {@link #angleTail}
	 */
	public Double getAngleTail() {
		return this.angleTail;
	}

	/**
	 * Returns Grinding Angle Waist
	 *
	 * @return {@link #angleWaist}
	 */
	public Double getAngleWaist() {
		return this.angleWaist;
	}

	/**
	 * Returns Grinding Angle Tip
	 *
	 * @return {@link #angleTip}
	 */
	public Double getAngleTip() {
		return this.angleTip;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final EdgeParameter that = (EdgeParameter) o;
		return Objects.equals(angleTail, that.angleTail) && Objects.equals(angleWaist, that.angleWaist)
				&& Objects.equals(angleTip, that.angleTip);
	}

	@Override
	public int hashCode() {
		return Objects.hash(angleTail, angleWaist, angleTip);
	}
}
