<div align="center">

# Order Flow MS

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![RabbitMQ](https://img.shields.io/badge/Rabbitmq-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

</div>

## Description

Order Flow MS is a microservice application designed to handle order management with integration of RabbitMQ for messaging and MongoDB for data storage. This application demonstrates how to use these technologies together within a Spring Boot application.

## Technologies and Project Dependencies

- **RabbitMQ:** Message broker for asynchronous communication.
- **Spring AMQP:** Integration of Spring with RabbitMQ for sending and receiving messages.
- **Spring Boot:** Framework for building microservices.
- **Spring Web:** For creating RESTful APIs.
- **Spring Devtools:** Additional tools for development, such as automatic reload.
- **Spring Data MongoDB:** Abstraction for data access with MongoDB.
- **Lombok:** Reduces boilerplate code like getters, setters, and constructors.
- **MongoDB:** NoSQL database for data persistence.
- **Docker Compose:** Tool for defining and running multi-container Docker applications.

## Testing the Project

1. Ensure Docker and Docker Compose are installed on your machine.
2. Navigate to the project directory in your terminal and run the following command to start the containers:
    ```bash
    docker-compose up -d
    ```
3. Start the Spring Boot application using:
    ```bash
    mvn spring-boot:run
    ```
4. Access RabbitMQ at [http://localhost:15672/](http://localhost:15672/) and publish a message to RabbitMQ.
5. Test the available endpoint.

## Endpoint for Testing

| Route                                          | Description                |
|------------------------------------------------|----------------------------|
| <kbd>GET /customers/{customerId}/orders</kbd>  | Retrieve a list of orders for a specific customer. |

## Contribution

- To contribute, fork this repository and submit your changes via pull requests.
- For bug reports or improvement suggestions, open an issue on the project page.

