FROM openjdk:11.0.9.1-jre
EXPOSE 8080
ADD /target/api-empresa-*.jar api-empresa.jar
ENTRYPOINT ["java", "-jar", "api-empresa.jar"]
