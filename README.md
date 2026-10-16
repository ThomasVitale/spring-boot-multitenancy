# Spring Boot Multitenancy

Example project demonstrating multitenancy with Spring Boot, Spring Security, Spring Cloud Gateway, Spring Data JPA, Arconia, OpenTelemetry, and Keycloak.

## Pre-requisites

* Java 25
* Podman/Docker

## Edge Service

Run the Edge Service application as follows:

```shell
./gradlew bootRun
```

Alternatively, you can use the [Arconia CLI](https://arconia.io/docs/arconia-cli/latest/):

```shell
arconia dev
```

Under the hood, the Arconia framework will automatically spin up a [Keycloak](https://docs.arconia.io/arconia/latest/dev-services/keycloak/) identity provider, a [Redis](https://docs.arconia.io/arconia/latest/dev-services/redis/) data store, and a [Grafana LGTM](https://docs.arconia.io/arconia/latest/dev-services/lgtm/) observability platform using Testcontainers (see [Arconia Dev Services](https://docs.arconia.io/arconia/latest/dev-services/) for more information).

The application will be accessible at http://localhost:80.

## Instrument Service

Run the Instrument Service application as follows:

```shell
./gradlew bootRun
```

Alternatively, you can use the [Arconia CLI](https://arconia.io/docs/arconia-cli/latest/):

```shell
arconia dev
```

Under the hood, the Arconia framework will automatically spin up a [Keycloak](https://docs.arconia.io/arconia/latest/dev-services/keycloak/) identity provider, a [PostgreSQL](https://docs.arconia.io/arconia/latest/dev-services/postgresql/) database, and a [Grafana LGTM](https://docs.arconia.io/arconia/latest/dev-services/lgtm/) observability platform using Testcontainers (see [Arconia Dev Services](https://docs.arconia.io/arconia/latest/dev-services/) for more information).

The application will be accessible at http://localhost:8181.

## Usage

Two tenants are configured: `dukes` and `beans`. Ensure you add the following configuration to your `hosts` file to resolve tenants from DNS names.

```bash
127.0.0.1       dukes.rock
127.0.0.1       beans.rock
```

Now open the browser window and navigate to `http://dukes.rock/instruments/`. You'll be redirected to the Keycloak authentication page. Log in with `isabelle/password`. The result will be the list of instruments from the Dukes rock band.

Now open another browser window and navigate to `http://beans.rock/instruments/`. You'll be redirected to the Keycloak authentication page. Log in with `bjorn/password`. The result will be the list of instruments from the Beans rock band.
