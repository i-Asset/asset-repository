package at.srfg.iasset.repository.model.helper.value.type;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = XSDateSerializer.class)
public class XSDateTime {
	private final ZonedDateTime zonedDate;
	private final LocalDateTime localDateTime;
	
	public static XSDateTime parse(String dateTime) throws DateTimeParseException {
		
		try {
			return new XSDateTime( null, LocalDateTime.parse(dateTime));
		} catch (DateTimeParseException e) {
			try {
				return new XSDateTime( ZonedDateTime.parse(dateTime), null);
			} catch (DateTimeParseException e3) {
				throw e3;
			}
		}
	}
	private XSDateTime(ZonedDateTime zoned, LocalDateTime local) {
		if (Objects.isNull(zoned) && Objects.isNull(local)) {
			throw new IllegalArgumentException("XSDateTime not initialized!");
		}
		this.zonedDate = zoned;
		this.localDateTime = local;
	}
	public Long toEpochMilli() {
		if (Objects.nonNull(zonedDate) ) return zonedDate.toInstant().toEpochMilli();
		if (Objects.nonNull(localDateTime) ) return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
		return 0l;
	}
	public String toString() {
		if (Objects.nonNull(zonedDate) ) return zonedDate.toString();
		if (Objects.nonNull(localDateTime) ) return localDateTime.toString();
		return "";
	}
}

