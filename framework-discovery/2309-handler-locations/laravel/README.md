# 2309 Laravel (expectations written before scanning)
Registration: routes/api.php lines 8-10.

| endpoint | expected handler file | expected line range | expected method/class | source |
|---|---|---|---|---|
| GET /api/items | app/Http/Controllers/ItemController.php | 7-10 | index / ItemController | `public function index` |
| POST /api/items | app/Http/Controllers/ItemController.php | 12-15 | store / ItemController | idem |
| GET /api/ping (invokable) | app/Http/Controllers/PingController.php | 7-10 | __invoke / PingController | `__invoke` |

Expected endpoint count: 3 (path may or may not carry /api prefix). registration populated.
