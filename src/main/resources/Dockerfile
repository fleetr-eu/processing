FROM java:8-jre-alpine
VOLUME /tmp
ADD target/@project.artifactId@-@project.version@.jar /@project.artifactId@-@project.version@.jar
RUN sh -c 'touch /@project.artifactId@-@project.version@.jar'
ENTRYPOINT ["java","-jar","/@project.artifactId@-@project.version@.jar"]
