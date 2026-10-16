package main

import (
	"net/http"

	"github.com/golang-jwt/jwt/v5"
)

func keyFunc(t *jwt.Token) (interface{}, error) { return []byte("secret"), nil }

func mustSubject(tok string) string {
	claims := &jwt.RegisteredClaims{}
	if _, err := jwt.ParseWithClaims(tok, claims, keyFunc); err != nil {
		panic(err)
	}
	return claims.Subject
}

func autoLogin(w http.ResponseWriter, r *http.Request) {
	session, _ := store.Get(r, "s")
	session.Values["user"] = mustSubject(r.Header.Get("X-Token"))
	session.Save(r, w)
}
