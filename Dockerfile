FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -B clean package

FROM eclipse-temurin:17-jre

WORKDIR /app

RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        libxext6 \
        libxrender1 \
        libxtst6 \
        libxi6 \
        libx11-6 \
        libfreetype6 \
        fontconfig \
    && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/maven1-1.0-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]
