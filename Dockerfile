FROM amd64/amazoncorretto:17

WORKDIR /app

COPY moonshot-api/build/libs/moonshot-api-0.0.1-SNAPSHOT.jar /app/moonshot.jar

CMD ["java", "-Duser.timezone=Asia/Seoul", "-jar", "-Dspring.profiles.active=deploy", "moonshot.jar"]