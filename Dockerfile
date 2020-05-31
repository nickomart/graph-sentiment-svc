FROM openjdk:8

ARG BUILD_JAR_VER=0.0.1-SNAPSHOT

ADD build/libs/reporting-service-${BUILD_JAR_VER}.jar app.jar
EXPOSE 8081

ENV NEO4J_HOST neo4j
ENV MONGO_HOST mongodb

ENTRYPOINT ["java","-Djava.security.edg=file:/dev/./urandom","-Dspring.profiles.active=dev","-jar","/app.jar"]
