# TODO CBR: 每次发版前检查模块版本号
xianlai_gateway_ver=1.0.0
xianlai_system_ver=1.1.0
xianlai_plugin_toolkit_ver=1.0.1

# 分布式镜像
docker build \
  --platform linux/amd64 \
  --build-arg VERSION=${xianlai_gateway_ver} \
  -t wyatt6/xianlai-gateway:${xianlai_gateway_ver} \
  ../xianlai-gateway/

docker build \
  --platform linux/amd64 \
  --build-arg VERSION=${xianlai_system_ver} \
  -t wyatt6/xianlai-system:${xianlai_system_ver} \
  ../xianlai-system/

docker build \
  --platform linux/amd64 \
  --build-arg VERSION=${xianlai_plugin_toolkit_ver} \
  -t wyatt6/xianlai-plugin-toolkit:${xianlai_plugin_toolkit_ver} \
  ../xianlai-plugin-toolkit/
