FROM openjdk:11.0.9.1-jre
ADD /target/api-empresa-*.jar api-empresa.jar
ENTRYPOINT ["java", "-jar", "api-empresa.jar"]