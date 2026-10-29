# 2309 aspnet-minimal handler locations

Expectations derived by reading this fixture's own source (written before scanning).

| endpoint | expected handler file | expected line range | expected method/class | source of expectation |
|---|---|---|---|---|
| GET /users | Handlers.cs | 3-6 | ListUsers / Handlers | method group in other class; registration Program.cs:4 |
| POST /users | Program.cs | 5-5 | (lambda) | single-line lambda |
| GET /local | Program.cs | 12-15 | Get / LocalHandlers | method group, method in same file |
