#stage 1
#start with a base image containing java runtime
FROM openjdk:11-slim as build

#add maintainer INFO
LABEL maintainer="Yafet Kubrom : yafet.kubrom@gmail.com"

#Running applications with user privileges helps to mitigate some risks
#To run the application as a non-root user
#RUN addgroup -S spring && adduser -S spring -G spring
#USER spring:spring

#the application's jar file
ARG JAR_FILE=target/*.jar

# Add the application's jar to the container
COPY ${JAR_FILE} app.jar

#unpackage jar file
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf /app.jar)
## Unpacks the app.jar copied previously into the filesystem of the build image

#stage 2
## This new image contains the different layers of a Spring Boot app instead of the complete JAR file.

#Same Java runtime
#FROM openjdk:11-slim

#Add volume pointing to /tmp
#VOLUME /tmp

#Running applications with user privileges helps to mitigate some risks
#To run the application as a non-root user
#RUN addgroup -S spring && adduser -S spring -G spring
#USER spring:spring

##Copies the different layers from the first image named build
#Copy unpackaged application to new container
ARG DEPENDENCY=/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

#execute the application
ENTRYPOINT ["java","-cp","app:app/lib/*","com.optimagrowth.licensingservice.LicensingServiceApplication"]
## Targets the licensing service in the image when the container is created


