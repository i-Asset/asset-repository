# asset-repository
Asset Administration Shell Repository is the development environment for the i-Twin Project.

## Getting started

### Build Dependencies and Install to Local Maven Repository

The `asset-repository` depends on Java-Models of the AAS. As long as they are not available from Maven Central, they need to be built and installed locally. 

#### Step 1 (optional)
If  a new RDF-Model is available, it is possible to generate your own (Java-)models from RDF. Usually the latest version of the required Java-Model will be available in the repo `aas4j` (see below)
```bash
# to be done in a separate folder
$ git clone git@github.com:i-Asset/aas4j-model-generator
$ cd aas4j-model-generator
$ git checkout feature/update-to-3.0.RC02
$ mvn clean package -P i-Asset
```

#### Step 2 checkout existing java model and install to local maven repository

```bash
# to be done in a separate folder
$ git clone git@github.com:i-Asset/aas4j
$ git checkout development
$ mvn clean install -DskipTests
```

### Build and run the asset-repository service

The service depends on a running MongoDB instance, which has two configuration values:
* MONGODB_URL, e.g. `mongodb://root:example@localhost:27017`
* MONGODB_NAME, e.g. `asset`


```bash
# this repository
$ git clone git@github.com:i-Asset/asset-repository
$ cd asset-repository
$ git checkout development
$ mvn clean install -DskipTests
# run app (mongodb config required!)
$ cd asset-repository-service
$ mvn spring-boot:run
```