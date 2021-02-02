FROM openliberty/open-liberty:kernel-java8-openj9-ubi

ARG VERSION=1.0
ARG REVISION=SNAPSHOT

LABEL \
  org.opencontainers.image.authors="Martand Mishra" \
  org.opencontainers.image.vendor="IBM" \
  org.opencontainers.image.url="local" \
  org.opencontainers.image.source="https://github.com/mart3051/guide-getting-started.git" \
  org.opencontainers.image.version="$VERSION" \
  org.opencontainers.image.revision="$REVISION" \
  io.openshift.expose-services="9080:tcp" \
  vendor="Open Liberty" \
  name="db-mq-poc" \
  version="$VERSION-$REVISION" \
  summary="MQ- DB2 POC" \
  description="DB2 MQ POC."

COPY --chown=1001:0 src/main/liberty/config/ /config/
COPY --chown=1001:0 target/*.war /config/apps/
RUN mkdir /config/jdbc
COPY --chown=1001:0 target/liberty/wlp/usr/servers/defaultServer/jdbc/jcc-11.5.5.0.jar /config/jdbc/
COPY --chown=1001:0 target/liberty/wlp/usr/servers/defaultServer/wmq.jmsra-9.2.1.0.rar /config/
EXPOSE 9080

RUN configure.sh
