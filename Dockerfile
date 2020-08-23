FROM maven:3.6.1-jdk-11

RUN mkdir -p /opt/app

ENV PROJECT_HOME /opt/app

COPY target/star-wars-planets-0.0.1.jar $PROJECT_HOME/star-wars-planets.jar

WORKDIR $PROJECT_HOME

CMD ["java", "-jar", "-Dspring.profiles.active=docker" ,"./star-wars-planets.jar"]
