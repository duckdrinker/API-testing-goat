# 2309 Koa (handler location)

koa 2.15.3 + @koa/router, CommonJS. 2 routes.

| endpoint | expected handler file | expected line range | expected method name | source of expectation |
|---|---|---|---|---|
| GET /users/:id | handlers/users.js | 3-6 | getUser | `async function getUser` in users.js |
| POST /inline | app.js | 11-17 | (anonymous) | inline async arrow spanning lines 11-17 |

Moved handler (/users/:id) should have `registration` = app.js:9.
