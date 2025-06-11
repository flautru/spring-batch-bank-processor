# 💳 Spring Batch Bank Processor

Projet Java Spring Batch permettant de lire un fichier CSV contenant des transactions bancaires, de les traiter et de
les enregistrer dans une base de données via JPA.

## 🧱 Technologies utilisées

- Java 21
- Spring Boot
- Spring Batch
- Spring Data JPA
- PostgreSQL
- Maven
- JUnit 5 + Mockito
- Lombok

## 📁 Fonctionnement

### 📥 Lecture (Reader)

Lecture d’un fichier CSV contenant des transactions bancaires grâce à `FlatFileItemReader`.

### 🔄 Traitement (Processor)

- Filtre les transactions à montant invalide (null ou ≤ 0)
- Catégorise automatiquement les transactions si la catégorie est vide :
    - Montant > 100 → "Loisirs"
    - Sinon → "Courses"

### 💾 Écriture (Writer)

Écriture des transactions valides en base de données avec `JpaItemWriter`.

## 🧪 Tests

- **Tests unitaires** : processor, converter, reader, writer
- **Tests d’intégration** : du batch complet avec un vrai fichier CSV et une base H2

Lancement des tests :

```bash
mvn test
```

Lancement de l'application :

```bash
mvn spring-boot:run
```
