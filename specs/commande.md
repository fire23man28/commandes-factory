# Spec : gestion des commandes

## Objectif

Permettre à un utilisateur de créer une commande client, puis de la faire avancer dans son cycle de vie (expédition ou annulation).

## Données

Une **commande** possède :
- `id` : identifiant unique, généré automatiquement,
- `client` : nom du client, obligatoire,
- `montant` : montant en euros, supérieur à 0,01, deux décimales,
- `statut` : `CREEE`, `EXPEDIEE` ou `ANNULEE`.

## Cycle de vie

```
CREEE ──expédier──▶ EXPEDIEE
  │
  └──annuler──▶ ANNULEE
```

## Règles métier

1. Une commande est toujours créée avec le statut `CREEE`.
2. Une commande `ANNULEE` ne peut pas être expédiée. Le système refuse et renvoie une erreur `409 Conflict`.
3. Une commande `EXPEDIEE` ne peut pas être annulée. Le système refuse et renvoie une erreur `409 Conflict`.
4. Une commande inconnue renvoie une erreur `404 Not Found`.

## Critères d'acceptation

- **CA1** : POST `/api/commandes` avec un client et un montant valide renvoie `201 Created` et le statut `CREEE`.
- **CA2** : POST `/api/commandes` avec un client vide renvoie `400 Bad Request`.
- **CA3** : POST `/api/commandes/{id}/expedition` sur une commande `CREEE` renvoie `200 OK` avec le statut `EXPEDIEE`.
- **CA4** : POST `/api/commandes/{id}/expedition` sur une commande `ANNULEE` renvoie `409 Conflict`.
- **CA5** : GET `/api/commandes/{id}` sur un id inconnu renvoie `404 Not Found`.
- **CA6** : GET `/api/commandes` renvoie `200 OK` avec la liste de toutes les commandes, liste vide incluse.

## Hors périmètre

- Authentification et droits d'accès.
- Lignes de commande et calcul de TVA.
- Paiement.
