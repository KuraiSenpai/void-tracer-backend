FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

RUN chmod +x gradlew

RUN ./gradlew bootJar -x test

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

RUN apt-get update && apt-get install -y curl && \
    curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs && \
    rm -rf /var/lib/apt/lists/*

COPY --from=build /app/build/libs/warframe-0.0.1-SNAPSHOT.jar app.jar

COPY scripts /app/scripts

WORKDIR /app/scripts/parser-node

RUN npm install

WORKDIR /app

ENV JAVA_TOOL_OPTIONS="-Xmx300M -Xms300M -XX:+UseSerialGC"
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]