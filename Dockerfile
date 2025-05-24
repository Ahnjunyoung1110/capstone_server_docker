# --- 빌드 단계 ---
FROM gradle:8.0.2-jdk17 AS build

WORKDIR /app

# gradlew 파일 복사 + 실행 권한 부여
COPY gradlew .
RUN chmod +x gradlew

# gradle 디렉토리 복사
COPY gradle /app/gradle

# 캐시를 위한 기본 명령 실행
RUN ./gradlew --version || return 0

# 의존성 파일 복사
COPY build.gradle settings.gradle ./
RUN ./gradlew dependencies || return 0

# 전체 프로젝트 복사 후 빌드
COPY . .
RUN ./gradlew clean build -x test


# --- 실행 단계 ---
FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
