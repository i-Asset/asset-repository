package at.srfg.iasset.semantic.model;

public enum DataTypeEnum {
	STRING,
	STRING_TRANSLATABLE,
	INTEGER_COUNT,
	INTEGER_MEASURE,
	INTEGER_CURRENCY,
	REAL_COUNT,
	REAL_MEASURE,
	REAL_CURRENCY,
	RATIONAL,
	RATIONAL_MEASURE,
	BOOLEAN,
	DATE,
	URL,
	TIME,
	TIMESTAMP
	;
	
	public static DataTypeEnum fromString(String s) {
		try {
			return DataTypeEnum.valueOf(s);
		} catch (Exception e) {
			return DataTypeEnum.STRING;
		}
	}
}
