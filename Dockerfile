FROM openjdk:8

# 时区设置
RUN echo "Asia/shanghai" > /etc/timezone \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

VOLUME /tmp

ADD target/DistributedId.jar app.jar
RUN bash -c 'touch /app.jar'

EXPOSE 16830
EXPOSE 16831

#ENV JAVA_OPTS="\
#-server \
#-Xmx4g \
#-Xms4g \
#-Xmn2g \
#-XX:SurvivorRatio=8 \
#-XX:MetaspaceSize=256m \
#-XX:MaxMetaspaceSize=512m \
#-XX:+UseParallelGC \
#-XX:ParallelGCThreads=4 \
#-XX:+UseParallelOldGC \
#-XX:+UseAdaptiveSizePolicy \
#-XX:+PrintGCDetails \
#-XX:+PrintTenuringDistribution \
#-XX:+PrintGCTimeStamps \
#-XX:+HeapDumpOnOutOfMemoryError \
#-XX:HeapDumpPath=/ \
#-Xloggc:/gc.log \
#-XX:+UseGCLogFileRotation \
#-XX:NumberOfGCLogFiles=5 \
#-XX:GCLogFileSize=10M"

#ENV JAVA_OPTS="-Dspring.profiles.active=dev"
#ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar

RUN mkdir -p /opt/settings/
#RUN echo "env=DEV" > /opt/settings/server.properties
ENTRYPOINT ["java", "-Xdebug", "-Xrunjdwp:server=y,transport=dt_socket,suspend=n", "-Dio.netty.leakDetectionLevel=advanced", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","1","2"]

