FROM openjdk:15
EXPOSE 8090
ADD ./parentModule/ui/target/app.jar app.jar
ENTRYPOINT ["java", "-jar", "--enable-preview", "app.jar"]
