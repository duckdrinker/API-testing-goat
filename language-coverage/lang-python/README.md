# Language coverage — Python (FastAPI / Flask / Django-DRF)

**Xygeni detectors:** `excessive_data_exposure`, `pii_leak_in_response`, `broken_object_level_authorization` (Phase B), `broken_function_level_authorization` (Phase B), `cors_misconfiguration`, `jwt_misconfiguration`, `rate_limit_absence`
**Reference test cases:** ASEC-056/057, ASEC-064/065, ASEC-070, ASEC-075, ASEC-080, ASEC-087, ASEC-100, ASEC-101

Three Python frameworks are involved because the individual detector tickets specify different
ones for different detectors (BOLA/BFLA Phase B are DRF-specific `permission_classes` patterns;
CORS is `flask-cors`; the rest are framework-agnostic). Each framework's files stand on their own —
this is not meant to be a single runnable app.

This fixture also exercises the framework endpoint-detection ("API Discovery") layer for
Django/DRF and FastAPI. See `views.py` + `serializers.py` + `subapp/` (Django/DRF) and the
`get_item_v2()` addition to `fastapi_app.py` (FastAPI) below. See test-plan-apisec-framework-discovery.html.

## `fastapi_app.py`

- `get_profile()` (`GET /api/users/profile`) — `ssn` unreferenced, sensitivity-tagged →
  `excessive_data_exposure` (056) + `pii_leak_in_response` (064).
