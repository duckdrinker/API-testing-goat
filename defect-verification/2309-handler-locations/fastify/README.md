# 2309 Fastify (handler location)

fastify 4.28.1, CommonJS. 2 routes.

| endpoint | expected handler file | expected line range | expected method name | source of expectation |
|---|---|---|---|---|
| GET /health | handlers/health.js | 3-5 | health | `async function health` in health.js |
| POST /inline | app.js | 7-13 | (anonymous) | inline async arrow spanning lines 7-13 |

Moved handler (/health) should have `registration` = app.js:5.
