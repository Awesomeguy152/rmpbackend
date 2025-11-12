### How to run
```bash
docker compose up --build
```
Smoke test:
```bash
curl -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d '{"email":"admin@example.com","password":"admin","role":"ADMIN"}'
curl -s -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d '{"email":"admin@example.com","password":"admin"}'
```
