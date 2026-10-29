package main

import "net/http"

func ListUsers(w http.ResponseWriter, r *http.Request) {
	w.Write([]byte("[]"))
}
