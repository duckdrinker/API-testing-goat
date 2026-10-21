# 2201 - BOLA variant discriminators

Fixtures for xygeni-product-backlog#2201 (API Security: add BOLA variant discriminators to the findings).
Scan the folder with `xygeni apisecurity -d <this folder> -f json -o out` and read the `properties` of each
`broken_object_level_authorization` finding (and of the `mass_assignment` one for case G).

## `java/` (Spring Boot, every endpoint is authenticated but none checks ownership, except case F)

| Case | Endpoint | What it checks |
|---|---|---|
| A | `GET /api/customers/{id}` | `response_sensitive_fields` lists `email`, `ssn`, `cardNumber` with their `sensitivity_tag` |
| B | `GET /api/profiles/{id}` | `response_sensitive_fields` absent or empty (response type has no sensitive fields) |
| C | `GET`/`PUT /api/orgs/{orgId}/projects/{projectId}` | `id_parameter_roles`: `orgId: scope`, `projectId: object`; `id_uses` names the lookup (GET) and the mutation (PUT) |
| D | `GET /api/sites/{siteId}/devices/{cid}` | same as C with names a name heuristic does not know: `siteId: scope`, `cid: object` |
| E | `GET`/`DELETE /api/accounts/{id}` | `id_parameter_roles`: `id: object`; `id_uses` names the lookup line and the delete line |
| F | `GET /api/my/accounts/{id}` | negative: the lookup is scoped to the caller (`findAccountByIdAndOwner`), no BOLA, no `id_uses` |
| G | `POST /api/orders` | `mass_assignment` finding with `owner_reference_fields` = `userId`, `ownerId` (a subset of the protected fields; `status` and `role` must not be in it) |
| I | `POST /api/orders/new` | same binding as G, but the body goes to `create(...)`: `mass_assignment` is not reported (it only recognises `update`, `save`, `insert` and `persist` as the write) |
| H | `POST /api/orders/safe` | negative: no owner-reference field, no `mass_assignment` finding |

## `openapi/`

`openapi.yaml` only (no code): the response types live in the descriptor schemas (`Customer` with sensitive fields,
`PublicProfile` without, `Project` under `/orgs/{orgId}/projects/{projectId}`). Used to check that
`response_sensitive_fields` and `id_parameter_roles` also work when the response type comes from a descriptor schema.