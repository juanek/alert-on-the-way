# Usar la imagen base de OpenJDK
FROM openjdk:17-jdk-slim

# Establecer el directorio de trabajo en /app
WORKDIR /app

# Copiar el archivo JAR generado
COPY target/alert-on-the-way-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto 8080
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
