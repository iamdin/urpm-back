FROM java:8
VOLUME /tmp
ADD target/urpm-0.0.1-SNAPSHOT.jar urpmback.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/urpmback.jar"]