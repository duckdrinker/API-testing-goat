# 2309 Hono (handler location)

hono 4.5.0, ESM. 2 routes.

| endpoint | expected handler file | expected line range | expected method name | source of expectation |
|---|---|---|---|---|
| GET /users | handlers/users.js | 1-4 | listUsers | `export function listUsers` in users.js |
| POST /inline | app.js | 8-13 | (anonymous) | inline async arrow spanning lines 8-13 |

Moved handler (/users) should have `registration` = app.js:6.
