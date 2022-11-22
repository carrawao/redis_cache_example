# Redis Cache Example

This repository was created in order to present a simple example regarding Redis as cache. It was part of the topic NoSQL databases imparted at the Duale Hochschule Baden-Württemberg (DHBW) Stuttgart. With the current state of the application the default configuration of Redis was used (Standalone).

## Installation

1- Setup Java (follow instructions [hier](https://www.java.com/en/download/help/download_options.html) according to the operating system).

2- Setup Maven (see steps [hier](https://maven.apache.org/install.html))

3- Setup Node.JS and npm (find steps [hier](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm))

## Run application
1- Create a database in MySQL (see instructions [hier](https://www.mysqltutorial.org/mysql-create-database/)). Once the database is setup, then naviagte to the file *application.properties* located in */src/main/resources* and the corresponding username and password fields should be given. By deafult it was set to username "root" with no password. Conclude with running the MySQL Server.


2- Navigate to the root folder of the project. In order to download all the necessary dependencies regarding the Java implementation, run from terminal (if IDE has a section for Maven, this can be done directly from there):

``
mvn clean install
``

3- On the root folder of the project in order to install all dependencies regarding the fronted implementation, run:

``
npm install
``

4- Run the main method of the file *ReactAndSpringDataRestApplication.java* located in */src/main/java/com/redis/rosebowl/*.

5- In order to initialize the frontend application, run the following command:

``
npm run watch
``

6- Finally, go to your browser and type *localhost:8080* and the application should be running.
