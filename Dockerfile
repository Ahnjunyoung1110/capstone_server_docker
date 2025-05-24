# --- 빌드 단계 ---
FROM gradle:8.0.2-jdk17 AS build

WORKDIR /app

# gradle 디렉토리 복사
COPY gradle /app/gradle

# 캐시를 위한 gradlew + build 설정 복사
COPY gradlew .
COPY build.gradle settings.gradle ./
RUN chmod +x gradlew
RUN ./gradlew dependencies || return 0

# 전체 프로젝트 복사 후 빌드
COPY . .
RUN chmod +x gradlew  # gradlew 다시 덮어쓰였을 경우 대비
RUN ./gradlew clean build -x test
