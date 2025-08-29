FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

RUN addgroup -S appuser && adduser -S appuser -G appuser

RUN apt-get update && apt-get install -y --no-install-recommends \
    curl \
    && rm -rf /var/lib/apt/lists/*

COPY target/*.jar app.jar

RUN chown -R appuser:appuser /app

USER appuser

EXPOSE 8080

ENV JAVA_OPTS="-Xms1g -Xmx2g -XX:+UseStringDeduplication -XX:+UseZGC -XX:+ZGenerational"

ENV JAVA_OPTS="$JAVA_OPTS \
    -XX:+ExitOnOutOfMemoryError \
    -XX:+HeapDumpOnOutOfMemoryError \
    -XX:HeapDumpPath=/app/heapdump \
    -Djava.security.egd=file:/dev/./urandom \
    -Dspring.profiles.active=prod"

HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
    CMD curl -f http://localhost:${APP_PORT:-8080}/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]