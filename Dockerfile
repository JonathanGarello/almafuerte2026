FROM eclipse-temurin:21-jdk

WORKDIR /app

# Instalar Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Copiar proyecto
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

# Compilar WAR
RUN mvn clean package -DskipTests

EXPOSE 8080

# Ejecutar WAR
CMD ["java", "-jar", "target/almafuerte2026-0.0.1-SNAPSHOT.war"]

