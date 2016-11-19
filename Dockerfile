FROM relateiq/oracle-java8

LABEL version = "1.0"

EXPOSE 8080

RUN mkdir /runtime

ADD target/demo-0.0.1-SNAPSHOT.jar /runtime/demo.jar

CMD java -jar /runtime/demo.jar -Dspring.profiles.active=${PROFILES}