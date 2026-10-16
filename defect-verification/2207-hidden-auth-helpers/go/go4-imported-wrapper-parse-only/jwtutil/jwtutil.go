package jwtutil

import "github.com/golang-jwt/jwt/v5"

func Decode(tok string) (*jwt.RegisteredClaims, error) {
	claims := &jwt.RegisteredClaims{}
	_, _, err := jwt.NewParser().ParseUnverified(tok, claims)
	return claims, err
}
