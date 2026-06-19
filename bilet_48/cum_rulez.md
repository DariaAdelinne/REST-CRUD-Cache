# Bilet 48 — Agendă REST cu Proxy (autentificare XML + filtru dicționar XML)

## Arhitectură (Coregrafie)

```
Client → ProxyMicroservice :8080 → AgendaMicroservice :8081
```

- **ProxyMicroservice** (port 8080): autentifică cererea (Basic Auth, credențiale din `credentials.xml`), filtrează conținutul (termeni interzisi din `dictionary.xml`), forwardează la AgendaMicroservice
- **AgendaMicroservice** (port 8081): CRUD contacte (name, phone, email), in-memory

Pattern: **coregrafie** (service chaining) — nu există orchestrator central.

## Pornire cu Docker

```bash
docker-compose up --build
```

## Pornire locală (fără Docker)

Terminal 1 — AgendaMicroservice:
```bash
cd AgendaMicroservice
mvn spring-boot:run
```

Terminal 2 — ProxyMicroservice:
```bash
cd ProxyMicroservice
mvn spring-boot:run
```

## Cum faci cereri (totul prin Proxy pe portul 8080)

Autentificare: Basic Auth cu `admin:parola123` sau `student:sd2024`

### GET toate contactele
```bash
curl -u admin:parola123 http://localhost:8080/contacts
```

### POST contact nou
```bash
curl -u admin:parola123 -X POST http://localhost:8080/contacts \
  -H "Content-Type: application/json" \
  -d '{"name":"Ion Popescu","phone":"0721000000","email":"ion@example.com"}'
```

### PUT actualizare contact
```bash
curl -u admin:parola123 -X PUT http://localhost:8080/contacts/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Ion Popescu","phone":"0721111111","email":"ion@example.com"}'
```

### DELETE contact
```bash
curl -u admin:parola123 -X DELETE http://localhost:8080/contacts/1
```

## Testare cazuri speciale

### Fără credențiale → 401
```bash
curl http://localhost:8080/contacts
```

### Credențiale greșite → 401
```bash
curl -u admin:greșit http://localhost:8080/contacts
```

### Termen interzis din dicționar → 404
```bash
curl -u admin:parola123 -X POST http://localhost:8080/contacts \
  -H "Content-Type: application/json" \
  -d '{"name":"Hacker spam","phone":"000","email":"hack@virus.com"}'
```

## Credențiale (credentials.xml)
- `admin` / `parola123`
- `student` / `sd2024`

## Termeni interzisi (dictionary.xml)
spam, hack, virus, malware, exploit, injection

## SOLID respectat
- **S**: fiecare clasă are o singură responsabilitate (validator, filter, forwarder, controller separate)
- **O**: poți adăuga un nou tip de validator/filter fără să modifici ProxyController
- **L**: XmlCredentialValidator și XmlDictionaryFilter pot fi înlocuite cu orice implementare a interfeței
- **I**: interfețe mici și focusate (ICredentialValidator, IContentFilter, IRequestForwarder)
- **D**: ProxyController depinde de interfețe, nu de implementări concrete
