# 2309 Nuxt (handler location)

nuxt 3.12.4, server/api routes. 3 routes.

| endpoint | expected handler file | expected line range | expected method name | source of expectation |
|---|---|---|---|---|
| GET /api/users | server/api/users.get.js | 4-6 | (defineEventHandler default export) | `export default defineEventHandler(...)` lines 4-6 |
| POST /api/users | server/api/users.post.js | 4-7 | (same) | lines 4-7 |
| GET /api/items/:id | server/api/items/[id].get.js | 4-7 | (same) | lines 4-7 |

Handler must NOT be line 1.
