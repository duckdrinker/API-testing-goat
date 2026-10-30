package repo

import "net/http"

func DeleteIssue(w http.ResponseWriter, r *http.Request) { w.Write([]byte("deleted")) }
func ListIssues(w http.ResponseWriter, r *http.Request)  { w.Write([]byte("[]")) }
