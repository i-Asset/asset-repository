FROM eclipse-temurin:25-alpine
ARG JAR_FILE=target/wintersteiger-connector*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
