# 2309 Next.js Pages Router (handler location)

next 14.2.5. 2 routes (default export in pages/api).

| endpoint | expected handler file | expected line range | expected method name | source of expectation |
|---|---|---|---|---|
| /api/users | pages/api/users.js | 4-6 | handler | `export default function handler` |
| /api/orders | pages/api/orders.js | 5-10 | ordersHandler | `export default async function ordersHandler` (endpoint(s) per HTTP method possible) |

Handler must NOT be line 1.
