FROM openjdk:8u131-jre-alpine

RUN adduser -h "/home/omart" -u 2000 -D -s /bin/false "omart" \
    && apk --no-cache add curl unzip \
    && apk add --update ca-certificates tzdata \
    && cp /usr/share/zoneinfo/Asia/Ho_Chi_Minh /etc/localtime \
    && echo echo "Asia/Ho_Chi_Minh" > /etc/timezone \
    && apk del tzdata \
    && rm -rf /var/cache/apk/* \
    && mkdir /etc-extra \
    && touch /etc-extra/hosts

RUN echo $'#!/bin/sh \n\
rm -rf /home/omart/app/* \n\
unzip -q /home/omart/app.jar -d /home/omart/app \n\
trap \'echo Stopping process $PID; kill -TERM $PID\' TERM INT \n\
cat /etc-extra/hosts >> /etc/hosts \n\
su -s /bin/sh omart -c \'exec /usr/bin/java $JAVA_OPTS -cp /home/omart/app org.springframework.boot.loader.JarLauncher $APP_OPTS\' & \n\
PID=$! \n\
echo $PID \n\
wait $PID \n\
trap - TERM INT \n\
wait $PID \n\
EXIT_STATUS=$?' > /init \
    && chmod +x /init

ENTRYPOINT ["/init"]

EXPOSE 8080

WORKDIR /home/omart

ENV JAVA_OPTS "-Dspring.profiles.active=prod -Xms512m -Xmx512m -XX:MaxMetaspaceSize=200m -XX:+UseG1GC -XX:+DisableExplicitGC -XX:+UseCompressedOops -verbose:gc -Xloggc:/tmp/gc.log -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC -XX:+PrintGCDetails -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M"

RUN mkdir -p /home/omart/app \
  && curl --header "PRIVATE-TOKEN: 6_rcrSTexrgJ3L_sZvYS" -s -L -o /home/omart/app.jar https://git.omartvietnam.com/service-backend/poi-service/-/jobs/artifacts/master/raw/target/app.jar?job=compile