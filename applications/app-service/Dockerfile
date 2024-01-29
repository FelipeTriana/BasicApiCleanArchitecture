FROM eclipse-temurin:17-jre-alpine

# Time Zone America/Bogota
ENV TZ="America/Bogota"

ARG APP_VERSION

WORKDIR /opt/app/aplicaciones

COPY build/certs/SURAINTCA.crt build/certs/SURAISSUINGCA01.crt build/certs/SURAISSUINGCA02.crt build/certs/SuraRootCA.crt /tmp/
COPY build/libs/applications-app-service-${APP_VERSION}.jar /opt/app/aplicaciones/cleanarchitecture/cleanarchitecture.jar

RUN rm -f /etc/localtime && \
    ln -s "/usr/share/zoneinfo/${TZ}" /etc/localtime && \
    $JAVA_HOME/bin/keytool \
      -noprompt \
      -import \
      -trustcacerts \
      -keystore $JAVA_HOME/lib/security/cacerts \
      -storepass changeit \
      -alias SuraRootCA \
      -file /tmp/SuraRootCA.crt && \
    $JAVA_HOME/bin/keytool \
      -noprompt \
      -import \
      -trustcacerts \
      -keystore $JAVA_HOME/lib/security/cacerts \
      -storepass changeit \
      -alias SURAINTCA \
      -file /tmp/SURAINTCA.crt && \
    $JAVA_HOME/bin/keytool \
      -noprompt \
      -import \
      -trustcacerts \
      -keystore $JAVA_HOME/lib/security/cacerts \
      -storepass changeit \
      -alias SURAISSUINGCA01 \
      -file /tmp/SURAISSUINGCA01.crt && \
    $JAVA_HOME/bin/keytool \
      -noprompt \
      -import \
      -trustcacerts \
      -keystore $JAVA_HOME/lib/security/cacerts \
      -storepass changeit \
      -alias SURAISSUINGCA02 \
      -file /tmp/SURAISSUINGCA02.crt && \
    rm -f /tmp/SURAINTCA.crt \
          /tmp/SURAISSUINGCA01.crt \
          /tmp/SURAISSUINGCA02.crt \
          /tmp/SuraRootCA.crt

USER nobody
ENTRYPOINT ["/bin/sh", "-c", "exec /opt/app/shared/cleanarchitecture/start.sh"]
