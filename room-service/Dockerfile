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

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/room-service.jar

# Use environment variables for PostgreSQL credentials
#ENV POSTGRES_DB=your_db_name \
#    POSTGRES_USER=your_username \
#    POSTGRES_PASSWORD=your_password

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "/app/room-service.jar"]
