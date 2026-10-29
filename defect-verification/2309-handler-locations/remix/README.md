# 2309 Remix (handler location)

@remix-run 2.11.0, resource routes. 3 endpoints.

| endpoint | expected handler file | expected line range | expected method name | source of expectation |
|---|---|---|---|---|
| GET /api/users | app/routes/api.users.js | 4-6 | loader | `export async function loader` |
| POST /api/users | app/routes/api.users.js | 8-11 | action | `export async function action` |
| GET /api/items | app/routes/api.items.js | 4-6 | loader | `export const loader = async () => {}` |

Handler must NOT be line 1.
