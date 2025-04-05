# README - Projet Kronologic

# Dietrich Corentin - Dziezuk Mathieu - Mougin Enzo

## Basé sur le jeu de société Kronologic
### Créé par Fabien Gridel et Yoann Levet / Edité par SuperMeeple

## Bibliothèques Utilisées

- JavaFX : Pour la gestion de l’interface graphique.

- PlantUML : Utilisé pour la génération des diagrammes UML.

- Choco-Solver : Cette bibliothèque servira à développer les IA dans la suite du projet. Durant cette première itération, elle n'a été utilisée uniquement adin qu'on la comprenne et qu'on la prenne en main grâce à la réalisation des prototypes.

## Lien vers la vidéo d'explication de l'application : [Vidéo de demonstration](documents/9-Iteration7/demo_kronologic.mp4)


# Déroulement du projet

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

## Itération 3

### 1. Répartition des Tâches

#### Mathieu :
- Terminer la réalisation de l'IA de déduction utilisant Choco-Solver.
- Commencer le développement de l'IA de déduction heuristique.
- Analyser les indices collectés et en tirer des conclusions logiques.
- Proposer une hypothèse logique basée sur les indices.
          
#### Enzo :
- Permettre au joueur de visualiser le film des déplacements des personnages.
- Réaliser un jeu de tests permettant de vérifier le bon fonctionnement des fonctionnalités développées.

#### Corentin :
- Permettre au joueur de visualiser les règles.
- Gestion des pions et des notes du joueur.

#### Enzo et Corentin :
- Gestion du placement automatique des pions et des sous-zones.

### 2. Fonctionnalités Développées

 - Afficher les règles du jeu
 - Afficher le film des déplacements des personnages au cours de la partie (pas encore fonctionnel)
 - Afficher le film des hypothèses du joueur (pas encore fonctionnel)
 - Gestion des pions et des notes du joueur
 - Synchronisation des vues Carte et Tableaux par rapport aux hypothèses du joueur
 - Terminer l'IA de déduction utilisant Choco-Solver
 - Implémenter l'IA de déduction heuristique (pas encore fonctionnel)
 - Réalisation d'un jeu de tests permettant de vérifier le bon fonctionnement des fonctionnalités développées

### 3. Mode d’Emploi

Lancez la classe MainMVC afin d'avoir accès aux fonctionnalités déjà disponibles.

#### Fonctionnalités Disponibles (en plus des fonctionnalités implémentées dans l'itération précédente) :

- Visualiser les règles : Permettre au joueur, via le bouton situé en haut à droite, d'avoir accès aux règles du jeu afin de se familiariser avec Kronologic
- Formuler des hypothèses : Tous les bugs liés au déplacement des pions ont été corrigés. De plus, la gestion des notes a été ajoutée.
- IA de déduction : Avoir accès aux déductions faites (en se basant sur les indices collectés) par l'IA utilisant Choco-Solver grâce au bouton "Déduction de l'IA".
- Films de la partie et du joueur (en cours de développement) : Le joueur peut voir les déplacements des personnages au cours de la partie (réalité) ainsi que l'évolution des hypothèse du joueur au fil des tours de jeu.

## Itération 4

### 1. Répartition des Tâches

#### Mathieu :
- Finalisation de l'IA de déduction utilisant Choco-Solver.
- Commencement de l'implémentation de l'IA d’assistance au joueur en utilisant Choco-Solver.
- Corriger les déductions du joueur grâce à l'IA d'assistance.
          
#### Enzo :
- Correction des bugs liés aux différentes affichages des vues Carte et Tableaux.
- Réalisation du film de la partie.
- Réalisation du film des hypothèses du joueur.

#### Corentin :
- Gestion des pions d'hypothèses et d'absences (données et affichage).
- Continuation du développement de l'IA de déduction heuristique.
- Réduction de domaines à chaque nouvel indice collecté.

