# Notes — Patrons de conception et SOLID (Atelier 6)

## Partie 1 : Analyse de AutoLocManager (v0)

### Question 1 : combien de responsabilités ?
La classe AutoLocManager en porte six :
- calculer le prix de la location (selon le type de réservation) ;
- créer le contrat et afficher son résumé ;
- envoyer l'e-mail au client ;
- envoyer le SMS (méthode présente) ;
- enregistrer le paiement ;
- sauvegarder le contrat.

### Question 2 : quel principe SOLID n'est pas respecté ?
- SRP (Single Responsibility) : une seule classe a plusieurs raisons de changer (tarifs, notifications, paiement, persistance).
- OCP (Open/Closed) : ajouter un type de réservation oblige à modifier la classe existante.
- DIP (Dependency Inversion) : la classe dépend directement de ses détails d'implémentation (envoi d'e-mail, sauvegarde) au lieu d'abstractions injectées.

### Question 3 : que se passe-t-il pour un nouveau type "PleineSaison" ?
Il faut ouvrir louerVehicule et ajouter un else if supplémentaire. Chaque nouveau type modifie du code déjà testé : risque de régression, méthode de plus en plus longue, tests à refaire.
Principe violé : OCP (une classe doit être ouverte à l'extension mais fermée à la modification). Avec le patron Strategy, on ajoute une nouvelle classe sans toucher à l'existant.