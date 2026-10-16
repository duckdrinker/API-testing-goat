package main

import (
	"net/http"

	"github.com/gin-gonic/gin"
)

type charge struct {
	Amount   float64 `json:"amount"`
	Quantity float64 `json:"quantity"`
}

func main() {
	r := gin.Default()
	r.POST("/accounts/:id/charge", chargeHandler)
	r.Run()
}

func chargeHandler(c *gin.Context) {
	var req charge
	if err := c.ShouldBindJSON(&req); err != nil {
		c.Status(http.StatusBadRequest)
		return
	}
	amount := req.Amount
	amount = amount * req.Quantity
	var acct Account
	db.First(&acct, c.Param("id"))
	acct.Balance -= amount
	db.Save(&acct)
	c.Status(http.StatusOK)
}
