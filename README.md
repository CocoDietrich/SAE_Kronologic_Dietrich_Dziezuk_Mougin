# README - Projet Kronologic

## Itération 1

### 1. Partage du Travail

Équipe de Développement

Développeur 1 : Responsable de l’implémentation de l’interface graphique (Vues et contrôleurs).

Développeur 2 : Responsable de la logique métier (Modèles et gestion des indices).

Développeur 3 : Responsable de l’architecture globale et de l’intégration du modèle MVC.

Répartition des Tâches par Itération

Itération 1 :

Développeur 1 : Création de VueAccueil, VueCarte, VuePoseQuestion.

Développeur 2 : Développement de Partie, GestionnaireIndices, Indice.

Développeur 3 : Intégration des composants et mise en place de MainMVC.

2. Fonctionnalités Développées

Itération 1 :

Affichage de l’écran d’accueil avec options de lancement.

Gestion de la vue des indices.

Interaction utilisateur via boutons cliquables.

Fonctionnalité "Poser une question" (sélection de lieu, temps et personnage).

Mise à jour de l'historique des indices.

3. Dépendances

Bibliothèques Utilisées

JavaFX : Pour la gestion de l’interface graphique.

PlantUML : Utilisé pour la génération des diagrammes UML.

JUnit 5 (prévu) : Pour les tests unitaires.

4. Mode d’Emploi

Lancer l’Application

Cloner le dépôt :

git clone <url_du_projet>

Compiler le Projet :

javac -cp .;javafx-sdk/lib/* Kronologic/MainMVC.java

Exécuter l’Application :

java -cp .;javafx-sdk/lib/* Kronologic.MainMVC

Fonctionnalités Disponibles

Accueil : Lancer une partie ou quitter le jeu.

Poser une Question : Sélectionner un lieu, un temps et un personnage.

Affichage des Indices : Visualiser l’historique des indices découverts.

