# Application Yoga App

## Installations
Pour le bon fonctionnement du projet, vous aurez besoin de plusieurs éléments.
   ### WampServer
   - Télécharger et installer WampServer
   - > https://wampserver.aviatechno.net/index.php?affiche=install&lang=fr.
   
   ### Apache Maven
   - Télécharger et installer Apache Maven
   - > https://maven.apache.org/download.cgi
   
   ### Java Developpment Kit
   - Télécharger et installer Java Developpment Kit
   - > https://www.oracle.com/java/technologies/downloads/

   ### Node Module
   - Rendez-vous dans le dossier `/chemin/vers/votre/projet/Front-End/` et utilisez la commande `npm install` dans l'invité de commandes.

## Database Configuration

1. **MySQL Configuration:**

   - L'utilisateur par default est `root`, il n'y a pas de mot de passe.
   - Create a database named `your_database_name`.

2. **Création Base de données in PhpMyAdminer**

   - Rendez vous sur l'adresse suivante : `localhost`.
   - Acceder maintenant a `PhpMyAdmin` et connectez vous.
   - Créez votre base de données.
   - Depuis l'onglet import, importer le script pour la création de la database qui se trouve :
     > /chemin/vers/votre/projet/Front-End/ressources/sql/script.sql

3. **Configuration de la base de données dans Spring Boot:** 
   - Mise a jour de application properties `/chemin/vers/votre/projet/Back-End/SpringSecurityConfig/src/main/resources/application.properties` :
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
     spring.datasource.username=root
     spring.datasource.password=
     ```

## Installation dépendance  
  
**Front**  
Placez-vous dans le dosser `/front`, et lancez la commande suivante : `npm install`.

**Back**  
Placez-vous dans le dosser `/back`, et lancez la commande suivante : `mvn clean install`.

## Lancement du projet

- Pour lancer le projet, vous devrez tout d'abord créer les variables d'environnement pour Java et Maven.  
- Pour lancer le serveur Java, dans un terminal placez vous dans le dossier `/Back-End/SpringSecurityConfig` et entrez la commande `mvn spring-boot:run`.  
- Pour lancer le serveur Angular, dans un terminal placez vous dans le dossier `/Front-end` et entrez la commande `npm run start`.


## Acceder à l'application

Pour acceder à l'application, rendez-vous sur : `localhost:4200`.


## Lancer les tests  

**Test Front End**  

Placez-vous dans le dossier `/front`.  

Afin de lancer les tests, utilisez la commande suivante : `npm run test`.  
Pour visionnez le tableau de bord des couvertures de test, il faudra ouvrir le fichier `index.html`.  

Pour le trouver il vous suffit d'aller au chemin suivant : `\front\coverage\jest\lcov-report`.


**Test End to End**  

Placez-vous dans le dossier `/front`.  

Afin de lancer les tests, utilisez la commande suivante : `npm run e2e`.  
Pour générer la couverture : `npm run e2e:coverage`  
Pour visionnez le tableau de bord des couvertures des tests, il faudra ouvrir le fichier `index.html`.  

Pour le trouver il vous suffit d'aller au chemin suivant : `\front\coverage\lcov-report`.

**Test Back End**  

Placez-vous dans le dossier `/back`.  

Afin de lancer les tests, utilisez la commande suivante :` mvn clean test`.  
Pour visionnez le tableau de bord des couvertures de test, il faudra ouvrir le fichier `index.html`.  

Pour le trouver il vous suffit d'aller au chemin suivant : `/back/target/site/jacoco/`.  

**Couvertures de tests**  

Pour visionnez les captures d'ecrans des testes de couvertures de tests allez ici : `/ressources/coverage/`.
