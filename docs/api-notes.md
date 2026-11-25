# Notes — API REST (Atelier 5)

## A. Endpoints

Tous les contrôleurs sont sous le préfixe /api (context-path).

| Contrôleur | Verbe et route | Service appelé | Succès | Erreurs |
|---|---|---|---|---|
| ContratController | GET /api/contrats | findAll() | 200 | — |
| ContratController | GET /api/contrats/{id} | findById(id) | 200 | 404 |
| ContratController | POST /api/contrats | create(contrat) | 201 | 400 |
| ContratController | PUT /api/contrats/{id} | update(id, contrat) | 200 | 404, 400 |
| ContratController | DELETE /api/contrats/{id} | deleteById(id) | 204 | 404 |
| PaiementController | GET /api/paiements | findAll() | 200 | — |
| PaiementController | GET /api/paiements/{id} | findById(id) | 200 | 404 |
| PaiementController | POST /api/paiements | (aucun) | — | 405 |
| AgenceController | GET /api/agences | findAll() | 200 | — |
| AgenceController | GET /api/agences/{id} | findById(id) | 200 | 404 |
| AgenceController | POST /api/agences | create(agence) | 201 | 400 |
| AgenceController | PUT /api/agences/{id} | update(id, agence) | 200 | 404, 400 |
| AgenceController | DELETE /api/agences/{id} | deleteById(id) | 204 | 404 |

(À dupliquer à l'identique, avec les 5 lignes, pour EmployeController /api/employes, VehiculeController /api/vehicules, EquipementController /api/equipements, ClientController /api/clients, ReservationController /api/reservations et MaintenanceController /api/maintenances.)

## B. Annotations Jackson

| Association | Annotation | Justification |
|---|---|---|
| Contrat.paiements / Paiement.contrat | @JsonManagedReference("contrat-paiements") côté liste, @JsonBackReference("contrat-paiements") côté parent | Composition : les paiements se créent dans le JSON du contrat. Le côté parent est masqué pour couper la boucle. |
| Agence.employes, Agence.vehicules | @JsonIgnore | L'enfant est créé seul et désigne son agence par identifiant. La liste inverse est exclue du JSON. |
| Vehicule.reservations, Vehicule.maintenances | @JsonIgnore | Même raisonnement. |
| Client.reservations | @JsonIgnore | Même raisonnement. |
| Equipement.vehicules | @JsonIgnore | Côté inverse du ManyToMany. |
| Reservation.contrat | @JsonIgnore | Côté inverse du OneToOne. |

Différence : @JsonBackReference masque l'attribut à la sérialisation mais le reconstitue à la désérialisation (le paiement retrouve son contrat). @JsonIgnore exclut complètement le champ, dans les deux sens.

## C. Postman et Swagger

- Postman sert à tester : requêtes rejouables, scripts de vérification. Exemple : la requête "Consulter un contrat inexistant" vérifie un 404 avec pm.response.to.have.status(404).
- Swagger UI sert à documenter et explorer : le contrat de l'API est généré depuis le code. Exemple : la section Schemas décrit Contrat à partir de l'entité, et les @ApiResponse affichent les codes 200 et 404.
- Ils sont complémentaires.

## D. Anomalies SonarQube for IDE

| Anomalie | Règle / explication | Correction apportée |
|---|---|---|
| (à compléter) | (à compléter) | (à compléter) |
| (à compléter) | (à compléter) | (à compléter) |

## E. Questions

1. Le DispatcherServlet reçoit toutes les requêtes. Il interroge le HandlerMapping, qui retrouve la méthode du contrôleur à partir du verbe HTTP et de l'URL.
2. Le contrôleur dépend de IContratService : il reste mince (pas de logique métier), n'accède jamais au repository, et on peut changer l'implémentation du service sans le toucher. Dépendre de l'interface assure un couplage faible.
3. Un POST crée une ressource (201 Created). Un DELETE réussi n'a rien à renvoyer (204 No Content). Une ressource absente est une erreur du client, donc 404 : ResourceNotFoundException est traduite par le GlobalExceptionHandler. Un 500 signalerait un bug serveur.
4. Le Paiement est l'enfant d'une composition : il se crée et se supprime via son Contrat. PaiementController n'expose donc que des GET. Un POST /api/paiements renvoie 405 Method Not Allowed.
5. Exposer les entités JPA couple l'API à la base, révèle des champs internes, provoque des boucles de sérialisation et des problèmes de proxys LAZY. Contrat.paiements utilise @JsonManagedReference car les paiements sont créés dans le JSON du contrat (composition). Agence.vehicules utilise @JsonIgnore car un véhicule est créé seul, avec son agence désignée par identifiant.