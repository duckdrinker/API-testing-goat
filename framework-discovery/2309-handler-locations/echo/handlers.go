package main

import "github.com/labstack/echo/v4"

func ListUsers(c echo.Context) error {
	return c.JSON(200, []string{})
}
