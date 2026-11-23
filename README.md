# commandes-factory

Mini projet Spring Boot qui illustre le workflow **AI software factory** : une spec claire, des règles pour l'IA, un découpage en tickets, du code, des tests, puis une CI qui valide chaque changement.

Le domaine est volontairement simple : gérer des **commandes** (créer, expédier, annuler).

## Workflow suivi

| Étape | Où dans le repo |
|---|---|
| 1. Spec (ce qu'on construit) | `specs/commande.md` |
| 2. Règles pour l'IA (conventions) | `AGENTS.md` |
| 3. Code en couches | `src/main/java/com/example/commandes/` |
| 4. Migration de base versionnée | `src/main/resources/db/migration/` |
| 5. Tests unitaires et d'intégration | `src/test/java/...` |
| 6. Validation automatique | `.github/workflows/ci.yml` |

## Stack

- Java 17, Spring Boot 3.3
- Spring Web, Spring Data JPA, Bean Validation
- PostgreSQL, Flyway
- JUnit 5, Mockito, Testcontainers
- Maven, GitHub Actions

## Lancer les tests

Prérequis : JDK 17, Maven, Docker (pour Testcontainers).

```bash
mvn verify
```

## Lancer l'application

Une base PostgreSQL doit tourner. Exemple avec Docker :

```bash
docker run --name commandes-db -e POSTGRES_DB=commandes -e POSTGRES_USER=commandes \
  -e POSTGRES_PASSWORD=change-me -p 5432:5432 -d postgres:16-alpine

export DB_PASSWORD=change-me
mvn spring-boot:run
```

## Endpoints

| Méthode | Route | Effet |
|---|---|---|
| POST | `/api/commandes` | Crée une commande (statut `CREEE`) |
| GET | `/api/commandes/{id}` | Lit une commande |
| POST | `/api/commandes/{id}/expedition` | Passe en `EXPEDIEE` |
| POST | `/api/commandes/{id}/annulation` | Passe en `ANNULEE` |

Exemple :

```bash
curl -X POST localhost:8080/api/commandes \
  -H "Content-Type: application/json" \
  -d '{"client":"Alice","montant":120.50}'
```

## Ce qui n'est pas encore couvert

Volontairement hors périmètre pour rester petit : Spring Security / JWT, Checkstyle, Sonar, hooks anti-secrets. Ce sont les prochaines étapes naturelles du projet, et chacune peut devenir un ticket dans la boucle décrite plus haut.
