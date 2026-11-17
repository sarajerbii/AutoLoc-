# Notes — Couche Service (Atelier 4)

## A. Modes d'injection comparés

| Critère | Constructeur | Attribut |
|---|---|---|
| Champ final possible | Oui | Non |
| Dépendance visible dans la signature | Oui | Non (cachée) |
| Utilisable avec new hors conteneur sans dépendance null | Oui | Non |
| Signalé par SonarQube for IDE | Non | Oui (S6813) |

## B. Services

| Service | Dépendances injectées | Mode | Justification |
|---|---|---|---|
| AgenceServiceImpl | IAgenceRepository | Constructeur | Champ final, dépendance explicite, typée par interface |
| EmployeServiceImpl | IEmployeRepository | Constructeur | Idem |
| VehiculeServiceImpl | IVehiculeRepository | Constructeur | Idem |
| EquipementServiceImpl | IEquipementRepository | Constructeur | Idem |
| ClientServiceImpl | IClientRepository | Constructeur | Idem |
| ReservationServiceImpl | IReservationRepository | Constructeur | Idem |
| ContratServiceImpl | IContratRepository | Constructeur | Idem |
| PaiementServiceImpl | IPaiementRepository | Constructeur | Idem, lecture seule (le Paiement dépend du Contrat) |
| MaintenanceServiceImpl | IMaintenanceRepository | Constructeur | Idem |

## C. Messages d'erreur

- **Message 1** : NoSuchBeanDefinitionException. @Service oublié sur ContratServiceImpl, ou classe hors du package tn.esprit.autoloc.
- **Message 2** : NoUniqueBeanDefinitionException. Correction 1 : @Primary sur la classe EmailNotificateur. Correction 2 : @Qualifier("emailNotificateur") sur le paramètre du constructeur de NotificationController.
- **Message 3** : dépendance circulaire. Avec le constructeur, chaque bean exige l'autre pour exister : aucun ne peut être créé en premier. Piste : extraire la logique commune dans un troisième service dont les deux dépendent.

## D. Anomalies SonarQube for IDE

aucune anomalie

## E. Questions de compréhension

1. Le new est exécuté par le conteneur Spring au démarrage : après le scan, il instancie ContratServiceImpl avec le proxy de IContratRepository.
2. Un contrôleur dépend de l'interface pour être faiblement couplé : on peut changer d'implémentation ou fournir un faux service en test sans toucher au contrôleur.
3. Un singleton est partagé par tous les threads. Exemple de bug : un attribut « contratCourant » écrasé par une requête pendant qu'une autre l'utilise, ce qui mélange les données de deux clients.
4. Passer directement l'objet reçu à save écraserait toutes les colonnes (les champs absents deviendraient null) et pourrait vider la collection de paiements si orphanRemoval est activé.
5. @Component se pose sur une classe détectée par le scan ; @Bean se pose sur une méthode d'une classe @Configuration, pour une classe qu'on ne peut pas annoter. @Primary se pose sur la classe (candidat par défaut) ; @Qualifier se pose à l'injection (choix explicite). IPaiementService est en lecture seule car le Paiement est l'enfant d'une composition : on le crée et on le retire via le Contrat.