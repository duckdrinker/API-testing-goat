package main

import (
	"github.com/gin-gonic/gin"
)

func main() {
	r := gin.Default()
	r.GET("/orders", listOrders)
	r.Run()
}

func listOrders(c *gin.Context) {
	var orders []Order
	sess := db.Model(&Order{})
	sess = sess.Where("owner_id = ?", c.Query("uid"))
	sess = sess.Order("id desc")
	sess = sess.Limit(50)
	sess.Find(&orders)
	c.JSON(200, orders)
}
