package main

import (
	"net/http"

	"github.com/golang-jwt/jwt/v5"
)

func keyFunc(t *jwt.Token) (interface{}, error) { return []byte("secret"), nil }

func readToken(r *http.Request) string { return r.Header.Get("X-Token") }

func autoLogin(w http.ResponseWriter, r *http.Request) {
	token := readToken(r)
	claims := &jwt.RegisteredClaims{}
	if _, err := jwt.ParseWithClaims(token, claims, keyFunc); err != nil {
		http.Error(w, "denied", http.StatusUnauthorized)
		return
	}
	session, _ := store.Get(r, "s")
	session.Values["user"] = claims.Subject
	session.Save(r, w)
}
