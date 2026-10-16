package main

import "net/http"

func subjectOf(tok string) string { return tok }

func autoLogin(w http.ResponseWriter, r *http.Request) {
	sub := subjectOf(r.Header.Get("X-Token"))
	session, _ := store.Get(r, "s")
	session.Values["user"] = sub
	session.Save(r, w)
}
