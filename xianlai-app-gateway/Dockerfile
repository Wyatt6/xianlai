
FROM openjdk:17.0.2

ARG VERSION=latest

ENV TZ=Asia/Shanghai
ENV XIANLAI_APP_GATEWAY_VERSION=${VERSION}

WORKDIR /app

ADD ./target/xianlai-app-gateway-${VERSION}.jar /app/xianlai-app-gateway/application.jar

VOLUME ["/app/xianlai-app-gateway/application-run.yml", "/app/xianlai-app-gateway/log"]

ENTRYPOINT ["java", "-jar", "/app/xianlai-app-gateway/application.jar", "--spring.profiles.active=run"]

EXPOSE 30000
