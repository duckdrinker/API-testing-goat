// Package web is a minimal stand-in for Gitea's modules/web router so the
// route/middleware idioms below match the real source shape.
package web

import "net/http"

type Router struct{ mux *http.ServeMux }

func NewRouter() *Router { return &Router{mux: http.NewServeMux()} }

func (r *Router) Group(pattern string, fn func(), middlewares ...any) {
	fn()
}

func (r *Router) Get(pattern string, h ...any)    { r.reg("GET", pattern, h) }
func (r *Router) Post(pattern string, h ...any)   { r.reg("POST", pattern, h) }
func (r *Router) Put(pattern string, h ...any)    { r.reg("PUT", pattern, h) }
func (r *Router) Delete(pattern string, h ...any) { r.reg("DELETE", pattern, h) }

func (r *Router) reg(method, pattern string, h []any) {}
