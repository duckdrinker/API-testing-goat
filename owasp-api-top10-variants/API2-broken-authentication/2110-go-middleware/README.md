# Fixture #2110 - Go route middleware as call expression

Expected `security` in the apisec inventory / absence of `unauthenticated_endpoint`:

| Case | Route | Expected security | unauthenticated_endpoint |
|---|---|---|---|
| CONTROL | GET /version | none | YES (legitimate) |
| CONTROL | GET /user/profile (`reqSignIn`) | group-auth-middleware | no |
| A | POST /admin/mirror/purge (`reqToken()`) | resolved | no |
| A2 | GET /admin/hooks (`tokenRequiresScopes(..), reqToken()`) | resolved | no |
| B | POST /repos/{owner}/{repo}/issues/{index}/delete (`reqRepoAdmin` per route) | resolved (#2210 says NOT yet) | no |
| B-ctl | GET /repos/{owner}/{repo}/issues | none | YES (legitimate) |
| C | GET/POST /projects/{id}/columns (helper param `writeChecks`) | resolved (ticket asks 1 level) | no |
