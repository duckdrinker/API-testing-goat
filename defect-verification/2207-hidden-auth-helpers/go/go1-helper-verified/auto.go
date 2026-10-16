package main

import (
	"net/http"

	"github.com/golang-jwt/jwt/v5"
)

func keyFunc(t *jwt.Token) (interface{}, error) { return []byte("secret"), nil }

func verifyToken(tok string) (*jwt.RegisteredClaims, error) {
	claims := &jwt.RegisteredClaims{}
	_, err := jwt.ParseWithClaims(tok, claims, keyFunc)
	return claims, err
}

func autoLogin(w http.ResponseWriter, r *http.Request) {
	claims, err := verifyToken(r.Header.Get("X-Token"))
	if err != nil {
		http.Error(w, "denied", http.StatusUnauthorized)
		return
	}
	session, _ := store.Get(r, "s")
	session.Values["user"] = claims.Subject
	session.Save(r, w)
}
