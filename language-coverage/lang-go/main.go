package main

import (
	"encoding/json"
	"net/http"

	"github.com/golang-jwt/jwt/v5"
	"github.com/gorilla/handlers"
	"github.com/gorilla/mux"
)

// UserProfileUpdateRequest — ASEC-096 (mass_assignment): "Role" binds
// straight through to the persisted user, which a client should never be
// able to set directly.
type UserProfileUpdateRequest struct {
	Name  string `json:"name"`
	Email string `json:"email"`
	Role  string `json:"role"`
}

func updateProfile(w http.ResponseWriter, r *http.Request) {
	var update UserProfileUpdateRequest
	_ = json.NewDecoder(r.Body).Decode(&update)
	_ = json.NewEncoder(w).Encode(update)
}

// createFeedback — ASEC-103 (Go half of rate_limit_absence): authenticated,
// user-input-accepting endpoint; no tollbooth / golang.org/x/time/rate
// usage anywhere in this module.
func createFeedback(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusAccepted)
}

// verifyToken — ASEC-089 (jwt_misconfiguration): no explicit signing-method
// check inside the keyfunc before returning the secret.
func verifyToken(tokenString string) (*jwt.Token, error) {
	return jwt.Parse(tokenString, func(token *jwt.Token) (interface{}, error) {
		return []byte("secret"), nil
	})
}

func main() {
	r := mux.NewRouter()
	r.HandleFunc("/api/users/profile", updateProfile).Methods("PUT")
	r.HandleFunc("/api/feedback", createFeedback).Methods("POST")

	// ASEC-082 (cors_misconfiguration): wildcard origin combined with
	// credentials via gorilla/handlers.
	corsHandler := handlers.CORS(
		handlers.AllowedOrigins([]string{"*"}),
		handlers.AllowCredentials(),
	)

	_ = http.ListenAndServe(":8080", corsHandler(r))
}
