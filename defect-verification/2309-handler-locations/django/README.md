# 2309 Django (expectations written before scanning)
Registration: proj/urls.py (lines 11-14; router.register line 8).

| endpoint | expected handler file | expected line range | expected method/class | source |
|---|---|---|---|---|
| /plain/ (plain view) | app/views.py | 5-7 | plain_view | `def plain_view` |
| /cbv/ (class-based View, GET) | app/views.py | 11-12 (get method) or 10-12 (class) | get / HelloView | class-based |
| /apiview/ (@api_view) | app/api.py | 7-8 (6-8 if decorator included) | api_function | decorated function |
| GET /books/ (ViewSet.list) | app/api.py | 12-13 | list / BookViewSet | class's own override |
| GET /books/{pk}/ (ViewSet.retrieve) | app/api.py | 15-16 | retrieve / BookViewSet | class's own override |

Expected: at least those 5 endpoints (DRF router may add format-suffix variants / api root). registration populated (other file).
