package controller

import "net/http"

func GetPost(w http.ResponseWriter, r *http.Request)    { w.Write([]byte("{}")) }
func CreatePost(w http.ResponseWriter, r *http.Request) { w.Write([]byte("{}")) }
func Home(w http.ResponseWriter, r *http.Request)       { w.Write([]byte("home")) }
func Plain(w http.ResponseWriter, r *http.Request)      { w.Write([]byte("plain")) }
