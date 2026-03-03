
FROM openjdk:17.0.2

ARG VERSION=latest

ENV TZ=Asia/Shanghai
ENV XIANLAI_MOD_TOOLKIT_VERSION=${VERSION}

WORKDIR /app

ADD ./target/xianlai-mod-toolkit-${VERSION}.jar /app/application.jar

ENTRYPOINT ["java", "-jar", "/app/application.jar", "--spring.profiles.active=run"]

VOLUME ["/app/application-run.yml", "/app/log"]

EXPOSE 30003
