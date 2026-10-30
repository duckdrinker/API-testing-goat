package main

import (
	"encoding/json"
	"net/http"

	"github.com/gorilla/mux"
)

// UserProfileResponse - declared response type that exposes fields the caller never asked for.
// Expected: excessive_data_exposure (Ssn, PasswordHash) + pii_leak_in_response (Ssn, Email, Phone).
type UserProfileResponse struct {
	ID           string `json:"id"`
	Name         string `json:"name"`
	Email        string `json:"email"`
	Phone        string `json:"phone"`
	Ssn          string `json:"ssn"`
	PasswordHash string `json:"passwordHash"`
}

// UserSummaryResponse - negative: only operation-relevant fields.
type UserSummaryResponse struct {
	ID   string `json:"id"`
	Name string `json:"name"`
}

// authMiddleware - bearer-token gate; makes every route below it authenticated.
func authMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if r.Header.Get("Authorization") == "" {
			http.Error(w, "unauthorized", http.StatusUnauthorized)
			return
		}
		next.ServeHTTP(w, r)
	})
}

// getProfile - Case A (positive): authenticated, path param, declared struct response.
func getProfile(w http.ResponseWriter, r *http.Request) {
	id := mux.Vars(r)["id"]
	resp := UserProfileResponse{
		ID: id, Name: "Jane Doe", Email: "jane@example.com",
		Phone: "+34 600 000 000", Ssn: "123-45-6789", PasswordHash: "x",
	}
	_ = json.NewEncoder(w).Encode(resp)
}

// getSummary - Case B (negative): same shape, safe response type.
func getSummary(w http.ResponseWriter, r *http.Request) {
	id := mux.Vars(r)["id"]
	_ = json.NewEncoder(w).Encode(UserSummaryResponse{ID: id, Name: "Jane Doe"})
}

func main() {
	r := mux.NewRouter()

	api := r.PathPrefix("/api").Subrouter()
	api.Use(authMiddleware)
	api.HandleFunc("/users/{id}/profile", getProfile).Methods("GET")
	api.HandleFunc("/users/{id}/summary", getSummary).Methods("GET")

	_ = http.ListenAndServe(":8080", r)
}
