FROM hub.c.163.com/library/java:8-jre
MAINTAINER MengXY <embeddedmeng@163.com>

ADD ./target/user.jar /app/
#EXPOSE 8001
# 用CMD无法传递参数
ENTRYPOINT ["java", "-jar", "/app/user.jar"]
