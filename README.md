# Money Transfer API

A simple Spring Boot REST API for transferring money between in-memory accounts.

## Requirements

- Java 17+
- Maven

## Run

```bash
mvn spring-boot:run
```

The API starts at `http://localhost:8080`.

## Endpoints

- `GET /accounts` - list accounts
- `POST /accounts` - create an account
- `GET /accounts/{id}` - get an account
- `POST /transfer` - transfer money
- `GET /transfer` - list transfers

Example transfer request:

```json
{
  "fromAccountId": "acc-1",
  "toAccountId": "acc-2",
  "amount": 100.00
}
```

The application starts with accounts `acc-1`, `acc-2`, and `acc-3`. Data is stored in memory and is reset when the application restarts.

## Tests

```bash
mvn test
```

A Postman collection is available at `postman/money-transfer.postman_collection.json`.
