package main

import "github.com/gin-gonic/gin"

func main() {
	r := gin.Default()
	r.GET("/users", ListUsers)
	r.POST("/users", func(c *gin.Context) {
		c.JSON(201, gin.H{"ok": true})
	})
	r.Run()
}
