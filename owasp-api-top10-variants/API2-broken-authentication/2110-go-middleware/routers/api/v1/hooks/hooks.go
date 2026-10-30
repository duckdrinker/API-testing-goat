package hooks

import "net/http"

func ListHooks(w http.ResponseWriter, r *http.Request) { w.Write([]byte("[]")) }