### 2. Fonctionnalités Développées

 - Correction de quelques bugs liés aux différentes affichages des vues Carte et Tableaux
 - Afficher le film des déplacements des personnages au cours de la partie
 - Afficher le film des hypothèses du joueur
 - Permettre au joueur de formuler des "absences" et des "hypothèses"
 - Finalisation de l'IA de déduction utilisant Choco-Solver
 - Continuation du développement de l'IA de déduction heuristique
 - Commencement de l'implémentation de l'IA d’assistance au joueur en utilisant Choco-Solver

### 3. Mode d’Emploi

Lancez la classe MainMVC afin d'avoir accès aux fonctionnalités déjà disponibles.

#### Fonctionnalités Disponibles (en plus des fonctionnalités implémentées dans l'itération précédente) :

- Absences et hypothèses : Le joueur peut en formuler grâce à des pions spécifiques sur la VueCarte.
- IA de déduction heuristique : Avoir accès aux déductions faites (en se basant sur les indices collectés) par l'IA grâce au bouton "Déduction de l'IA".
- Films de la partie et du joueur : Le joueur peut voir les déplacements des personnages au cours de la partie (réalité) ainsi que l'évolution des hypothèse du joueur au fil des tours de jeu.

## Itération 5

### 1. Répartition des Tâches

#### Mathieu :
- Finalisation de l'IA de déduction utilisant Choco-Solver.
- Commencement de l'implémentation de l'IA d’assistance au joueur en utilisant Choco-Solver.
- Corriger les déductions du joueur grâce à l'IA d'assistance.
          
#### Enzo :
- Réalisation du film de la partie.
- Réalisation du film des hypothèses du joueur.
- Correction des bugs et amélioration de l'interface graphique.

#### Corentin :
- Gestion des pions d'hypothèses et d'absences (données et affichage).
- Continuation du développement de l'IA de déduction heuristique.
- Réduction de domaines à chaque nouvel indice collecté.

### 2. Fonctionnalités Développées

 - Gestion des pions d'absence, d'hypothèse et d'hypothèse d'absence
 - Gestion de l'affichage des présences, des absences et des hypothèses
 - Ajout de la possibilité de visualiser le film de la Réalité en fin de Partie
 - Restructuration du MVC
 - Finalisation de l'IA de déduction utilisant Choco-Solver
 - Développement de l'IA de déduction heuristique
 - Implémentation de l'IA d’assistance au joueur en utilisant Choco-Solver (commencement)

## Itération 6

### 1. Répartition des Tâches

#### Mathieu :
- Terminer l'IA d'assistance Choco-Solver.
- Début de l'implémentation de l'IA joueuse.
          
#### Enzo :
- Comparaison de l'efficacité des deux IA de déduction.
- Recherche du coupable via l'IA de déduction heuristique.
- Commencement de l'implémentation de l'IA d'assistance heuristique.

#### Corentin :
- Recherche du coupable via l'IA de déduction heuristique.
- Génération de scénarios pour renouveler l'expérience de jeu.

### 2. Fonctionnalités Développées

 - Comparaison de l'efficacité des deux IA de déduction
 - Recherche du coupable via l'IA de déduction heuristique
 - Terminer l'IA d'assistance Choco-Solver
 - Début de l'implémentation de l'IA joueuse
 - Génération de scénarios pour renouveler l'expérience de jeu
 - Commencement de l'implémentation de l'IA d'assistance heuristique

## Itération 7

### 1. Répartition des Tâches

#### Mathieu :
- Finalisation de l'implémentation de l'IA joueuse
- Fixage des bugs liés à l'interface graphique
          
#### Enzo :
- Comparaison des différentes IA d'assistance
- Finalisation de l'implémentation de l'IA d'assistance heuristique

#### Corentin :
- Analyses portant sur le générateur de scénarios
- Réédaction du document final

#### Tout le groupe :
- Création de la vidéo de démonstration
- Soutenance finale

### 2. Fonctionnalités Développées

 - Finalisation du développement de l'IA joueuse
 - Analyses portant sur le générateur de scénarios
 - Finalisation de l'implémentation de l'IA d'assistance heuristique
 - Comparaison des différentes IA d'assistance
 - Création d'une vidéo de démonstration globale de l'application
