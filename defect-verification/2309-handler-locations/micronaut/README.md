# 2309 micronaut handler locations

Expectations derived by reading this fixture's own source (written before scanning).

| endpoint | expected handler file | expected line range | expected method/class | source of expectation |
|---|---|---|---|---|
| GET /users | src/main/java/demo/UserController.java | 10-12 (9-12 if annotation included) | list / UserController | annotated method |
| POST /users/new | src/main/java/demo/UserController.java | 15-17 (14-17 if annotation included) | create / UserController | annotated method |
