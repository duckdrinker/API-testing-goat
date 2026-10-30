package main

import (
	"net/http"

	"github.com/gorilla/sessions"
)

var store = sessions.NewCookieStore([]byte("k"))

func lookupHash(u string) []byte { return nil }

func main() {
	http.HandleFunc("/login", loginHandler)
	http.HandleFunc("/auto-login", autoLogin)
	http.ListenAndServe(":8080", nil)
}
