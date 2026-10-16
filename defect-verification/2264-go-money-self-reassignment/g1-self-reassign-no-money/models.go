package main

import "gorm.io/gorm"

type Account struct {
	ID      uint
	Balance float64
	Owner   uint
}

type Order struct {
	ID    uint
	Total float64
	Owner uint
}

var db *gorm.DB

