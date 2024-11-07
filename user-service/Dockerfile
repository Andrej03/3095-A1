## ---------------
## BUILD STAGE
## ---------------
#
#FROM gradle:8-jdk23-alpine AS builder
#
#COPY --chown=gradle:gradle . /home/gradle/src
#
#WORKDIR /home/gradle/src
#
#RUN gradle build -x test
#
## ---------------
## PACKAGE STAGE
## ---------------
#FROM openjdk:23-jdk
#
#RUN mkdir /app
#
#COPY --from=builder /home/gradle/src/build/libs/*.jar /app/user-service.jar
#
## Use environment variables for PostgreSQL credentials
#ENV POSTGRES_URL=jdbc:postgresql://postgres-user-service:5432/userdb \
#    POSTGRES_USER=admin
#    POSTGRES_PASSWORD=password
#
#EXPOSE 5432
#
#ENTRYPOINT ["java", "-jar", "/app/user-service.jar"]
