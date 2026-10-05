# Fixture #2112 - mass_assignment

Two copies: `no-descriptor/` (source only) and `with-descriptor/` (same + `openapi.json`).

| Id | Endpoint | Shape | Expected |
|---|---|---|---|
| V1 | POST /api/core/declared/ | Serializer declaring enabled/owner | report or not (record) |
| V2 | POST /api/core/blanket/{pk}/ | `for key in request.data: setattr(...)` | **report (ticket's expectation)** |
| V3/V4 | POST /api/core/model-bound/ | ModelViewSet via path() | report or not (record) |
| V5 | /api/core/data-sources/ | ModelViewSet via router | report or not (record) |
| NEG | /api/core/safe-sources/ | read_only_fields | no |
| NEG | POST /api/core/allowlist/{pk}/ | explicit allowlist | no |

#2108 says mass_assignment (apisec) went 15 -> 0 ("removed"): record whether the detector still exists.
