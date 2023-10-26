README for Project2
Project Setup:
Prerequisites:

Java JDK 11
Apache Maven (for building the project)
Library Dependencies:
All necessary libraries are located within the lib directory.

Setup & Run:

Ensure Java 11 is installed and properly set up on your system.
Ensure Maven is installed and added to your system's PATH.
Navigate to the root directory of the project.
Build the project using the following command:
```bash
mvn package
```

After building, run the application using the following command:
```bash
java --module-path lib --add-modules javafx.controls,javafx.fxml -jar target/project2-1.0-SNAPSHOT.jar
```

And the default username and password is `admin`