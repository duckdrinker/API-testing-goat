package main

import "github.com/gofiber/fiber/v2"

func ListUsers(c *fiber.Ctx) error {
	return c.JSON([]string{})
}
