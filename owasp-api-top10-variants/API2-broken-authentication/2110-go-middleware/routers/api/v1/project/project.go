package project

import "net/http"

func ListColumns(w http.ResponseWriter, r *http.Request)  { w.Write([]byte("[]")) }
func CreateColumn(w http.ResponseWriter, r *http.Request) { w.Write([]byte("ok")) }
