# semantic-lookup-service
Spring-Boot Micro-Service providing a IEC 61360 compatible taxonomy. The data may be stored in the taxonomy covering hierarchical classes with parent child relationships, properties, property units and coded property values.

## requirements

A running PostgreSQL Database is required. The system allows the integration of eCl@ss data exports - for using eCl@ss data, the respective Database Schema can be found in 

```
./src/main/resources/eClassPostgres.sql
```
and the eClass CSV (Downloads from eClass Sites) files must be loaded into the database. See [instructions](http://www.postgresqltutorial.com/import-csv-file-into-posgresql-table/) on how to import CSV files.

## data 

The tables for storing the taxonomy are created on startup. See the [swagger documentation](http://www.localhost:8084/swagger-ui.html) for using the services (when running the system locally). 

## indexing

When running in a cloud environment, the system integrates with an instance of the [solr-indexing](https://github.com/i-Asset/solr-indexing) service. When active, each change of the taxonomy data causes a reindexing of the respective with the indexing service. 

### Service build and startup

 ```
 mvn clean spring-boot:run
 ```

  
 The Service may be verified online [iAsset-Staging](http://iasset.salzburgresearch.at/semantic-lookup-service/actuator/info)
 
 Disable SOLR autloading
