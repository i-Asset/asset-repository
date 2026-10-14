#!/bin/bash

set -e

# Warning: Use Dietmar's generator instead for producing
#    AAS Files that can support the jsonld-representations

MODEL_FILE=${1:-input.ttl}
PACKAGE_NAME=${2:-com.copadata.aas2rdf.model.samm.generated} # e.g. "at.srfg.passat.model.samm.generated"
SAMM_JAR_TAG="v2.14.0"
SAMM_JAR="samm-cli-2.14.0.jar"

if [ ! -f "$MODEL_FILE" ]; then
  echo "$MODEL_FILE does not exist. Hint: provide path to the ttl-file as parameter."
  echo "Cannot generate API. Exiting."
  exit
fi

if [ ! -f "$SAMM_JAR" ]; then
  echo "$SAMM_JAR does not exist."
  read -r -p "Download from github? (Y/n)" yn
  [ -z "$yn" ] && yn="Y"  # if 'yes' have to be default choice
  case $yn in
      [Yy]* ) ;;
      * ) echo "Cannot generate API. Exiting."; exit;;
  esac

  wget https://github.com/eclipse-esmf/esmf-sdk/releases/download/$SAMM_JAR_TAG/$SAMM_JAR --output-document=$SAMM_JAR

fi

BASEURL="http://localhost/copadata.aas"
MODEL_DIRECTORY="src/main/resources/"
JAVA_SRC_DIRECTORY="src/main/java/"

java -Dpolyglotimpl.DisableMultiReleaseCheck=true -jar $SAMM_JAR aspect $MODEL_FILE to openapi --api-base-url $BASEURL --output $MODEL_DIRECTORY$MODEL_FILE".yml"
java -Dpolyglotimpl.DisableMultiReleaseCheck=true -jar $SAMM_JAR aspect $MODEL_FILE to jsonld --output $MODEL_DIRECTORY$MODEL_FILE".jsonld"
java -Dpolyglotimpl.DisableMultiReleaseCheck=true -jar $SAMM_JAR aspect $MODEL_FILE to aas --output $MODEL_DIRECTORY$MODEL_FILE"-aas.xml"
java -Dpolyglotimpl.DisableMultiReleaseCheck=true -jar $SAMM_JAR aspect $MODEL_FILE to aas --format=json  --output $MODEL_DIRECTORY$MODEL_FILE"-aas.json"
java -Dpolyglotimpl.DisableMultiReleaseCheck=true -jar $SAMM_JAR aspect $MODEL_FILE to json --output $MODEL_DIRECTORY$MODEL_FILE".json" # example json
java -Dpolyglotimpl.DisableMultiReleaseCheck=true -jar $SAMM_JAR aspect $MODEL_FILE to asyncapi --output $MODEL_DIRECTORY$MODEL_FILE"-async.yml"
java -Dpolyglotimpl.DisableMultiReleaseCheck=true -jar $SAMM_JAR aspect $MODEL_FILE to java --output-directory $JAVA_SRC_DIRECTORY --package-name $PACKAGE_NAME

echo "Models saved to $MODEL_DIRECTORY"
echo "API saved to $JAVA_SRC_DIRECTORY"
