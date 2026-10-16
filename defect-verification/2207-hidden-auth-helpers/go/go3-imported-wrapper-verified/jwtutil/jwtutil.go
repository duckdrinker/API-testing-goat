package jwtutil

import "github.com/golang-jwt/jwt/v5"

func Decode(tok string) (*jwt.RegisteredClaims, error) {
	claims := &jwt.RegisteredClaims{}
	_, err := jwt.ParseWithClaims(tok, claims, func(t *jwt.Token) (interface{}, error) { return []byte("secret"), nil })
	return claims, err
}
