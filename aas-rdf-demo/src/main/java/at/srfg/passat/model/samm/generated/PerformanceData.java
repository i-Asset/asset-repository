
package at.srfg.passat.model.samm.generated;

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
 * Generated class for Performance Data
 * (urn:samm:at.srfg.passat.ski:1.0.0#PerformanceData). Data controlling the
 * processing performance
 */
@Generated(value = "esmf-sdk 2.14.0", date = "2026-02-13T13:20:04.819+01")

public class PerformanceData {

	@NotNull
	private PerformanceClassEnum performanceClass;

	@NotNull
	private Boolean vEdge;

	@JsonCreator
	public PerformanceData(@JsonProperty(value = "performanceClass") PerformanceClassEnum performanceClass,
			@JsonProperty(value = "vEdge") Boolean vEdge) {
		super(

		);
		this.performanceClass = performanceClass;
		this.vEdge = vEdge;
	}

	/**
	 * Returns Class
	 *
	 * @return {@link #performanceClass}
	 */
	public PerformanceClassEnum getPerformanceClass() {
		return this.performanceClass;
	}

	/**
	 * Returns vEdge
	 *
	 * @return {@link #vEdge}
	 */
	public Boolean isVEdge() {
		return this.vEdge;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final PerformanceData that = (PerformanceData) o;
		return Objects.equals(performanceClass, that.performanceClass) && Objects.equals(vEdge, that.vEdge);
	}

	@Override
	public int hashCode() {
		return Objects.hash(performanceClass, vEdge);
	}
}
