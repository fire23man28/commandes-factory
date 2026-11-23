# AGENTS.md — règles pour les agents IA

Ce fichier est lu par l'agent avant chaque ticket. Toute modification doit le respecter.

## Architecture

- Couches strictes : `controller` → `service` → `repository`. Une couche n'appelle que la couche directement en dessous.
- Un `controller` ne contient aucune logique métier et ne manipule jamais une entité `model` directement : il échange des `DTO`.
- Le `service` porte la logique métier et les transactions (`@Transactional`).
- Le `repository` n'étend que `JpaRepository`. Pas de logique dedans.

## Données

- Toute modification de schéma passe par une migration Flyway dans `src/main/resources/db/migration/`, nommée `V<n>__description.sql`.
- Ne jamais modifier une migration déjà publiée. On en ajoute une nouvelle.
- `spring.jpa.hibernate.ddl-auto` reste à `validate`.

## Qualité

- Chaque nouvelle règle métier a au moins un test unitaire JUnit 5 (+ Mockito pour les dépendances).
- Chaque nouvel endpoint a au moins un test d'intégration avec `@SpringBootTest` et Testcontainers.
- Éviter le N+1 : pas de requête dans une boucle, utiliser `JOIN FETCH` ou un `@EntityGraph` si besoin.
- Pas de `catch (Exception e) {}`. Les erreurs sont soit gérées, soit remontées.
- Pas de commentaire qui répète le code. Un commentaire explique le « pourquoi ».

## Sécurité

- Aucun secret en dur (mot de passe, clé, token). Utiliser des variables d'environnement.
- Ne jamais toucher à la configuration de sécurité ni aux migrations destructives sans validation humaine : marquer le ticket `Blocked / Human`.

## Périmètre d'un ticket

- Un ticket = une fonctionnalité ou une règle. Si la tâche touche plus de trois fichiers de couches différentes, la découper.
- Ne pas refactorer du code hors du ticket.
