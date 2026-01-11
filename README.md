# Projet MSA — MicroservicesApp

**Nom et Prénom : Hiba Balhirch**  
**Dépôt : https://github.com/hiba1453/Projet-Miscroservices-Balhirch-Hiba.git**

## Objectif

À partir de l’architecture imposée (Eureka + Gateway + services métier), ce projet ajoute :

- **MySQL par microservice** (Database per Service) : `db_user`, `db_book`, `db_emprunter`
- **Kafka** pour la communication asynchrone
- **notification-service** (consumer Kafka uniquement, sans endpoints REST)
- **Déploiement Docker Compose** (MySQL + Kafka/Zookeeper + microservices)

Référence de base:  
https://gitlab.com/drissRiane/microservicesapp.git

---

## Architecture & services

- `discovery-service` (Eureka Server)
- `gateway-service` (API Gateway)
- `user-service` (utilisateurs + MySQL `db_user`)
- `book-service` (livres + MySQL `db_book`)
- `emprunter-service` (emprunts + MySQL `db_emprunter` + producer Kafka)
- `notification-service` (consumer Kafka, notification simulée via logs)

---

## Persistance MySQL

Chaque service métier possède **sa propre base** :

- `user-service` → `db_user`
- `book-service` → `db_book`
- `emprunter-service` → `db_emprunter`

Les paramètres DB sont configurés via les variables d’environnement dans `docker-compose.yaml`.

---

## Kafka (asynchrone)

- **Topic** : `emprunt-created`
- **Producer** : `emprunter-service`
- **Consumer** : `notification-service`

Exemple d’évènement émis lors de la création d’un emprunt :

```json
{
  "empruntId": 1,
  "userId": 3,
  "bookId": 5,
  "eventType": "EMPRUNT_CREATED",
  "timestamp": "2025-01-01T14:00:00"
}
```

> Le `notification-service` écoute le topic et affiche une notification (log/console).

---

## Démarrage (Docker Compose)

### Prérequis
- Docker + Docker Compose (plugin)
- Connexion internet (pour télécharger les images la première fois)

### Lancer le projet

À la racine :

```bash
docker-compose up --build
```

---

## Vérifications 

### 1) Eureka
Ouvrir : `http://localhost:8761`  
Vérifier que les services sont enregistrés.

### 2) Gateway
Les routes passent par la gateway.

### 3) Kafka / notifications
Créer un emprunt via l’API de `emprunter-service` (via la gateway si configurée).  
Observer les logs du `notification-service` :

```bash
docker compose logs -f notification-service
```

