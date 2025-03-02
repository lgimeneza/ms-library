# MS Library

This is a Kotlin-based microservice for managing a library, built with Spring Boot and Gradle. The project follows the principles of hexagonal architecture to ensure a clean separation of concerns and maintainability.

## Table of Contents

- [Getting Started](#getting-started)
- [Building and Running](#building-and-running)
- [Running Tests](#running-tests)
- [License](#license)

## Getting Started

### Prerequisites

- Java 17
- Gradle 8.12.1

### Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/lgimeneza/ms-library.git
    cd ms-library
    ```

2. Build the project:
    ```sh
    ./gradlew build
    ```

## Building and Running

To build and run the application, use the following command:
```sh
./gradlew bootRun
```
The application will be available at `http://localhost:8080`.

## Running Tests

To run the tests, use the following command:
```sh
./gradlew test
```

## License

Distributed under the MIT License. See `LICENSE` for more information.
