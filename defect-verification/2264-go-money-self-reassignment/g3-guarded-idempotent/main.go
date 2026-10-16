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
	key := c.GetHeader("Idempotency-Key")
	if key == "" {
		c.Status(http.StatusBadRequest)
		return
	}
	var req topUp
	if err := c.ShouldBindJSON(&req); err != nil || req.Amount <= 0 || req.Amount > 1000 {
		c.Status(http.StatusBadRequest)
		return
	}
	var done int64
	db.Table("processed_requests").Where("key = ?", key).Count(&done)
	if done > 0 {
		c.Status(http.StatusOK)
		return
	}
	var acct Account
	db.First(&acct, c.Param("id"))
	acct.Balance += req.Amount
	db.Save(&acct)
	db.Exec("INSERT INTO processed_requests(key) VALUES (?)", key)
	c.Status(http.StatusOK)
}
