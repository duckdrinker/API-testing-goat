# 2309 Express (handler location)

Express 4.19.2, CommonJS. 8 routes declared.

| endpoint | expected handler file | expected line range | expected method name | source of expectation |
|---|---|---|---|---|
| POST /login | handlers/login.js | 3-6 | login | `function login` in login.js |
| POST /factory | handlers/factory.js | 3-7 (makeHandler) or 4-6 (returned fn) | makeHandler | `app.post('/factory', makeHandler())`; factory call, either acceptable |
| GET /api/items | handlers/router.js | 5-7 | listItems | `router.get('/items', listItems)` on express.Router() |
| GET /inline | app.js | 17-23 | (anonymous) | inline arrow spanning lines 17-23 (whole function) |
| GET /ping | app.js | 25 (registration kept) | - | default import `module.exports = ping`: EXPECTED-LIMIT |
| GET /svc | app.js | 26 (registration kept) | - | `new Svc({}).handle`: EXPECTED-LIMIT |
| POST /orders | handlers/auth.js | 10-13 | createOrder | middleware chain `auth, createOrder`, last arg is handler |
| GET /local | app.js | 29-31 | localHandler | local function declaration |

Moved handlers should have `registration` populated (app.js line of the route).
