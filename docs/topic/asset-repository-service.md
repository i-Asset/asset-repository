## i-Asset Repository Service

This project provides the AAS backend for all of the connectors in order to:
- store TEMPLATEs for Asset Administration Shell, Submodel with the `asset-repository-mongo`component.
- host directory information for AAS/Submodel INSTANCES including their REST-Endpoint.
- interlink with the `semantic-lookup-service` which provides an IEC 61360 compatible taxonomy.

Technically, this (micro-)service is built with Spring Boot and can be deployed on-premise or in a cloud environment!

