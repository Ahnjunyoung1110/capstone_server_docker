# --- 빌드 단계 ---
FROM gradle:8.0.2-jdk17 AS build

WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test

# --- 실행 단계 ---
FROM openjdk:17-jdk-alpine

WORKDIR /app

# 빌드 스테이지에서 생성된 jar를 이 단계로 복사
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
