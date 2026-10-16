package main

import (
	"net/http"

	"fx/jwtutil"
)

func autoLogin(w http.ResponseWriter, r *http.Request) {
	claims, err := jwtutil.Decode(r.Header.Get("X-Token"))
	if err != nil {
		http.Error(w, "denied", http.StatusUnauthorized)
		return
	}
	session, _ := store.Get(r, "s")
	session.Values["user"] = claims.Subject
	session.Save(r, w)
}
