package main

import "github.com/labstack/echo/v4"

func main() {
	e := echo.New()
	e.GET("/users", ListUsers)
	e.POST("/users", func(c echo.Context) error {
		return c.JSON(201, map[string]bool{"ok": true})
	})
	e.Start(":8080")
}
