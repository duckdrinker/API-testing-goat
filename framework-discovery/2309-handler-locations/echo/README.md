# 2309 echo handler locations

Expectations derived by reading this fixture's own source (written before scanning).

| endpoint | expected handler file | expected line range | expected method/class | source of expectation |
|---|---|---|---|---|
| GET /users | handlers.go | 5-7 | ListUsers | func declaration in other file; registration main.go:7 |
| POST /users | main.go | 8-10 | (closure) | inline closure in main.go lines 8-10 |
