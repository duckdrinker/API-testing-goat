# 2309 webflux handler locations

Expectations derived by reading this fixture's own source (written before scanning).

| endpoint | expected handler file | expected line range | expected method/class | source of expectation |
|---|---|---|---|---|
| GET /users | src/main/java/demo/UserHandler.java | 10-12 | listUsers / UserHandler | handler::listUsers in other class; registration RouterConfig.java:14 |
| POST /users | src/main/java/demo/RouterConfig.java | 15-15 | (lambda) | single-line lambda at line 15 |
