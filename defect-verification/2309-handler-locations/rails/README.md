# 2309 Rails (expectations written before scanning, from reading the source)
Registration: config/routes.rb (lines 2-5).

| endpoint | expected handler file | expected line range | expected method/class | source of expectation |
|---|---|---|---|---|
| GET /articles | app/controllers/articles_controller.rb | 2-4 | index / ArticlesController | `def index` ... `end` |
| POST /articles | app/controllers/articles_controller.rb | 6-9 | create / ArticlesController | idem |
| GET /articles/:id | app/controllers/articles_controller.rb | 11-13 | show / ArticlesController | idem |
| GET /widgets (resources) | app/controllers/widgets_controller.rb | 2-4 | index / WidgetsController | idem; registration routes.rb:5 |
| GET /widgets/:id (resources) | app/controllers/widgets_controller.rb | 6-8 | show / WidgetsController | idem |

Expected endpoint count: 5. registration populated for all (handler moved to another file).
