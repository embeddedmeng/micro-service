#!/usr/bin/env bash
# 停止所有容器
docker stop $(docker ps -a -q)
# 删除所有容器
docker rm $(docker ps -a -q)
# 删除所有镜像
docker rmi $(docker images -q)
# 重新打jar包
mvn clean package -Dmaven.test.skip=true -U
# 创建容器
#docker-compose -f docker-compose.yml up -d