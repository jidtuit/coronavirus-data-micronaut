FROM openjdk:14-alpine
COPY build/libs/coviddata-*-all.jar coviddata.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "coviddata.jar"]
