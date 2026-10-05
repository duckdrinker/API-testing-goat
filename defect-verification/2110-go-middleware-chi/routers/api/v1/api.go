package v1

import (
	"net/http"

	"github.com/go-chi/chi/v5"
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

func reqSignIn(next http.Handler) http.Handler       { return authed(next) }
func reqToken() func(http.Handler) http.Handler       { return authed }
func reqRepoAdmin(next http.Handler) http.Handler     { return authed(next) }

func version(w http.ResponseWriter, r *http.Request)      { w.Write([]byte("1.0")) }
func profile(w http.ResponseWriter, r *http.Request)      { w.Write([]byte("profile")) }
func purgeMirror(w http.ResponseWriter, r *http.Request)  { w.Write([]byte("purged")) }
func listIssues(w http.ResponseWriter, r *http.Request)   { w.Write([]byte("[]")) }
func deleteIssue(w http.ResponseWriter, r *http.Request)  { w.Write([]byte("deleted")) }

func Routes() http.Handler {
	r := chi.NewRouter()

	// CONTROL: no middleware.
	r.Get("/version", version)

	// CONTROL: bare identifier middleware.
	r.Group(func(g chi.Router) {
		g.Use(reqSignIn)
		g.Get("/user/profile", profile)
	})

	// CASE A: call-expression middleware.
	r.Group(func(g chi.Router) {
		g.Use(reqToken())
		g.Post("/admin/mirror/purge", purgeMirror)
	})

	// CASE B: per-route middleware.
	r.With(reqRepoAdmin).Post("/repos/{owner}/{repo}/issues/{index}/delete", deleteIssue)
	r.Get("/repos/{owner}/{repo}/issues", listIssues)

	return r
}
