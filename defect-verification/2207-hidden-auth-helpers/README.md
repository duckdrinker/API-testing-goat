# Fixture #2207 - hidden_authentication_path through application helpers

Each case folder is an independent project, scanned with `xygeni scan --run=apisec --dir <case> --all-files ...`
(scanner 6.21.0). The rule is comparative: every case carries its own canonical login path, because without one the
census is withheld and only an `info` is emitted. Grant shapes that fire per language (calibrated empirically):

| Language | Login path (canonical) | Grant used |
|---|---|---|
| Python (Django) | `authenticate()` + `login()` | `user.is_staff = True` / `user.role = ...` |
| Java (Spring) | `AuthenticationManager.authenticate` + `SecurityContextHolder.setAuthentication` | `SecurityContextHolder...setAuthentication(...)` |
| Kotlin (Spring) | same as Java | same as Java |
| JS/TS (Express) | `passport.authenticate('local')` | `req.session.user = ...` |
| Go (net/http) | bcrypt compare + gorilla session | `session.Values["user"] = ...` |
| PHP (Laravel) | `Auth::attempt` | `Auth::login(...)` |
| Ruby (Rails + Devise) | `valid_password?` + `sign_in` | `sign_in(user)` |
| Swift (Vapor) | `Bcrypt.verify` + `req.auth.login` | `req.auth.login(user)` |
| C# (ASP.NET Core) | `SignInManager.PasswordSignInAsync` | no shape fired (see below) |

## Case matrix (same numbering in every language)

| # | Case | Expected |
|---|---|---|
| 0 | baseline, request data grants identity, nothing verifies | finding |
| 1 | helper in the same class/module verifies (catalogue primitive), then grant | silent |
| 2 | twin: helper does not verify | finding |
| 3 | imported wrapper verifies | silent |
| 4 | twin: wrapper only parses (verification off) | finding |
| 5 | revocation (clear / logout / `= false`) | silent |
| 6 | 2-call helper chain ending in the primitive | silent |
| 7 | verifying helper called inside the grant expression itself | silent (observed: see ER) |
| 8 | primitive called directly in the handler (control) | silent |

Folders: `python/p0..p7`, `java/j0..j10` (j8 = case 7 with 1 call, j9 = case 8, j10 = auth0 primitive control),
`kotlin/k0..k11`, `javascript/js0..js8`, `go/go0..go8`, `php/php0..php8`, `ruby/rb0..rb8`, `swift/sw0..sw9`
(`sw9` = unverified token read from content without a helper).

## Real projects (HEAD, cloned in cloned-repos)

saleor `dfa97c9` (2026-10-15), SecurityShepherd `cff3958` (2026-10-09), WebGoat v2026.4 `872d614`.
Expected on 6.21.0: saleor 0, SecurityShepherd 0, WebGoat 1 (`HijackSessionAuthenticationProvider.java:58`).

## C# (not verifiable)

Eight shapes were tried with the ASP.NET Core Identity login path (`SignInAsync` with a request-supplied user,
`HttpContext.User = ...`, `HttpContext.SignInAsync(principal)`, session `SetString`, `Response.Cookies.Append`,
`AddToRoleAsync`, `IsAdmin = true`, issuing a `JwtSecurityToken` with a role claim): the endpoints are discovered
but `hidden_authentication_path` never fires, so no C# case is kept.
