#Docker File

#jdk 이미지 실행
FROM openjdk:17-alpine

#이미지 빌드
ARG JAR_FILE=/build/libs/pongguel-0.0.1-SNAPSHOT.jar

#jar 복사
COPY ${JAR_FILE} /pongguel.jar

#컨테이너 시작 실행 명령
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=secret", "/pongguel.jar"]