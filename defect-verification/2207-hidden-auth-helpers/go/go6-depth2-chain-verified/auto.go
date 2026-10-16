package main

import (
	"net/http"

	"github.com/golang-jwt/jwt/v5"
)

func keyFunc(t *jwt.Token) (interface{}, error) { return []byte("secret"), nil }

func parseToken(tok string) (*jwt.RegisteredClaims, error) {
	claims := &jwt.RegisteredClaims{}
	_, err := jwt.ParseWithClaims(tok, claims, keyFunc)
	return claims, err
}

func subjectFrom(tok string) (string, error) {
	claims, err := parseToken(tok)
	if err != nil {
		return "", err
	}
	return claims.Subject, nil
}

func autoLogin(w http.ResponseWriter, r *http.Request) {
	sub, err := subjectFrom(r.Header.Get("X-Token"))
	if err != nil {
		http.Error(w, "denied", http.StatusUnauthorized)
		return
	}
	session, _ := store.Get(r, "s")
	session.Values["user"] = sub
	session.Save(r, w)
}
