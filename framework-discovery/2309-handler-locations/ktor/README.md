# 2309 ktor handler locations

Expectations derived by reading this fixture's own source (written before scanning).

| endpoint | expected handler file | expected line range | expected method/class | source of expectation |
|---|---|---|---|---|
| GET /inline | src/main/kotlin/demo/Routing.kt | 9-11 | (lambda) | inline lambda full range |
| GET /delegated | src/main/kotlin/demo/Handlers.kt | 6-8 | handleDelegated | lambda only delegates; function it calls (registration Routing.kt:12-14) |
