mvn com.redhat.quarkus.platform:quarkus-maven-plugin:2.2.3.Final-redhat-00013:create \
    -DprojectGroupId=com.redhat \
    -DprojectArtifactId=quarkus-backend \
    -DplatformGroupId=com.redhat.quarkus \
    -DplatformVersion=2.2.3.Final-redhat-00013 \
    -DclassName="com.redhat.quarkus.backend.PeopleResourceJaxRs" \
    -Dpath="/people" \
    -Dextensions="agroal,jdbc-h2,hibernate-orm-rest-data-panache,resteasy-reactive-jackson"
