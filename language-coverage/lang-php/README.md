# Language coverage — PHP / Laravel

**Xygeni detectors:** `cors_misconfiguration`, `jwt_misconfiguration`, `mass_assignment`, `rate_limit_absence`
**Reference test cases:** ASEC-083, ASEC-090, ASEC-097, ASEC-103 (PHP half)

Same scope rationale as `lang-go/`: BOLA/BFLA Phase B and the AST-level core detectors don't list
PHP among their per-language cases, so this fixture only covers the four that do.

## What this fixture demonstrates

- `config/cors.php` — `'allowed_origins' => ['*']` combined with `'supports_credentials' => true`
  → `cors_misconfiguration` (083).
- `JwtService::verify()` — `JWT::decode($t, $key, ['HS256', 'none'])` (Firebase PHP-JWT, "none"
  accepted) → `jwt_misconfiguration` (090).
- `UserController::update()` — the full request is mass-assigned via `$user->fill($request->all())`,
  and `role` is listed in `User::$fillable` → `mass_assignment` (097).
- `FeedbackController::store()` (`POST /api/feedback`, see `routes/api.php`) — authenticated,
  user-input endpoint with no `throttle` middleware / `RateLimiter::for(...)` applied →
  `rate_limit_absence` (103, PHP half).

## Expected scan result

```
xygeni-cli apisecurity --dir . --detectors cors_misconfiguration,jwt_misconfiguration,mass_assignment,rate_limit_absence
```

## Additional case: xygeni-product-backlog#1552 (`mass_assignable_model`)

Endpoint-independent detector — no route/controller is required for it to fire, but
`AccountController::store()` (`POST /api/accounts`, see `routes/api.php`) is included for realism.

- `Account` — `protected $guarded = [];`. Nothing guarded, every column mass-assignable.
- `Invoice` — `protected $guarded = ['id', 'created_at', 'updated_at'];`. Only keys/timestamps are
  guarded; every business attribute is still assignable.

```
xygeni-cli apisecurity --dir . --detectors mass_assignable_model
```
`Account` should be reported at severity High; `Invoice` at severity Low. `User` (which declares
`$fillable`) should not be reported by this detector — the allowlist wins in Laravel.
