FROM eclipse-temurin:23_37-jre-alpine
ARG JAR=build/libs/identity-hub-cx.jar
ARG OTEL_JAR
ARG ADDITIONAL_FILES

ARG APP_USER=docker
ARG APP_UID=10100

RUN addgroup --system "$APP_USER"

RUN adduser \
     --shell /sbin/nologin \
     --disabled-password \
     --gecos "" \
     --ingroup "$APP_USER" \
     --no-create-home \
     --uid "$APP_UID" \
     "$APP_USER"

WORKDIR /app

RUN mkdir /app/logs

USER "$APP_USER"

COPY ${JAR} identity-hub-cx.jar
COPY ${ADDITIONAL_FILES} ./
COPY main/resources/application.properties configuration.properties

HEALTHCHECK NONE

ENTRYPOINT ["java", \
     "-Djava.util.logging.config.file=/app/logging.properties", \
     "-Djava.security.egd=file:/dev/urandom", \
     "-jar", \
     "identity-hub-cx.jar"]