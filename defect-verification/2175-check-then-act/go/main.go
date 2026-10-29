package main

import (
	"net/http"

	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
)

type Coupon struct {
	ID     uint
	Code   string
	Status string
}

type Redemption struct {
	ID   uint
	Code string
}

var db *gorm.DB

func main() {
	r := gin.Default()
	r.POST("/coupons/:id/redeem-unguarded", redeemUnguarded)
	r.POST("/coupons/:id/redeem-split", redeemSplit)
	r.POST("/coupons/:id/redeem-atomic", redeemAtomic)
	r.POST("/coupons/:id/delete-after-check", deleteAfterCheck)
	r.POST("/coupons/:id/already-done-write", alreadyDoneWrite)
	r.Run()
}

// c1: unguarded check -> write
func redeemUnguarded(c *gin.Context) {
	var coupon Coupon
	db.First(&coupon, c.Param("id"))
	if coupon.Status == "open" {
		coupon.Status = "used"
		db.Save(&coupon)
	}
	c.Status(http.StatusOK)
}

// c2: check in the handler, write in a helper
func markUsed(coupon *Coupon) {
	coupon.Status = "used"
	db.Save(coupon)
}

func redeemSplit(c *gin.Context) {
	var coupon Coupon
	db.First(&coupon, c.Param("id"))
	if coupon.Status == "open" {
		markUsed(&coupon)
	}
	c.Status(http.StatusOK)
}

// c3: atomic conditional UPDATE
func redeemAtomic(c *gin.Context) {
	var coupon Coupon
	db.First(&coupon, c.Param("id"))
	if coupon.Status == "open" {
		db.Exec("UPDATE coupons SET status = 'used' WHERE id = ? AND status = 'open'", coupon.ID)
	}
	c.Status(http.StatusOK)
}

// c4: delete of the row just checked
func deleteAfterCheck(c *gin.Context) {
	var coupon Coupon
	db.First(&coupon, c.Param("id"))
	if coupon.Status == "expired" {
		db.Delete(&coupon)
	}
	c.Status(http.StatusOK)
}

// c6: already-done write
func alreadyDoneWrite(c *gin.Context) {
	var coupon Coupon
	db.First(&coupon, c.Param("id"))
	if coupon.Status == "used" {
		coupon.Status = "used"
		db.Save(&coupon)
	}
	c.Status(http.StatusOK)
}
