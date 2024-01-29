# Spring Boot Multitenancy

## Stack

* Java 21
* Spring Boot 3.2
* Grafana OSS

## Usage

You can use Docker Compose to set up Keycloak and the Grafana observability stack.

From the project root folder, run Docker Compose.

```bash
docker-compose up -d
```

The Instrument Service application can be run as follows to rely on Testcontainers to spin up a PostgreSQL database:

```bash
./gradlew bootTestRun
```

The Edge Service application can be run as follows:

```bash
./gradlew bootRun
```

Two tenants are configured: `dukes` and `beans`. Ensure you add the following configuration to your `hosts` file to resolve tenants from DNS names.

```bash
127.0.0.1       dukes.rock
127.0.0.1       beans.rock
```

Now open the browser window and navigate to `http://dukes.rock/instruments/`. You'll be redirected to the Keycloak authentication page. Log in with `isabelle/password`. The result will be the list of instruments from the Dukes rock band.

Now open another browser window and navigate to `http://beans.rock/instruments/`. You'll be redirected to the Keycloak authentication page. Log in with `bjorn/password`. The result will be the list of instruments from the Beans rock band.
