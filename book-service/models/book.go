package models

import (
	"time"

	"gorm.io/gorm"
)

type Book struct {
	ID        uint           `json:"id" gorm:"primarykey"`
	Title     string         `json:"title" gorm:"not null" binding:"required"`
	Author    string         `json:"author" gorm:"not null" binding:"required"`
	Price     float64        `json:"price" gorm:"not null" binding:"required,gt=0"`
	Category  string         `json:"category"`
	CreatedAt time.Time      `json:"created_at"`
	UpdatedAt time.Time      `json:"updated_at"`
	DeletedAt gorm.DeletedAt `json:"-" gorm:"index"`
}

type BookResponse struct {
	ID        uint    `json:"id"`
	Title     string  `json:"title"`
	Author    string  `json:"author"`
	Price     float64 `json:"price"`
	Category  string  `json:"category"`
	CreatedAt string  `json:"created_at"`
}

type BookRequest struct {
	Title    string  `json:"title" binding:"required" example:"Go Programming Language"`
	Author   string  `json:"author" binding:"required" example:"Alan Donovan"`
	Price    float64 `json:"price" binding:"required,gt=0" example:"45.99"`
	Category string  `json:"category" example:"Technology"`
}

func (b *Book) ToResponse() BookResponse {
	return BookResponse{
		ID:        b.ID,
		Title:     b.Title,
		Author:    b.Author,
		Price:     b.Price,
		Category:  b.Category,
		CreatedAt: b.CreatedAt.Format("2006-01-02 15:04:05"),
	}
}
