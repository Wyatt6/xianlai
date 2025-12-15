
FROM openjdk:17.0.2

ARG VERSION=latest

ENV TZ=Asia/Shanghai
ENV XIANLAI_APP_IAM_VERSION=${VERSION}

WORKDIR /app

ADD ./target/xianlai-app-iam-${VERSION}.jar /app/xianlai-app-iam/application.jar

VOLUME ["/app/xianlai-app-iam/application-run.properties", "/app/xianlai-app-iam/log"]

ENTRYPOINT ["java", "-jar", "/app/xianlai-app-iam/application.jar", "--spring.profiles.active=run"]

EXPOSE 30002
