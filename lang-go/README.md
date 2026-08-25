# Language coverage — Go

**Xygeni detectors:** `cors_misconfiguration`, `jwt_misconfiguration`, `mass_assignment`, `rate_limit_absence`
**Reference test cases:** ASEC-082, ASEC-089, ASEC-096, ASEC-103 (Go half)

BOLA/BFLA Phase B and excessive_data_exposure/pii_leak_in_response do not list Go among their
per-language cases in the Scanner TP, so this fixture only covers the four detectors that do.

## What this fixture demonstrates

- `main()` — `handlers.CORS(handlers.AllowedOrigins([]string{"*"}), handlers.AllowCredentials())`
  (gorilla/handlers) → `cors_misconfiguration` (082).
- `verifyToken()` — `jwt.Parse` with no signing-method check in the keyfunc before returning the
  secret → `jwt_misconfiguration` (089).
- `updateProfile()` (`PUT /api/users/profile`) — `UserProfileUpdateRequest.Role` binds straight
  through → `mass_assignment` (096).
- `createFeedback()` (`POST /api/feedback`) — no `tollbooth` / `golang.org/x/time/rate` usage
  anywhere in the module → `rate_limit_absence` (103, Go half).

## Expected scan result

```
xygeni-cli apisecurity --dir . --detectors cors_misconfiguration,jwt_misconfiguration,mass_assignment,rate_limit_absence
```
