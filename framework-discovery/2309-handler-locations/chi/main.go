package main

import (
	"net/http"

	"github.com/go-chi/chi/v5"
)

func main() {
	r := chi.NewRouter()
	r.Get("/users", ListUsers)
	r.Post("/users", func(w http.ResponseWriter, req *http.Request) {
		w.WriteHeader(201)
	})
	http.ListenAndServe(":3000", r)
}
