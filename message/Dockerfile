FROM hub.c.163.com/library/java:8-jre
MAINTAINER MengXY <embeddedmeng@163.com>

ADD ./target/message.jar /app/
#EXPOSE 7000

ENTRYPOINT ["java", "-jar", "/app/message.jar"]
