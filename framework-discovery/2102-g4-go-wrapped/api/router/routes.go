package router

import (
	"net/http"

	"crapi/community/api/controller"
	"crapi/community/api/middlewares"
	"github.com/gorilla/mux"
)

func Routes(db any) http.Handler {
	r := mux.NewRouter()

	// CONTROL: plain function reference.
	r.HandleFunc("/community/api/v2/community/plain", controller.Plain).Methods("GET")

	// CASE: handler wrapped in two middleware calls.
	r.HandleFunc("/community/api/v2/community/posts/recent",
		middlewares.SetMiddlewareJSON(
			middlewares.SetMiddlewareAuthentication(controller.GetPost, db))).Methods("GET", "OPTIONS")

	r.HandleFunc("/community/api/v2/community/posts",
		middlewares.SetMiddlewareJSON(
			middlewares.SetMiddlewareAuthentication(controller.CreatePost, db))).Methods("POST")

	// CASE: single wrapper, no auth.
	r.HandleFunc("/community/home", middlewares.SetMiddlewareJSON(controller.Home)).Methods("GET")

	// TWIN: wrapper call with no handler argument -> must not yield a handler.
	r.HandleFunc("/community/api/v2/community/empty", middlewares.Log(nil)).Methods("GET")
	return r
}
