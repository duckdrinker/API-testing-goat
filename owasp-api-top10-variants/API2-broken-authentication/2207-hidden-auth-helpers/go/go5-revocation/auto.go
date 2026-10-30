package main

import "net/http"

func autoLogin(w http.ResponseWriter, r *http.Request) {
	session, _ := store.Get(r, "s")
	delete(session.Values, "user")
	session.Options.MaxAge = -1
	session.Save(r, w)
}
