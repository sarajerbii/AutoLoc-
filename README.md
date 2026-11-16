# AutoLoc

Plateforme de gestion de location de véhicules multi-agences — back-end Spring Boot exposant une API REST.

> Projet fil rouge du cours **Architecture des Systèmes d'Information (ASI 26-27)** — ESPRIT.

## 1. Acteurs et cas d'utilisation (Séance 1)

Première liste identifiée à partir de l'étude de cas. Elle sera affinée au fil des ateliers.

### 1.1 Acteurs

| Acteur | Description |
|---|---|
| **Client** | Particulier ou professionnel souhaitant louer un véhicule |
| **Agent d'agence** | Employé en charge de la gestion opérationnelle d'une agence |
| **Responsable d'agence (Manager)** | Supervise une agence et son personnel ; dispose des droits de l'Agent |
| **Administrateur** | Administre la plateforme |

### 1.2 Cas d'utilisation par acteur

#### Client
- S'inscrire et mettre à jour son profil
- Consulter les véhicules disponibles (par ville, catégorie et période)
- Créer une réservation
- Modifier ou annuler une réservation
- Consulter ses contrats et l'historique de ses réservations

#### Agent d'agence
- Gérer les véhicules de l'agence (ajout, modification, suppression, statut)
- Valider une réservation
- Établir un contrat
- Enregistrer un paiement (acompte, solde, pénalités)
- Planifier et suivre les maintenances d'un véhicule

#### Responsable d'agence (Manager)
- Hériter de tous les cas d'utilisation de l'Agent d'agence
- Gérer les employés de l'agence
- Consulter les statistiques de l'agence (occupation, chiffre d'affaires)

#### Administrateur
- Gérer les agences
- Gérer les catégories de véhicules
- Consulter les statistiques globales
- Configurer la plateforme

### 1.3 Cas d'utilisation transverses (système)

- Calculer le tarif d'une location (catégorie, durée, période, équipements optionnels)
- Libérer automatiquement les véhicules en fin de location
- Alerter sur les contrats arrivant à échéance

## 2. Stack technique

Java 17+, Maven, Spring Boot (Data JPA, MVC, AOP, Scheduler), MySQL/PostgreSQL, H2 (tests), Lombok, springdoc-openapi.
