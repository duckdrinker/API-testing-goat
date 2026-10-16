package main

import (
	"net/http"

	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
)

type Order struct {
	ID    uint
	Owner string
}

var db *gorm.DB

// #2214 - owner bound through a local variable (gin + gorm).
// NOT REPORTED cases must stay silent; REPORTED cases must fire
// go.user_controlled_primary_key.

// CTRL-1 REPORTED: unbounded lookup, baseline positive.
func ctrl1(c *gin.Context) {
	var order Order
	id, _ := c.GetQuery("id")
	db.First(&order, id)
	c.JSON(http.StatusOK, order)
}

// CTRL-2 NOT REPORTED: caller written inline in the bound.
func ctrl2(c *gin.Context) {
	var order Order
	id, _ := c.GetQuery("id")
	db.Where("owner = ?", c.GetString("user")).First(&order, id)
	c.JSON(http.StatusOK, order)
}

// A1 NOT REPORTED: bound in the lookup, caller held in a local.
func a1(c *gin.Context) {
	owner := c.GetString("user")
	var order Order
	id, _ := c.GetQuery("id")
	db.Where("owner = ?", owner).First(&order, id)
	c.JSON(http.StatusOK, order)
}

// A2 REPORTED: twin of A1, the local holds a request value.
func a2(c *gin.Context) {
	owner := c.Query("owner")
	var order Order
	id, _ := c.GetQuery("id")
	db.Where("owner = ?", owner).First(&order, id)
	c.JSON(http.StatusOK, order)
}

// B1 NOT REPORTED: comparison after the lookup, caller held in a local.
func b1(c *gin.Context) {
	owner := c.GetString("user")
	var order Order
	id, _ := c.GetQuery("id")
	db.First(&order, id)
	if order.Owner != owner {
		c.Status(http.StatusForbidden)
		return
	}
	c.JSON(http.StatusOK, order)
}

// B2 REPORTED: twin of B1, the local holds a request value.
func b2(c *gin.Context) {
	owner := c.Query("owner")
	var order Order
	id, _ := c.GetQuery("id")
	db.First(&order, id)
	if order.Owner != owner {
		c.Status(http.StatusForbidden)
		return
	}
	c.JSON(http.StatusOK, order)
}

// C1 REPORTED: local first takes the caller, then is rebound to a request value.
func c1(c *gin.Context) {
	owner := c.GetString("user")
	owner = c.Query("owner")
	var order Order
	id, _ := c.GetQuery("id")
	db.Where("owner = ?", owner).First(&order, id)
	c.JSON(http.StatusOK, order)
}

// C2 REPORTED: rebound local, comparison after the lookup.
func c2(c *gin.Context) {
	owner := c.GetString("user")
	owner = c.Query("owner")
	var order Order
	id, _ := c.GetQuery("id")
	db.First(&order, id)
	if order.Owner != owner {
		c.Status(http.StatusForbidden)
		return
	}
	c.JSON(http.StatusOK, order)
}

// D1 REPORTED: loop variable, not assigned once from the caller.
func d1(c *gin.Context) {
	var order Order
	id, _ := c.GetQuery("id")
	for _, owner := range c.QueryArray("owners") {
		db.Where("owner = ?", owner).First(&order, id)
	}
	c.JSON(http.StatusOK, order)
}

func main() {
	r := gin.Default()
	r.GET("/ctrl1", ctrl1)
	r.GET("/ctrl2", ctrl2)
	r.GET("/a1", a1)
	r.GET("/a2", a2)
	r.GET("/b1", b1)
	r.GET("/b2", b2)
	r.GET("/c1", c1)
	r.GET("/c2", c2)
	r.GET("/d1", d1)
	r.Run()
}
