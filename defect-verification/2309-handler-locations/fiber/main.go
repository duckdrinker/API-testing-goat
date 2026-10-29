package main

import "github.com/gofiber/fiber/v2"

func main() {
	app := fiber.New()
	app.Get("/users", ListUsers)
	app.Post("/users", func(c *fiber.Ctx) error {
		return c.Status(201).JSON(fiber.Map{"ok": true})
	})
	app.Listen(":3000")
}
