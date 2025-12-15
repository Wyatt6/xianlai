# TODO CBR: 每次发版前检查模块版本号
xianlai_ver=0.1.0
xianlai_app_gateway_ver=1.0.0
xianlai_app_common_ver=1.0.0
xianlai_app_iam_ver=1.0.0

# 分布式镜像
docker build \
  --platform linux/amd64 \
  --build-arg VERSION=${xianlai_app_gateway_ver} \
  -t wyatt6/xianlai-app-gateway:${xianlai_app_gateway_ver} \
  ../xianlai-app-gateway/

docker build \
  --platform linux/amd64 \
  --build-arg VERSION=${xianlai_app_common_ver} \
  -t wyatt6/xianlai-app-common:${xianlai_app_common_ver} \
  ../xianlai-app-common/

docker build \
  --platform linux/amd64 \
  --build-arg VERSION=${xianlai_app_iam_ver} \
  -t wyatt6/xianlai-app-iam:${xianlai_app_iam_ver} \
  ../xianlai-app-iam/

# 集中式镜像
docker build \
  --platform linux/amd64 \
  --build-arg VERSION=${xianlai_ver} \
  --build-arg GATEWAY_VER=${xianlai_app_gateway_ver} \
  --build-arg COMMON_VER=${xianlai_app_common_ver} \
  --build-arg IAM_VER=${xianlai_app_iam_ver} \
  -t wyatt6/xianlai:${xianlai_ver} \
  ../

