
FROM openjdk:17.0.2

ARG VERSION=latest

ENV TZ=Asia/Shanghai
ENV XIANLAI_APP_COMMON_VERSION=${VERSION}

WORKDIR /app

ADD ./target/xianlai-app-common-${VERSION}.jar /app/xianlai-app-common/application.jar

VOLUME ["/app/xianlai-app-common/application-run.yml", "/app/xianlai-app-common/log"]

ENTRYPOINT ["java", "-jar", "/app/xianlai-app-common/application.jar", "--spring.profiles.active=run"]

EXPOSE 30001
