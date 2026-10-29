package main

import "net/http"

func main() {
	mux := http.NewServeMux()
	mux.HandleFunc("GET /users", ListUsers)
	mux.HandleFunc("POST /users", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(201)
	})
	http.ListenAndServe(":8080", mux)
}
