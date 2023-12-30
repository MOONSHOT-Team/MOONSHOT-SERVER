FROM amd64/amazoncorretto:17

WORKDIR /app

COPY ./build/libs/server-0.0.1-SNAPSHOT.jar /app/moonshot.jar

CMD ["java", "-Duser.timezone=Asia/Seoul", "-jar", "-Dspring.profiles.active=deploy", "moonshot.jar"]