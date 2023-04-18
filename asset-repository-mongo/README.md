# asset-repository-mongo
NoSQL Persistence for Asset Administration Shell Repository using MongoDB!

## Getting started

### Build Dependencies and Install to Local Maven Repository

The `asset-repository-mongo` depends on Java-Models of the AAS. As long as they are not available from Maven Central, they need to be built and installed locally. It also depends on the `asset-repository-api` which provides the required interfaces to implement, e.g. the `at.srfg.iasset.repository.component.Persistence` interface which in turn extends the `Environment`interface from the Java Models API. 

### Step 1 - Build

This project is part of the parent build process!


## Startup procedure

During startup of theh `asset-repository-service`, this component is activated and connects with the configured MongoDB and inserts a bunch of test data.


