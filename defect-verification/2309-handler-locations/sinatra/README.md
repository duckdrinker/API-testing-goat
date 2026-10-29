# 2309 Sinatra (expectations written before scanning)
Single file app.rb; handler = full block range.

| endpoint | expected handler file | expected line range | expected method/class | source |
|---|---|---|---|---|
| GET /hello | app.rb | 3-6 | (block) | `get ... do` to `end` |
| POST /echo | app.rb | 8-12 | (block) | idem |
| GET /one | app.rb | 14-14 | (block) | one-line block |

Expected endpoint count: 3.
