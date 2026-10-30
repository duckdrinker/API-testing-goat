package main

import "github.com/gin-gonic/gin"

func ListUsers(c *gin.Context) {
	c.JSON(200, gin.H{"users": []string{}})
}
