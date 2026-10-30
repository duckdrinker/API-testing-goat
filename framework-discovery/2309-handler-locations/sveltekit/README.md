# 2309 SvelteKit (handler location)

@sveltejs/kit 2.5.20, +server.js. 3 routes.

| endpoint | expected handler file | expected line range | expected method name | source of expectation |
|---|---|---|---|---|
| GET /api/users | src/routes/api/users/+server.js | 4-6 | GET | `export async function GET` |
| POST /api/users | src/routes/api/users/+server.js | 8-11 | POST | `export async function POST` |
| GET /api/items | src/routes/api/items/+server.js | 4-6 | GET | `export function GET` |

Handler must NOT be line 1.
