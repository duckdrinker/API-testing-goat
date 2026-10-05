package mirror

import "net/http"

func PurgeMirror(w http.ResponseWriter, r *http.Request) { w.Write([]byte("purged")) }
