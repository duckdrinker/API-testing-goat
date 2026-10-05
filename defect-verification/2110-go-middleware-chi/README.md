# Fixture #2110 (chi variant)
Same cases as ../2110-go-middleware but on real go-chi so that discovery recognises the router.
Control GET /version and GET /repos/.../issues are legitimately unauthenticated.
A (reqToken() via g.Use) and B (r.With(reqRepoAdmin)) should carry security.
