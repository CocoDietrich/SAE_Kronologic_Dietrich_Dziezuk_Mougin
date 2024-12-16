# README - Projet Kronologic

## Itération 1

### 1. Répartition des Tâches

Mathieu : - Suivre un tutoriel complet sur Choco-Solver afin de se familiariser avec l’outil.
          - Réaliser des prototypes pour résoudre des problèmes simples. Cela a pour objectif de saisir le fonctionnement de la programmation par contraintes.
          - Comprendre comment intégrer Choco-Solver au projet.
          
Enzo et Corentin : - Encoder les données du jeu et créer des classes Java permettant de les modéliser (lieux, personnages, indices, temps).
                   - Développer une architecture MVC afin de lier les données de jeu avec l’interface graphique de l’application.
                   - Commencer le développement de l’interface graphique (page d’accueil et redirection vers la feuille de jeu).

### 2. Fonctionnalités Développées

- Créer une page d'accueil
- Création de l'interface de jeu (éléments visuels de la feuille de jeu et de la pose de question)
- Permettre au joueur de poser une question (sans le placement automatique des pions)

### 3. Dépendances

#### Bibliothèques Utilisées

- JavaFX : Pour la gestion de l’interface graphique.

- PlantUML : Utilisé pour la génération des diagrammes UML.

- Choco-Solver : Cette bibliothèque servira à développer les IA dans la suite du projet. Durant cette première itération, elle n'a été utilisée uniquement adin qu'on la comprenne et qu'on la prenne en main grâce à la réalisation des prototypes.

### 4. Mode d’Emploi

Afin de lancer l’epplication, il faut dans un premier cloner le dépôt grâce à la commande git clone https://github.com/CocoDietrich/SAE_Kronologic_Dietrich_Dziezuk_Mougin.git.
Ensuite, lancez la classe MainMVC afin d'avoir accès aux fonctionnalités déjà disponibles.

#### Fonctionnalités Disponibles :

- Accueil : Lancer une partie ou quitter le jeu.
- Poser une Question : Sélectionner un lieu, un temps et un personnage.
- Affichage des Indices : Visualiser l’historique des indices découverts.

