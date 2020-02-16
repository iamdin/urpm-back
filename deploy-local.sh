#!/usr/bin/env sh

# 确保脚本抛出遇到的错误
#set -e

# 停止已有容器
docker stop $(docker ps -a |  grep "urpmback"  | awk '{print $1}')

# 删除已有容器
docker rm $(docker ps -a |  grep "urpmback"  | awk '{print $1}')

# 删除已有镜像
docker rmi $(docker images -a |  grep "urpmback"  | awk '{print $1}')

# 创建docker镜像
docker build . -t urpmback

# 运行docker
docker run -e -d "SPRING_PROFILES_ACTIVE=dev" --name urpmback -p 8088:8088 urpmback