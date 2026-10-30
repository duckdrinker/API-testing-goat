package v1

import (
	"net/http"

	"code.gitea.io/gitea/modules/web"
	"code.gitea.io/gitea/routers/api/v1/hooks"
	"code.gitea.io/gitea/routers/api/v1/mirror"
	"code.gitea.io/gitea/routers/api/v1/misc"
	"code.gitea.io/gitea/routers/api/v1/project"
	"code.gitea.io/gitea/routers/api/v1/repo"
	"code.gitea.io/gitea/routers/api/v1/user"
)

func authed(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if r.Header.Get("Authorization") == "" {
			http.Error(w, "unauthorized", http.StatusUnauthorized)
			return
		}
		next.ServeHTTP(w, r)
	})
}

// bare identifier middleware (already resolved today)
func reqSignIn(next http.Handler) http.Handler { return authed(next) }

// call-expression middleware (#2110 case A)
func reqToken() func(http.Handler) http.Handler { return authed }

func tokenRequiresScopes(scope string) func(http.Handler) http.Handler { return authed }

func reqRepoAdmin(next http.Handler) http.Handler { return authed(next) }

func Routes() *web.Router {
	m := web.NewRouter()

	// CONTROL: no middleware at all -> legitimately unauthenticated.
	m.Get("/version", misc.Version)

	// CONTROL: bare identifier group middleware.
	m.Group("/user", func() {
		m.Get("/profile", user.Profile)
	}, reqSignIn)

	// CASE A: call-expression group middleware.
	m.Group("/admin/mirror", func() {
		m.Post("/purge", mirror.PurgeMirror)
	}, reqToken())

	// CASE A2: fuller idiom, two chained middlewares, one with args.
	m.Group("/admin/hooks", func() {
		m.Get("", hooks.ListHooks)
	}, tokenRequiresScopes("admin"), reqToken())

	// CASE B: per-route middleware passed as call argument (not a group).
	m.Group("/repos/{owner}/{repo}", func() {
		m.Get("/issues", repo.ListIssues)
		m.Post("/issues/{index}/delete", reqRepoAdmin, repo.DeleteIssue)
	})

	// CASE C: middleware passed through a helper's parameter.
	m.Group("/projects", func() {
		addProjectRoutes(m, reqToken())
	})

	return m
}

func addProjectRoutes(m *web.Router, writeChecks ...any) {
	m.Group("/{id}", func() {
		m.Get("/columns", project.ListColumns)
		m.Post("/columns", append(writeChecks, project.CreateColumn)...)
	})
}
