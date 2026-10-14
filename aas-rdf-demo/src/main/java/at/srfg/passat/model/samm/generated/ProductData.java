
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
 * Generated class for Product Data
 * (urn:samm:at.srfg.passat.ski:1.0.0#ProductData). Entity containing product
 * information (ski set level)
 */
@Generated(value = "esmf-sdk 2.14.0", date = "2026-02-13T13:20:04.819+01")

public class ProductData {

	@NotNull
	private Double skiWidthTail;

	@NotNull
	private Double skiWidthWaist;

	@NotNull
	private Double skiWidthTip;

	@NotNull
	private BigInteger skiLength;

	@NotNull
	private String skiProductName;

	@NotNull
	private String skiBrand;

	@NotNull
	private String skiManufacturer;

	@JsonCreator
	public ProductData(@JsonProperty(value = "skiWidthTail") Double skiWidthTail,
			@JsonProperty(value = "skiWidthWaist") Double skiWidthWaist,
			@JsonProperty(value = "skiWidthTip") Double skiWidthTip,
			@JsonProperty(value = "skiLength") BigInteger skiLength,
			@JsonProperty(value = "skiProductName") String skiProductName,
			@JsonProperty(value = "skiBrand") String skiBrand,
			@JsonProperty(value = "skiManufacturer") String skiManufacturer) {
		super(

		);
		this.skiWidthTail = skiWidthTail;
		this.skiWidthWaist = skiWidthWaist;
		this.skiWidthTip = skiWidthTip;
		this.skiLength = skiLength;
		this.skiProductName = skiProductName;
		this.skiBrand = skiBrand;
		this.skiManufacturer = skiManufacturer;
	}

	/**
	 * Returns Width (Tail)
	 *
	 * @return {@link #skiWidthTail}
	 */
	public Double getSkiWidthTail() {
		return this.skiWidthTail;
	}

	/**
	 * Returns Width (Waist)
	 *
	 * @return {@link #skiWidthWaist}
	 */
	public Double getSkiWidthWaist() {
		return this.skiWidthWaist;
	}

	/**
	 * Returns Width (Tip)
	 *
	 * @return {@link #skiWidthTip}
	 */
	public Double getSkiWidthTip() {
		return this.skiWidthTip;
	}

	/**
	 * Returns Length
	 *
	 * @return {@link #skiLength}
	 */
	public BigInteger getSkiLength() {
		return this.skiLength;
	}

	/**
	 * Returns Product Name
	 *
	 * @return {@link #skiProductName}
	 */
	public String getSkiProductName() {
		return this.skiProductName;
	}

	/**
	 * Returns Brand
	 *
	 * @return {@link #skiBrand}
	 */
	public String getSkiBrand() {
		return this.skiBrand;
	}

	/**
	 * Returns Manufacturer
	 *
	 * @return {@link #skiManufacturer}
	 */
	public String getSkiManufacturer() {
		return this.skiManufacturer;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final ProductData that = (ProductData) o;
		return Objects.equals(skiWidthTail, that.skiWidthTail) && Objects.equals(skiWidthWaist, that.skiWidthWaist)
				&& Objects.equals(skiWidthTip, that.skiWidthTip) && Objects.equals(skiLength, that.skiLength)
				&& Objects.equals(skiProductName, that.skiProductName) && Objects.equals(skiBrand, that.skiBrand)
				&& Objects.equals(skiManufacturer, that.skiManufacturer);
	}

	@Override
	public int hashCode() {
		return Objects.hash(skiWidthTail, skiWidthWaist, skiWidthTip, skiLength, skiProductName, skiBrand,
				skiManufacturer);
	}
}
