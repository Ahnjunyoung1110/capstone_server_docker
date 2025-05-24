# --- 빌드 단계 ---
FROM gradle:8.0.2-jdk17 AS build
WORKDIR /app

COPY . .

RUN chmod +x gradlew
RUN ./gradlew clean build -x test

# --- 실행 단계 ---
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
