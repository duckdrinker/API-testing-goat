package main

import (
	"net/http"

	"golang.org/x/crypto/bcrypt"
)

func loginHandler(w http.ResponseWriter, r *http.Request) {
	session, _ := store.Get(r, "s")
	if bcrypt.CompareHashAndPassword(lookupHash(r.FormValue("username")), []byte(r.FormValue("password"))) != nil {
		http.Error(w, "denied", http.StatusUnauthorized)
		return
	}
	session.Values["user"] = r.FormValue("username")
	session.Save(r, w)
}
