package main

import (
	"net/http"

	"github.com/gin-gonic/gin"
)

type topUp struct {
	Amount float64 `json:"amount"`
}

func main() {
	r := gin.Default()
	r.POST("/accounts/:id/topup", topUpHandler)
	r.Run()
}

func topUpHandler(c *gin.Context) {
	var req topUp
	if err := c.ShouldBindJSON(&req); err != nil {
		c.Status(http.StatusBadRequest)
		return
	}
	var acct Account
	db.First(&acct, c.Param("id"))
	acct.Balance += req.Amount
	db.Save(&acct)
	c.Status(http.StatusOK)
}
