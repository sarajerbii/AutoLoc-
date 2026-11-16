# Notes — Couche Repository (Atelier 3)

## Choix d'interface

| Interface | Étend | Justification |
|---|---|---|
| IContratRepository | JpaRepository<Contrat, Long> | CRUD complet, findAll renvoie une List, saveAndFlush disponible. |
| IAgenceRepository | JpaRepository<Agence, Long> | Même justification : CRUD, listes, tri, pagination, flush. |
| IEmployeRepository | JpaRepository<Employe, Long> | Idem. |
| IVehiculeRepository | JpaRepository<Vehicule, Long> | Idem. |
| IEquipementRepository | JpaRepository<Equipement, Long> | Idem. |
| IClientRepository | JpaRepository<Client, Long> | Idem. |
| IReservationRepository | JpaRepository<Reservation, Long> | Idem. |
| IPaiementRepository | JpaRepository<Paiement, Long> | Lecture des paiements ; création et suppression via le Contrat. |
| IMaintenanceRepository | JpaRepository<Maintenance, Long> | Idem. |

## Anomalies SonarQube for IDE

| Anomalie | Correction apportée |
|---|---|
| des imports inutilisés java.util.* dans toutes les entités ; | suppression |
