package main

import "net/http"

func autoLogin(w http.ResponseWriter, r *http.Request) {
	session, _ := store.Get(r, "s")
	session.Values["user"] = r.FormValue("userId")
	session.Save(r, w)
}
