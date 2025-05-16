# --- 빌드 단계 ---
FROM gradle:8.0.2-jdk17 AS build

WORKDIR /app



# Gradle 관련 캐시 유지
COPY gradlew .
COPY gradle /app/gradle
RUN ./gradlew --version || return 0  # 캐시 준비용


# 의존성 먼저 복사 → 캐시 분리
COPY build.gradle settings.gradle ./
RUN ./gradlew dependencies || return 0

# 전체 코드 복사 후 빌드
COPY . .
RUN ./gradlew clean build -x test

# --- 실행 단계 ---
FROM openjdk:17-jdk-alpine

WORKDIR /app

# 빌드 스테이지에서 생성된 jar를 이 단계로 복사
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
