package main

import "net/http"

func readToken(r *http.Request) string { return r.Header.Get("X-Token") }

func autoLogin(w http.ResponseWriter, r *http.Request) {
	token := readToken(r)
	session, _ := store.Get(r, "s")
	session.Values["user"] = token
	session.Save(r, w)
}
