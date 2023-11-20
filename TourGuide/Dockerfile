FROM bellsoft/liberica-openjdk-alpine:latest
ADD build/libs/tourGuide-1.0.0.jar tourGuide-1.0.0.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","tourGuide-1.0.0.jar"]