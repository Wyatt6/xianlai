
FROM openjdk:17.0.2

ARG VERSION=latest
ARG GATEWAY_VER=latest
ARG COMMON_VER=latest
ARG IAM_VER=latest

ENV TZ=Asia/Shanghai
ENV XIANLAI_VERSION=${VERSION}
ENV XIANLAI_APP_GATEWAY_VERSION=${GATEWAY_VER}
ENV XIANLAI_APP_COMMON_VERSION=${COMMON_VER}
ENV XIANLAI_APP_IAM_VERSION=${IAM_VER}

WORKDIR /app

ADD ./xianlai-app-gateway/target/xianlai-app-gateway-${GATEWAY_VER}.jar /app/xianlai-app-gateway/application.jar
ADD ./xianlai-app-common/target/xianlai-app-common-${COMMON_VER}.jar /app/xianlai-app-common/application.jar
ADD ./xianlai-app-iam/target/xianlai-app-iam-${IAM_VER}.jar /app/xianlai-app-iam/application.jar

VOLUME ["/app/xianlai-app-gateway/application-run.properties", "/app/xianlai-app-gateway/log"]
VOLUME ["/app/xianlai-app-common/application-run.properties", "/app/xianlai-app-common/log"]
VOLUME ["/app/xianlai-app-iam/application-run.properties", "/app/xianlai-app-iam/log"]

ADD ./build_scripts/startup_apps.sh /app/startup_apps.sh
RUN chmod +x /app/startup_apps.sh

ENTRYPOINT ["/app/startup_apps.sh"]

EXPOSE 30000 30001 30002
