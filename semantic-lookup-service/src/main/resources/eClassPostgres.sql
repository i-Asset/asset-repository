SET search_path = public;
SET client_encoding = 'UTF8';

CREATE TABLE eClass_Classification_Class (
	Supplier VARCHAR(6),
	IdCC VARCHAR(9),
	Identifier VARCHAR(6),
	VersionNumber VARCHAR(3),
	VersionDate DATE,
	RevisionNumber VARCHAR(2),
	CodedName VARCHAR(8),
	PreferredName VARCHAR(80),
	Definition VARCHAR(1023),
	ISOLanguageCode VARCHAR(2),
	ISOCountryCode VARCHAR(2),
	Note VARCHAR(1023),
	Remark VARCHAR(1023),
	Level INTEGER,
	MKSubclass BOOLEAN,
	MKKeyword BOOLEAN,
	IrdiCC VARCHAR(20) PRIMARY KEY
);


CREATE TABLE eClass_Unit (
	StructuredNaming VARCHAR(1000),
	ShortName VARCHAR(1000),
	Definition VARCHAR(1000),
	Source VARCHAR(1000),
	Comment VARCHAR(1000),
	SINotation VARCHAR(1000),
	SIName VARCHAR(1000),
	DINNotation VARCHAR(1000),
	ECEName VARCHAR(1000),
	ECECode VARCHAR(3),
	NISTName VARCHAR(1000),
	IECClassification VARCHAR(1000),
	IrdiUN VARCHAR(20) PRIMARY KEY,
	NameOfDedicatedQuantity VARCHAR(1000)
);


CREATE TABLE eClass_Property (
	Supplier VARCHAR(6),
	IdPR VARCHAR(9),
	Identifier VARCHAR(6),
	VersionNumber VARCHAR(3),
	VersionDate DATE,
	RevisionNumber VARCHAR(2),
	PreferredName VARCHAR(80),
	ShortName VARCHAR(29),
	Definition VARCHAR(1023),
	SourceOfDefinition VARCHAR(1023),
	Note VARCHAR(1023),
	Remark VARCHAR(1023),
	PreferredSymbol VARCHAR(17),
	IrdiUN VARCHAR(20) REFERENCES eClass_Unit(IrdiUN) ON UPDATE RESTRICT ON DELETE RESTRICT,
	ISOLanguageCode VARCHAR(2),
	ISOCountryCode VARCHAR(2),
	Category VARCHAR(3),
	AttributeType VARCHAR(8),
	DefinitionClass VARCHAR(255),
	DataType VARCHAR(19),
	IrdiPR VARCHAR(20) PRIMARY KEY,
	CurrencyAlphaCode VARCHAR(3)
);



CREATE TABLE eClass_Keyword_Synonym (
	SupplierKWSupplierSY VARCHAR(6),
	Identifier VARCHAR(6),
	VersionNumber VARCHAR(3),
	IdCCIdPR VARCHAR(9),
	KeywordValueSynonymValue VARCHAR(80),
	Explanation VARCHAR(255),
	ISOLanguageCode VARCHAR(2),
	ISOCountryCode VARCHAR(2),
	TypeOfTargetSE VARCHAR(2),
	IrdiTarget VARCHAR(20),
	IrdiKWIrdiSY VARCHAR(20),
	TypeOfSE VARCHAR(2),
	PRIMARY KEY (IrdiTarget, IrdiKWIrdiSY)
);


CREATE TABLE eClass_Value (
	Supplier VARCHAR(6),
	IdVA VARCHAR(9),
	Identifier VARCHAR(6),
	VersionNumber VARCHAR(3),
	RevisionNumber VARCHAR(2),
	VersionDate DATE,
	PreferredName VARCHAR(80),
	ShortName VARCHAR(17),
	Definition VARCHAR(1023),
	Reference VARCHAR(1023),
	ISOLanguageCode VARCHAR(2),
	ISOCountryCode VARCHAR(2),
	IrdiVA VARCHAR(20) PRIMARY KEY,
	DataType VARCHAR(19)
);


CREATE TABLE  eClass_Classification_Class_Property (
	SupplierIdCC VARCHAR(6),
	IdCC VARCHAR(9),
	ClassCodedName VARCHAR(8),
	SupplierIdPR VARCHAR(6),
	IdPR VARCHAR(9),
	IrdiCC VARCHAR(20) REFERENCES eClass_Classification_Class (IrdiCC) ON UPDATE RESTRICT ON DELETE CASCADE,
	IrdiPR VARCHAR(20) REFERENCES eClass_Property (IrdiPR) ON UPDATE RESTRICT ON DELETE CASCADE,
	PRIMARY KEY(IrdiCC, IrdiPR)
);


CREATE TABLE  eClass_Classification_Class_Property_Value (
	IrdiCC VARCHAR(20) REFERENCES eClass_Classification_Class (IrdiCC) ON UPDATE RESTRICT ON DELETE CASCADE,
	IrdiPR VARCHAR(20) REFERENCES eClass_Property (IrdiPR) ON UPDATE RESTRICT ON DELETE CASCADE,
	IrdiVA VARCHAR(20) REFERENCES eClass_Value (IrdiVA) ON UPDATE RESTRICT ON DELETE CASCADE,
	Value_Constraint VARCHAR (5),
	PRIMARY KEY(IrdiCC, IrdiPR, IrdiVA)
);


CREATE TABLE eClass_Property_Value (
	IrdiPR VARCHAR(20) REFERENCES eClass_Property (IrdiPR) ON UPDATE RESTRICT ON DELETE CASCADE,
	IrdiVA VARCHAR(20) REFERENCES eClass_Value (IrdiVA) ON UPDATE RESTRICT ON DELETE CASCADE,
	PRIMARY KEY(IrdiPR, IrdiVA)
);