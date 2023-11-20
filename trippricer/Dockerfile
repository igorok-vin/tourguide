FROM bellsoft/liberica-openjdk-alpine:latest
ADD build/libs/tripPricer-1.0.0.jar tripPricer-1.0.0.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","tripPricer-1.0.0.jar"]