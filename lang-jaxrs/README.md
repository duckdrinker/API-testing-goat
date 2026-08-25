# Language coverage — Java (JAX-RS)

**Xygeni component:** API Discovery framework detector `jaxrs`

`pom.xml` declares `jakarta.ws.rs-api` (maps to framework `jaxrs` for autodiscovery).
`UserResource.java` imports the classic `javax.ws.rs.*` namespace; `BookResource.java` imports
the newer `jakarta.ws.rs.*` namespace, to confirm the detector's import gate accepts either.

## What this fixture demonstrates

- `UserResource.getUser()` (`GET /api/users/{id}`) — `@PathParam("id")` → PATH; `@QueryParam("expand")`
  with `@DefaultValue("false")` → QUERY, marked optional; `@HeaderParam("X-Request-Id")` → HEADER;
  `@Context SecurityContext` discarded as a parameter but generates a "programmatic" security
  requirement; `@RolesAllowed({"USER", "ADMIN"})` → framework discovery.
- `UserResource.searchUsers()` (`GET /api/users/search`) — `@QueryParam("q")` → QUERY;
  `@MatrixParam("sort")` → QUERY (deliberate mapping, not a MATRIX location); `@Context UriInfo`
  discarded entirely, never appears as a parameter; `@Encoded` → tag; `@PermitAll` → framework
  discovery.
- `UserResource.createUser()` (`POST /api/users`) — unannotated, non-primitive `UserPayload`
  parameter → implicit BODY, `name="body"`; method-level `@Consumes`/`@Produces` override the
  class-level ones; `@RolesAllowed({"ADMIN"})` → framework discovery.
- `UserResource.updateUser()` (`PUT /api/users/{id}`) — `@PathParam("id")` → PATH; `@BeanParam
  UserUpdateBean` → BODY; `@DenyAll` → framework discovery.
- `UserResource.deleteUser()` (`DELETE /api/users/{id}`) — `@PathParam("id")` → PATH;
  `@CookieParam("session")` → COOKIE; `@Deprecated` marks the endpoint deprecated;
  `@RolesAllowed({"ADMIN"})` → framework discovery.
- `UserResource.patchUserProfile()` (`PATCH /api/users/{id}/profile`) — `@PathParam("id")` → PATH;
  `@FormParam("nickname")` → BODY; method-level `@Consumes(APPLICATION_FORM_URLENCODED)` →
  framework discovery.
- `UserResource.headUser()` (`HEAD /api/users/{id}`) — `@PathParam("id")` → PATH → framework
  discovery.
- `UserResource.optionsUsers()` (`OPTIONS /api/users`) — no parameters → framework discovery.
- `BookResource.listBooks()` (`GET /api/books`) — `@RolesAllowed({"USER", "ADMIN"})`, pure
  `jakarta.ws.rs.*` imports → framework discovery.
- `BookResource.getBook()` (`GET /api/books/{id}`) — `@PathParam("id")` → PATH → framework
  discovery.
- `BookResource.createBook()` (`POST /api/books`) — unannotated, non-primitive `BookPayload`
  parameter → implicit BODY, `name="body"`; `@RolesAllowed({"ADMIN"})` → framework discovery.

## Expected scan result

```
xygeni apisecurity --dir . --discovery-only -f json
```
