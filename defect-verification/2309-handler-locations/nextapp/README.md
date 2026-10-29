# 2309 Next.js App Router (handler location)

next 14.2.5. 3 routes.

| endpoint | expected handler file | expected line range | expected method name | source of expectation |
|---|---|---|---|---|
| GET /api/users | app/api/users/route.js | 4-6 | GET | `export async function GET` |
| POST /api/users | app/api/users/route.js | 8-11 | POST | `export async function POST` |
| GET /api/items | app/api/items/route.js | 4-6 | GET | `export function GET` |

Handler must NOT be line 1.
