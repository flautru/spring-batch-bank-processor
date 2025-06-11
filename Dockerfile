# Utilise une image Maven pour la compilation
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copie le code source
COPY . .

# Compile le projet sans les tests
RUN ./mvnw clean package -DskipTests

# Étape finale avec l'image JDK
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copie le jar depuis l'étape builder
COPY --from=builder /app/target/*.jar app.jar

# Commande de lancement
ENTRYPOINT ["java", "-jar", "app.jar"]