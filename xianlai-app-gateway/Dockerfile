
FROM openjdk:17.0.2

ARG VERSION=latest

ENV TZ=Asia/Shanghai
ENV XIANLAI_APP_GATEWAY_VERSION=${VERSION}

WORKDIR /app

ADD ./target/xianlai-app-gateway-${VERSION}.jar /app/application.jar

ENTRYPOINT ["java", "-jar", "/app/application.jar", "--spring.profiles.active=run"]

VOLUME ["/app/application-run.yml", "/app/log"]

EXPOSE 30000
