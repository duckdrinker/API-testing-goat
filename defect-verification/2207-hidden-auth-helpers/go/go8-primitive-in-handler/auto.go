package main

import (
	"net/http"

	"github.com/golang-jwt/jwt/v5"
)

func keyFunc(t *jwt.Token) (interface{}, error) { return []byte("secret"), nil }

func autoLogin(w http.ResponseWriter, r *http.Request) {
	claims := &jwt.RegisteredClaims{}
	if _, err := jwt.ParseWithClaims(r.Header.Get("X-Token"), claims, keyFunc); err != nil {
		http.Error(w, "denied", http.StatusUnauthorized)
		return
	}
	session, _ := store.Get(r, "s")
	session.Values["user"] = claims.Subject
	session.Save(r, w)
}
