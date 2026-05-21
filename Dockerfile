FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/StoreManagementTool.jar StoreManagementTool.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "StoreManagementTool.jar"]
