# README - Projet Kronologic

## Dépendances du projet

#### Bibliothèques Utilisées

- JavaFX : Pour la gestion de l’interface graphique.

- PlantUML : Utilisé pour la génération des diagrammes UML.

- Choco-Solver : Cette bibliothèque servira à développer les IA dans la suite du projet. Durant cette première itération, elle n'a été utilisée uniquement adin qu'on la comprenne et qu'on la prenne en main grâce à la réalisation des prototypes.

## Itération 1

### 1. Répartition des Tâches

#### Mathieu :
- Suivre un tutoriel complet sur Choco-Solver afin de se familiariser avec l’outil.
- Réaliser des prototypes pour résoudre des problèmes simples. Cela a pour objectif de saisir le fonctionnement de la programmation par contraintes.
- Comprendre comment intégrer Choco-Solver au projet.
          
#### Enzo et Corentin :
- Encoder les données du jeu et créer des classes Java permettant de les modéliser (lieux, personnages, indices, temps).
- Développer une architecture MVC afin de lier les données de jeu avec l’interface graphique de l’application.
- Commencer le développement de l’interface graphique (page d’accueil et redirection vers la feuille de jeu).

### 2. Fonctionnalités Développées

- Créer une page d'accueil
- Création de l'interface de jeu (éléments visuels de la feuille de jeu et de la pose de question)
- Permettre au joueur de poser une question (sans le placement automatique des pions)

### 3. Mode d’Emploi

Lancez la classe MainMVC afin d'avoir accès aux fonctionnalités déjà disponibles.

#### Fonctionnalités Disponibles :

- Accueil : Lancer une partie ou quitter le jeu.
- Poser une Question : Sélectionner un lieu, un temps et un personnage.
- Affichage des Indices : Visualiser l’historique des indices découverts.

## Itération 2

### 1. Répartition des Tâches

#### Mathieu :
- Réaliser l'IA de déduction utilisant Choco-Solver.
- Analyser les indices collectés et en tirer des conclusions logiques.
- Proposer une hypothèse logique basée sur les indices.
          
#### Enzo et Corentin :
- Permettre au joueur de formuler des déductions et de voir s'il a gagné la partie ou non.
- Permettre au joueur de formuler des hypothèses. (la gestion des notes du joueur n'est pas terminée)
- Permettre au joueur de changer d’affichage entre la VueCarte et la vue VueTableau.

### 2. Fonctionnalités Développées

- Permettre au joueur de formuler des déductions
- Permettre au joueur de formuler des hypothèses (sans la gestion des pions d'absence et d'hypothèse)
- Permettre au joueur de changer d’affichage
- Réalisation de l'IA de déduction utilisant Choco-Solver :
   - Analyser les indices collectés et en tirer des conclusions logiques
   - Proposer une hypothèse logique basée sur les indices

### 3. Mode d’Emploi

Lancez la classe MainMVC afin d'avoir accès aux fonctionnalités déjà disponibles.

#### Fonctionnalités Disponibles (en plus des fonctionnalités implémentées dans l'itération précédente) :

- Faire une Déduction : Afin de mettre un terme à la partie, sélectionner le coupable, le lieu du meurtre et le temps du meurtre et vérifier si c'est juste ou non.
- Changer d'affichage : Avoir une visualisation différente des notes du joueur sous forme de tableaux.
- Formuler des hypothèses : Déplacer les pions représentant les personnages ou le pion de nombre représentant un nombre de personnages sur les cartes en fonction des indices découverts.
- IA de déduction : Avoir accès aux déductions faites (en se basant sur les indices collectés) par l'IA utilisant Choco-Solver grâce au bouton "Déduction de l'IA".
