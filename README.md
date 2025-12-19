# XianLai

`v0.2.0`

坚持不容易，请给作者点点 ⭐️Star⭐️ 鼓励鼓励。

## 简介

XianLai 是一款基于 Vue3 + ElementPlus 和 Spring Cloud Alibaba 的开源、轻量的多功能后台管理系统。它采用分布式微服务架构，支持配置化 UI 页面、身份识别和访问管理（IAM），同时也提供了工作生活中很多很有用的功能板块，能够帮助你提高工作效率、理顺家庭生活。

你可以按照本文档说明部署 XianLai 系统后直接使用它提供的功能。当然你也可以基于 XianLai 系统的配置化 UI 页面、身份识别和访问管理（IAM）等基础功能，快速搭建你自己的后台管理系统，开发你想要的个性化功能。

本仓库是后端部分，前端请去往：[https://github.com/Wyatt6/xianlai-vue](https://github.com/Wyatt6/xianlai-vue)

## 招贤榜

一个人的力量是有限的，团体的力量是无穷的。我们还有很多有趣的、可能对大家有帮助的想法有待实现。欢迎各位对本项目感兴趣的开发者参与进来，共同参与产品研发和项目建设，同时交流探讨技术一起进步。由于我们稍微有点完美主义，在用户体验和代码质量上的要求较高，请多担待。志同道合者请联系：wyatt6@163.com。

## 系统功能清单

- **技术网关**：根据 URL 的流量分发；请求跟踪 ID；基于 Nacos 实现的动态路由配置；

- **公共功能**：参数选项管理；数据初始化；验证码生成和校验；后端接口管理；前端路径常量管理；前端管理台菜单管理；前端页面路由管理；

- **身份识别和访问管理**：权限管理；角色管理，包括为角色授权；用户管理，包括用户创建、注册、登录、退出、修改密码、头像管理、信息修改、角色绑定；

- **多功能工具箱**：密码本；

- **核心程序**：提供核心程序，包括公共注解、公共异常类、AOP 处理程序、日志打印程序、SaToken 配置、Feign 配置、Feign 消费者服务声明、响应对象封装类、工具类；

- **访问封装程序**：提供对 Redis 连接的统一配置；

- **访问封装程序**：提供对 MySQL 连接和 Druid 工具的统一配置。

## 后端技术栈

`OpenJDK 17`，`Spring Boot 3.2.4`，`Spring Cloud 2023.0.1`，`Spring Cloud Alibaba 2023.0.1.0`，`Sa-Token 1.44.0`，`MySQL 8.4.7`，`Redis 8.4.0`，`Nacos 3.1.1`等。

## 部署手册

使用 Docker 和 Docker Compose（v2）部署，需提前安装和配置好。XianLai 发布的 Docker 镜像：

- [wyatt6/xianlai-app-gateway](https://hub.docker.com/repository/docker/wyatt6/xianlai-app-gateway/general)
- [wyatt6/xianlai-app-common](https://hub.docker.com/repository/docker/wyatt6/xianlai-app-common/general)
- [wyatt6/xianlai-app-iam](https://hub.docker.com/repository/docker/wyatt6/xianlai-app-iam/general)

### 1. 创建容器网络

```shell
docker network create xianlai_net
```

### 2. 拉取 Redis 1.27.2 镜像并启动容器

具体方法可网上搜索，注意为了提高安全性要给 Redis 设置访问密码。这里的密码需要填入后续步骤的`application-run.yml`配置文件中的相应配置项。

### 3. 拉取 MySQL 8.4.7 镜像并启动容器

具体方法可网上搜索。启动完成后连接数据库实例为 XianLai 系统创建数据库和访问对象并授权：

```sql
create database xianlai;
create user xianlai identified by '设置的密码';
grant all privileges on xianlai.* to xianlai@'%';
flush privileges;
```

这里的用户和密码需要填入后续步骤的`application-run.yml`配置文件中的相应配置项，其中密码需要使用 druid 加密程序（在工程的`build_scripts/`目录下可找到）进行加密生成`password`和`publicKey`：

```shell
java -cp ./druid-1.2.18.jar com.alibaba.druid.filter.config.ConfigTools 上述xianlai用户的密码
```

### 4. 启动 Nacos 3.1.1 容器并配置

具体方法可网上搜索。启动完成后需创建新的命名空间`xianlai`，创建访问用户并设置密码，创建角色绑定给访问用户，给角色授予读写命名空间`xianlai`的权限。这里的用户和密码需要填入后续步骤的`application-run.yml`配置文件中的相应配置项。

创建技术网关动态路由功能读取的配置文件：

- **命名空间**: xianlai
- **Data ID**: gateway-dynamic-routes
- **Group ID**: DEFAULT_GROUP
- **格式**: JSON

初始配置内容如下，后续视情况添加和修改：

```json
[
  {
    "id": "common",
    "order": 1,
    "predicates": [
      {
        "name": "Path",
        "args": { "pattern": "/api/common/**" }
      }
    ],
    "uri": "lb://xianlai-app-common",
    "filters": [
      {
        "name": "RewritePath",
        "args": {
          "regexp": "/api/common/?(?<segment>.*)",
          "replacement": "/${segment}"
        }
      }
    ]
  },
  {
    "id": "iam",
    "order": 2,
    "predicates": [
      {
        "name": "Path",
        "args": { "pattern": "/api/iam/**" }
      }
    ],
    "uri": "lb://xianlai-app-iam",
    "filters": [
      {
        "name": "RewritePath",
        "args": {
          "regexp": "/api/iam/?(?<segment>.*)",
          "replacement": "/${segment}"
        }
      }
    ]
  },
  {
    "id": "toolkit",
    "order": 3,
    "predicates": [
      {
        "name": "Path",
        "args": { "pattern": "/api/toolkit/**" }
      }
    ],
    "uri": "lb://xianlai-mod-toolkit",
    "filters": [
      {
        "name": "RewritePath",
        "args": {
          "regexp": "/api/toolkit/?(?<segment>.*)",
          "replacement": "/${segment}"
        }
      }
    ]
  }
]
```

### 5. 将 Redis、MySQL、Nacos 容器加入容器网络

```shell
docker network connect xianlai_net redis容器名
docker network connect xianlai_net mysql容器名
docker network connect xianlai_net nacos容器名
```

### 6. 创建目录和准备配置文件

在服务器上为 XianLai 的部署创建如下结构的目录，并将各个模块的配置文件上传到目标位置：

```
+ xianlai/
|       .env
|       docker-compose.yml
|       + xianlai-app-gateway/
|       |       application-run.yml
|       |       + log/
|       + xianlai-app-common/
|       |       application-run.yml
|       |       + log/
|       + xianlai-app-iam/
|       |       application-run.yml
|       |       + log/
|       + xianlai-mod-toolkit/
|       |       application-run.yml
|       |       + log/
```

### 7. 启动容器编排

进入上述的`xianlai/`目录中，执行以下命令启动容器编排：

```shell
docker compose up -d
```

启动后前端请求就可以发送到技术网关的`30000`端口，交给分布式的 XianLai 系统进行处理。超级管理员初始用户名和密码都是`superadmin`，请及时修改密码。

其他可能用到的 Docker Compose 命令：

```shell
docker compose up       创建并启动容器编排
docker compose down     停止并删除容器编排
docker compose start    启动容器编排
docker compose restart  重启容器编排
docker compose stop     停止容器编排
docker compose ps       显示容器编排列表
```

## 更新版本

1. 上传新版本的`.env`和`docker-compose.yml`文件到目标位置；
2. 进入`xianlai/`目录下；
3. 执行`docker compose down`停止并删除旧的容器编排；
4. 执行`docker compose up -d`创建并启动新版本的容器编排。

## 附录 A：模块版本号对照表

XianLai 当前版本号：`v0.2.0`

| 模块                | 名称               | 模块版本 | XianLai 版本 |
| ------------------- | ------------------ | -------- | ------------ |
| xianlai-app-gateway | 技术网关           | 1.0.0    | 0.1.0+       |
| xianlai-app-common  | 公共功能           | 1.0.0    | 0.1.0        |
| xianlai-app-common  | 公共功能           | 1.1.0    | 0.2.0        |
| xianlai-app-iam     | 身份识别和访问管理 | 1.0.0    | 0.1.0        |
| xianlai-app-iam     | 身份识别和访问管理 | 1.1.0    | 0.2.0        |
| xianlai-mod-toolkit | 工具箱模组         | 1.0.0    | 0.2.0        |
| xianlai-core        | 核心程序           | 1.0.0    | 0.1.0+       |
| xianlai-infra-redis | Redis 访问封装程序 | 1.0.0    | 0.1.0+       |
| xianlai-infra-mysql | MySQL 访问封装程序 | 1.0.0    | 0.1.0+       |
