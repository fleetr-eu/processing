FROM java:8-jre-alpine
VOLUME /tmp
ADD target/stream-processing-0.1.0.jar /stream-processing-0.1.0.jar
RUN sh -c 'touch /stream-processing-0.1.0.jar'
ENTRYPOINT ["java","-jar","/stream-processing-0.1.0.jar"]
