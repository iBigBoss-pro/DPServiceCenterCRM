# DPServiceCenterCRM — repaired source baseline

## Requirements
- JDK 21
- Maven 3.9+

## Run
```bash
mvn clean test
mvn spring-boot:run
```
Then check `http://localhost:8080/api/health`.

By default, the process uses an in-memory H2 database so the Spring Boot shell can start locally without a separate PostgreSQL installation. The database is ephemeral and the current domain classes are plain Java domain objects, not mapped JPA entities.

## Important scope note
This archive preserves and fixes the existing domain model. It is **not yet a complete working CRM**: REST CRUD endpoints, repositories, service layer, JPA mappings/schema, authentication/authorization, and database migrations still need to be implemented before real customer/order/payment data should be entered. PostgreSQL driver is present; configure `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, and `SPRING_DATASOURCE_DRIVER` when persistent database support is implemented.

## Main corrections in this repair
- Standard Maven source tree and Java package names.
- Standard Java 21/Spring Boot application entry point; no preview-language features.
- Resolved the base/template `getVersion()` method collision.
- Added validation for payment inputs, stock balances, order pricing, approvals, notifications and document lifecycle.
- Fixed partial state mutation in order-condition approval.
- Fixed repeated document copy numbers in repeated calls on the same domain object.
- Used calendar months for warranty expiry.
- Added bean accessors for view DTOs.

The domain logic requires automated tests and a persistence/service layer before production use. This README deliberately does not claim that absent functionality is implemented.
