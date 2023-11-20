FROM bellsoft/liberica-openjdk-alpine:latest
ADD build/libs/gpsUtil-1.0.0.jar gpsUtil-1.0.0.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","gpsUtil-1.0.0.jar"]