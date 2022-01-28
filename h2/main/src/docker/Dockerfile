FROM registry.access.redhat.com/ubi8/openjdk-11
COPY content/start-h2.sh /home/jboss/bin/
RUN \
    curl -SLvo /home/jboss/h2.jar "https://search.maven.org/remotecontent?filepath=com/h2database/h2/2.1.210/h2-2.1.210.jar" && \
    mkdir -p /home/jboss/h2-data && chmod g+w /home/jboss/h2-data
EXPOSE 9092
CMD [ "/bin/bash", "/home/jboss/bin/start-h2.sh" ]