- `get_summary()` (negative) — only operation-relevant fields → no finding (057, 065).
- `update_profile()` (`PUT /api/users/profile`) — `role` binds through → `mass_assignment`.
- `UserProfileUpdateRequest.enabledPassword` (`bool`) — credential-shaped field name, but a
  boolean type → must NOT be sensitivity-tagged by `excessive_data_exposure`/`pii_leak_in_response`
  (name-based heuristics must respect the field's actual type).
- `verify_token()` — `jwt.decode(token, options={"verify_signature": False})` →
  `jwt_misconfiguration` (087), severity Critical.
- `get_item_v2()` (`GET /api/items/{item_id}`) — combines `Path(...)` (required PATH param),
  `Query(default=10, le=100)` (optional QUERY) and `Query(..., min_length=1)` (required QUERY),
  `Header(None)`, `Cookie(None)`, `Depends(get_current_user)` (dependency-injection security
  requirement, not a plain parameter) and `Security(oauth2_scheme, scopes=["read:items"])`
  (oauth2-scopes security requirement), plus `operation_id="get_item_v2"`, `tags=["items"]`,
  `deprecated=True`, `status_code=200` on the decorator and an explicit `-> ItemResponse` return
  type. See test-plan-apisec-framework-discovery.html.

## `flask_app.py`

- `CORS(app, origins='*', supports_credentials=True)` → `cors_misconfiguration` (080).
- `create_ticket()` (`POST /api/support-tickets`) — authenticated, user input, no flask-limiter
  anywhere in the file → `rate_limit_absence` (100).

## `flask_app.py` — Flask API Discovery additions

- `get_item('/api/flask-items/<int:item_id>')` — Flask 2.x `@app.get()` shortcut; `<int:...>`
  converter → PATH parameter typed int. See test-plan-apisec-framework-discovery.html.
- `create_item('/api/items')` — `@app.post()` shortcut; `@login_required` → framework discovery
  (security). See test-plan-apisec-framework-discovery.html.
- `file_by_owner('/api/files/<uuid:owner_id>/<path:filepath>')` — classic
  `@app.route(methods=['GET', 'DELETE'])` with two converters (`uuid`, `path`) and two HTTP
  methods on one view function; `@jwt_required` → framework discovery (security). See
  test-plan-apisec-framework-discovery.html.
- `legacy_search('/api/legacy-search', endpoint='search_endpoint')` — `endpoint=` overrides the
  operationId to `search_endpoint`. See test-plan-apisec-framework-discovery.html.
- `items_bp` (`Blueprint('items_bp', __name__, url_prefix='/api/v2/items')`) — `get_item_by_slug`
  (`<slug:...>` converter) and `search_items` (Flask 2.x `@items_bp.get()` shortcut,
  `@roles_required('admin')` → framework discovery/security) — the Blueprint's `url_prefix` must
  be applied to both discovered paths. See test-plan-apisec-framework-discovery.html.
- `ItemAPI(MethodView)` (`get`/`post`/`delete`, mounted at `/api/v2/items-legacy` via
  `add_url_rule`) — each HTTP method becomes its own endpoint, `operationId="ItemAPI.<method>"`;
  `post`/`delete` carry `@login_required`/`@roles_required('admin')` → framework discovery
  (security). **Known, documented limitation demonstrated deliberately (not a bug to fix here):**
  the detector does not resolve `add_url_rule()`'s actual path, so all three methods are expected
  to be reported at path `"/"`, not at `/api/v2/items-legacy`. See test-plan-apisec-framework-discovery.html.

## `drf_views.py` + `urls.py`

- `OrderDetailView` (`permission_classes = [IsAuthenticated]`) — BOLA Phase B, check absent (070).
- `SafeOrderDetailView` (`permission_classes = [IsOwnerOrAdmin]`) — check present, cleared (070).
- `AdminReportView` (`permission_classes = [IsAuthenticated]`) — BFLA Phase B, check absent (075).
- `SafeAdminReportView` (`permission_classes = [IsAdminUser]`) — check present, cleared (075).
- `SupportTicketDrfView` — no `throttle_classes` → `rate_limit_absence` (101).
- `FeedbackView` (`throttle_classes = [FeedbackThrottle]`, negative) — no finding.

## `views.py` + `serializers.py` + `urls.py` + `subapp/` — Django/DRF API Discovery

- `ArchiveView` (`GET /archive/<year>/<month>/`, via `re_path(r'^archive/(?P<year>[0-9]{4})/(?P<month>[0-9]{2})/$', ..., name='archive')`
  in `urls.py`) — named regex groups become PATH parameters, and `name=` overrides the
  operationId. See test-plan-apisec-framework-discovery.html.
- `ItemDetailView` / `ItemByTokenView` / `ItemBySlugView` / `FileView` (`items/<int:id>/`,
  `items/<uuid:token>/`, `items/<slug:slug>/`, `files/<path:filepath>/`) — exercises the
  `int` / `uuid` / `slug` / `path` converters (previously only `<str:id>` was covered). See
  test-plan-apisec-framework-discovery.html.
- `item_collection()` (`GET/POST /items/collection/`, `@api_view(['GET', 'POST'])` with
  `@permission_classes([IsAuthenticated])` and `@authentication_classes([TokenAuthentication])`)
  — function-based DRF view detection. See test-plan-apisec-framework-discovery.html.
- `ItemListView` (`ListCreateAPIView`, `items/`) — Generic Concrete View producing
  GET+POST automatically with no explicit handler methods. See test-plan-apisec-framework-discovery.html.
- `ItemDetailView` (`RetrieveUpdateDestroyAPIView`, `items/<int:id>/`) — Generic
  Concrete View producing GET+PUT+PATCH+DELETE automatically. See test-plan-apisec-framework-discovery.html.
- `ItemViewSet` (`ModelViewSet`, also registered on a `DefaultRouter` in `urls.py` as `items-vs`) —
  full CRUD action set plus a custom `@action(detail=True, methods=['post'], url_path='mark-archived')`
  endpoint, all emitted at the placeholder path `"/"` from direct class inspection (independent of
  any URL resolution). **Known, documented limitation (from the issue itself), not asserted here:**
  `router.register()`-based URL resolution is not traced, so the real `/items-vs/...` path is not
  expected to appear anywhere in the report. See test-plan-apisec-framework-discovery.html.
- `ItemReadOnlyViewSet` (`ReadOnlyModelViewSet`, registered as `items-ro`) — only
  list+retrieve are produced, never create/update/destroy. See test-plan-apisec-framework-discovery.html.
- `ItemSerializer` (`serializers.ModelSerializer`, explicit class-level fields + `Meta.fields`)
  — used as a RESPONSE object in `ItemBySlugView` (`ItemSerializer(items, many=True).data`,
  no `data=` kwarg) → counted as a DataObject linked to the endpoint; used for the REQUEST body
  in `item_collection()` (`ItemSerializer(data=request.data)`) → not counted as a response
  DataObject. See test-plan-apisec-framework-discovery.html.
- `EmptySerializer` (`serializers.Serializer`, no fields) — must produce zero DataObjects when
  analyzed. See test-plan-apisec-framework-discovery.html.
- `subapp/urls.py` (`include('subapp.urls')`, mounted at `path('sub/', ...)` in `urls.py`) —
  cross-file `include()` resolution; `subapp.views.ItemView` / `PingView` resolve to
  `sub/items/<int:id>/` and `sub/ping/` with the parent's `sub/` prefix applied. See
  test-plan-apisec-framework-discovery.html.

## Expected scan result

```
xygeni-cli apisecurity --dir . --detectors excessive_data_exposure,pii_leak_in_response,broken_object_level_authorization,broken_function_level_authorization,cors_misconfiguration,jwt_misconfiguration,rate_limit_absence
```
