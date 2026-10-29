# 2309 Vapor (expectations written before scanning)
Registration: Sources/App/routes.swift lines 5-7.

| endpoint | expected handler file | expected line range | expected method/class | source |
|---|---|---|---|---|
| GET /todos (use: ctrl.index) | Sources/App/TodoController.swift | 4-6 | index / TodoController | `func index` |
| POST /todos (use: ctrl.create) | Sources/App/TodoController.swift | 8-11 | create / TodoController | `func create` |
| GET /closure (trailing closure) | Sources/App/routes.swift | 7-10 | (closure) | closure body range |

Expected endpoint count: 3. registration populated for the two `use:` routes (other file).
