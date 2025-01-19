# prices

### Prerequisites
* Java 21
* Maven
* Docker (optional)

### Running Tests
To run all the unit and integration tests, use the following command

    mvn test

### Build the Application
Enter into the `prices` directory and run the following command

    mvn clean install

### Run the Application
After building the jar file run the following command inside the `prices` directory

    java -jar target/prices-0.0.1-SNAPSHOT.jar

### Build the Docker Image
If you want to run the app in a container run the following commands in the root directory

    docker build -t your-image-name .
    docker run -p 8080:8080 your-image-name

### Swagger
Once the application is running, you should be able to access the Swagger view

http://localhost:8080/swagger-ui.html

### H2 Database Console
If you want to access the H2 console use this jdbc url `jdbc:h2:mem:prices` and leave the default values

http://localhost:8080/h2