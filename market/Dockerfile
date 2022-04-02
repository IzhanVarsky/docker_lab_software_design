FROM adoptopenjdk/maven-openjdk11
ENV CATALINA_OPTS=-Djava.security.egd=file:/dev/./urandom SPRING_PROFILES_ACTIVE=test
#COPY market/target/market-1.0-SNAPSHOT.jar /usr/local/lib/demo.jar
COPY target/market-1.0-SNAPSHOT.jar /usr/local/lib/demo.jar
EXPOSE 8080/tcp
ENTRYPOINT ["java","-jar","/usr/local/lib/demo.jar", "-Dspring.profiles.active=test"]