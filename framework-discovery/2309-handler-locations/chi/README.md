# 2309 chi handler locations

Expectations derived by reading this fixture's own source (written before scanning).

| endpoint | expected handler file | expected line range | expected method/class | source of expectation |
|---|---|---|---|---|
| GET /users | handlers.go | 5-7 | ListUsers | func in other file; registration main.go:11 |
| POST /users | main.go | 12-14 | (closure) | inline closure main.go 12-14 |
