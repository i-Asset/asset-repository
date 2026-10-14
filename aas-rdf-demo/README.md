# atomic-connector 

## Purpose

Demonstrator reading configured Excel-Files and publishing them as AAS (Sub)Model

## Build from source

#### Prerequisites: Download an install the asset-repository (dependencies)

Ensure, [Maven](https://maven.apache.org/download.cgi) is available. 

Download and build the Asset-Repository (https://github.com/i-Asset/asset-repository)

```
git clone https://github.com/i-Asset/asset-repository
```

Change the directory to `asset-repository`

Switch to development-Branch of the source code!

```
git switch development
```

## Build the dependencies

In the `asset-directory` folder, run maven install

```
mvn install
```

This will create the dependency jars in your own maven repository (Folder .m2 in your user home)

### Build atomic-connector

Run maven install

 ```
mvn install
```
This will create the runnable jar file in the target-Directory of the project

### Run single jar file

```
java -jar target/atomic-connector-0.1.0-SNAPSHOT-jar-with-dependencies.jar
```

### Customize 

In order to customize the demonstrator, create an "app.properties" file and store it in the same directory as the 
runnable jar file.



#### Customizable options
Options for the HTTP-Service-Endpoint

- `endpoint.context` = &lt;context path prefix&lt; defaults to `/`
- `endpoint.port` = &lt;port where the data is provided&lt; defaults to `5100`

Options for the XLSX files

- `xlsx.file.name` = &lt;file_name&gt;
- `xlsx.file.sheet.name` = &lt;Name of the Sheet in the XLSX-file&gt; - Example: `Tabelle1`
- `xlsx.file.sheet.idColumn` = &lt;Index (zero based) of the column holding the identifying data per row!&gt; - Example `1`
- `xlsx.file.trackChanges` = &lt;true | false&gt; When *true*,  changes of the file are detected in order to reload the content on changes
- `xlsx.file.aas.id` = &lt;ID of the Asset Administration Shell&gt; - Example: `urn:atomic:xlsx:fileX`
- `xlsx.file.aas.name` = &lt;Human readable name of the Asset administration Shell&gt; - Example: `Kennwertliste`
- `xlsx.file.submodel.id` = &lt;ID of the Submodel holding the loaded data&gt; - Example: `urn:atomic:xlsx:fileX:data`
- `xlsx.file.submodel.name` = &lt;Human readable name of the Submodel holding the loaded data&gt; - Example: `Kennwertliste XYZ`

### Assumptions

- The first row of the XLSX-File holds the names of the attributes
- The names of the attributes may denote the unit of the value in brackets, eg. `Millimeter (mm)`, `Kilogramm (kg)` or Pound `(pound)`
- The names of the attributes are used for the `idShort`of each SubmodelElement


## Acccessing the Data

Running the jar file will expose the AAS-Shell-Service at the configured port. You may point the browser to

```
http://localhost:5100/shells
```
in order to list the available Asset Administration Shells. The output will look like the following:

```
[
  {
    "modelType": "AssetAdministrationShell",
    "assetInformation": null,
    "submodels": [
      {
        "type": "ModelReference",
        "keys": [
          {
            "type": "Submodel",
            "value": "urn:atomic:kennwertliste:langlauf"
          }
        ]
      }
    ],
    "id": "urn:atomic:kennwertliste",
    "displayNames": [
      {
        "language": "de",
        "text": "Kennwertliste Langlauf"
      }
    ]
  }
]
```
the id's and displayName will correspond to the configured settings.

Point the browser to

```
http://localhost:5100/shells/{{aasIdentifier-base64Encoded}}
```
to access the single Asset Administration Shell (omitting the square brackets). You may use the online [Base64 Encoder](https://www.base64encode.org/) to obtain the encoded AAS Identifier using `xlsx.file.aas.id` from the configuration as input!

The data is however contained in the submodel which may be accessed as follows

```
http://localhost:5100/shells/{{aasIdentifier-base64Encoded}}/submodels/{{submodelIdentifier-base64Encoded}}
```
Again, the configured submodel identifier (config value `xlsx.file.submodel.id`) is taken as input to obtain the base64-Encoded value. The result will look like the following example

```
"modelType" : "Submodel",
  "kind" : "Instance",
  "id" : "urn:atomic:kennwertliste:langlauf",
  "displayNames" : [ {
    "language" : "de",
    "text" : "Kennwerte Teilmodell"
  } ],
  "submodelElements" : [ {
  ... omitted for brevity ...
  } ]
```
The raw data is organized in `SubmodelElementCollection` elements for each data row. Each collection holds the data elements as `Property` which can be directly accessed with 

```
http://localhost:5100/shells/{{aasIdentifier-base64Encoded}}/submodels/{{submodelIdentifier-base64Encoded}}/submodel-elements/{{idShort-Path}}
```
where element's hierarchy is indicated with the idShort path! All `idShort` Paths are concatenated with a dot (Punkt). Please note, the `idShort` Path is not Base64-Encoded!



