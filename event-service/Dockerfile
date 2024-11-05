# ---------------
# BUILD STAGE
# ---------------

FROM gradle:8-jdk23-alpine AS builder

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle build -x test

# ---------------
# PACKAGE STAGE
# ---------------
FROM openjdk:23-jdk

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/event-service.jar

# Use environment variables for MongoDB credentials
#ENV MONGO_DB_USERNAME=admin \
#    MONGO_DB_PWD=password \
#    SPRING_DATA_MONGODB_URI=mongodb://${MONGO_DB_USERNAME}:${MONGO_DB_PWD}@mongo:27017/eventdb

EXPOSE 8088

ENTRYPOINT ["java", "-jar", "/app/event-service.jar"]
