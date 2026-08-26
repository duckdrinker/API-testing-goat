# Language coverage — C# / ASP.NET Core

**Xygeni detectors:** `excessive_data_exposure`, `pii_leak_in_response`, `broken_object_level_authorization` (Phase B), `broken_function_level_authorization` (Phase B), `cors_misconfiguration`, `jwt_misconfiguration`, `mass_assignment`
**Reference test cases:** ASEC-054/055, ASEC-062/063, ASEC-072, ASEC-077, ASEC-081, ASEC-086, ASEC-093

Unlike the other fixtures (one OWASP category each), this project exists purely to give the
Scanner TP a **C# example for every detector whose Java example already exists** but whose
per-language matrix cell was still empty.

## What this fixture demonstrates

- `UserController.GetProfile()` (`GET /api/users/profile`) — `Ssn` unreferenced by the request,
  sensitivity-tagged → `excessive_data_exposure` (054) + `pii_leak_in_response` (062).
- `UserController.GetSummary()` (`GET /api/users/{id}/summary`, negative) — only operation-relevant
  fields → no finding (055, 063).
- `UserController.UpdateProfile()` (`PUT /api/users/profile`) — `Role` binds straight through →
  `mass_assignment` (093).
- `UserController.SafeUpdate()` (negative) — no bindable protected field → no finding.
- `ProductController.GetProduct()` (`GET /api/products/{id}`) — BOLA Phase B, check absent (072).
- `ProductController.GetOwnProduct()` (`GET /api/products/safe/{id}`) — `[Authorize(Policy="OwnerOrAdmin")]`
  present → Phase B clears the candidate (072, "present" branch).
- `ProductController.GetManagerScopedProduct()` (`GET /api/products/manager/{id}`) —
  `[Authorize(Roles="Manager")]` present, but `manager` was removed from the shipped
  `privilegedRoles` default on `broken_object_level_authorization.yml`
  (xygeni-product-backlog#1400) → Phase B should now confirm the candidate instead of clearing it.
- `ProductController.GetRoleScopedProduct()` (`GET /api/products/role/{id}`) —
  `[Authorize(Roles = "User")]` is a bare role check, not an ownership check →
  xygeni-product-backlog#1551, BOLA should still be confirmed here.
- `AdminController.LockUser()` (`POST /api/admin/users/{id}/lock`) — BFLA Phase B, check absent (077).
- `AdminController.UnlockUser()` — `[Authorize(Roles="Admin")]` present → cleared (077, "present" branch).
- `Program.cs` — `AddCors(o => o.AddDefaultPolicy(b => b.AllowAnyOrigin().AllowCredentials()))` →
  `cors_misconfiguration` (081).
- `JwtService.Verify()` — `ValidateIssuerSigningKey = false` + no-op `SignatureValidator` →
  `jwt_misconfiguration` (086), severity Critical.

## Expected scan result

```
xygeni-cli apisecurity --dir . --detectors excessive_data_exposure,pii_leak_in_response,broken_object_level_authorization,broken_function_level_authorization,cors_misconfiguration,jwt_misconfiguration,mass_assignment
```

One finding per positive case listed above — including `GetManagerScopedProduct` (post-#1400) and
`GetRoleScopedProduct` (#1551), which both declare a role check but should still confirm as BOLA —
plus the two negative endpoints (`GetSummary`, `SafeUpdate`) and the two "check present" endpoints
that should stay cleared (`GetOwnProduct`, `UnlockUser`) should not appear.
