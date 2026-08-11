# Language coverage — Python (FastAPI / Flask / Django-DRF)

**Xygeni detectors:** `excessive_data_exposure`, `pii_leak_in_response`, `broken_object_level_authorization` (Phase B), `broken_function_level_authorization` (Phase B), `cors_misconfiguration`, `jwt_misconfiguration`, `rate_limit_absence`
**Reference test cases:** ASEC-056/057, ASEC-064/065, ASEC-070, ASEC-075, ASEC-080, ASEC-087, ASEC-100, ASEC-101

Three Python frameworks are involved because the individual detector tickets specify different
ones for different detectors (BOLA/BFLA Phase B are DRF-specific `permission_classes` patterns;
CORS is `flask-cors`; the rest are framework-agnostic). Each framework's files stand on their own —
this is not meant to be a single runnable app.

## `fastapi_app.py`

- `get_profile()` (`GET /api/users/profile`) — `ssn` unreferenced, sensitivity-tagged →
  `excessive_data_exposure` (056) + `pii_leak_in_response` (064).
- `get_summary()` (negative) — only operation-relevant fields → no finding (057, 065).
- `update_profile()` (`PUT /api/users/profile`) — `role` binds through → `mass_assignment`.
- `verify_token()` — `jwt.decode(token, options={"verify_signature": False})` →
  `jwt_misconfiguration` (087), severity Critical.

## `flask_app.py`

- `CORS(app, origins='*', supports_credentials=True)` → `cors_misconfiguration` (080).
- `create_ticket()` (`POST /api/support-tickets`) — authenticated, user input, no flask-limiter
  anywhere in the file → `rate_limit_absence` (100).

## `drf_views.py` + `urls.py`

- `OrderDetailView` (`permission_classes = [IsAuthenticated]`) — BOLA Phase B, check absent (070).
- `SafeOrderDetailView` (`permission_classes = [IsOwnerOrAdmin]`) — check present, cleared (070).
- `AdminReportView` (`permission_classes = [IsAuthenticated]`) — BFLA Phase B, check absent (075).
- `SafeAdminReportView` (`permission_classes = [IsAdminUser]`) — check present, cleared (075).
- `SupportTicketDrfView` — no `throttle_classes` → `rate_limit_absence` (101).
- `FeedbackView` (`throttle_classes = [FeedbackThrottle]`, negative) — no finding.

## Expected scan result

```
xygeni-cli apisecurity --dir . --detectors excessive_data_exposure,pii_leak_in_response,broken_object_level_authorization,broken_function_level_authorization,cors_misconfiguration,jwt_misconfiguration,rate_limit_absence
```
