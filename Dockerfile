FROM repo.sadad.co.ir/repository/baam-docker-registry/eclipse-temurin:21-jre-alpine
VOLUME /tmp

ENV TZ=Asia/Tehran

RUN  mkdir -p /var/log/investment-api
RUN  chmod -R 777 /var/log/investment-api

RUN  mkdir -p /opt/security/investment-api
RUN  chmod -R 777 /opt/security/investment-api

COPY src/main/resources/private.pem /opt/security/investment-api/
COPY src/main/resources/public.pem /opt/security/investment-api/

COPY target/*.jar investment-api-1.0.8-SNAPSHOT.jar
ENTRYPOINT ["java","-Xdebug","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=1523","-Dcom.ibm.icu.util.TimeZone.DefaultTimeZoneType=JDK","-jar","/investment-api-1.0.8-SNAPSHOT.jar"]