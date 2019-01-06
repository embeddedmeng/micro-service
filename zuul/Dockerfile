FROM hub.c.163.com/library/java:8-jre
MAINTAINER MengXY <embeddedmeng@163.com>

ADD ./target/zuul.jar /app/
#EXPOSE 80

ENTRYPOINT ["java", "-jar", "/app/zuul.jar"]
