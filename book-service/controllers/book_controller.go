package controllers

import (
	"net/http"
	"strconv"

	"book-service/database"
	"book-service/models"

	"github.com/gin-gonic/gin"
)

// GetBooks godoc
// @Summary Get all books
// @Description Get list of all books
// @Tags books
// @Accept json
// @Produce json
// @Success 200 {array} models.BookResponse
// @Router /api/v1/books [get]
func GetBooks(c *gin.Context) {
	var books []models.Book

	result := database.DB.Find(&books)
	if result.Error != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"error": "Kitaplar getirilirken hata oluştu",
		})
		return
	}

	var response []models.BookResponse
	for _, book := range books {
		response = append(response, book.ToResponse())
	}

	c.JSON(http.StatusOK, response)
}

// GetBook godoc
// @Summary Get a book by ID
// @Description Get book details by ID
// @Tags books
// @Accept json
// @Produce json
// @Param id path int true "Book ID"
// @Success 200 {object} models.BookResponse
// @Router /api/v1/books/{id} [get]
func GetBook(c *gin.Context) {
	id := c.Param("id")

	var book models.Book
	result := database.DB.First(&book, id)

	if result.Error != nil {
		c.JSON(http.StatusNotFound, gin.H{
			"error": "Kitap bulunamadı",
		})
		return
	}

	c.JSON(http.StatusOK, book.ToResponse())
}

// CreateBook godoc
// @Summary Create a new book
// @Description Create a new book with the provided data
// @Tags books
// @Accept json
// @Produce json
// @Param book body models.BookRequest true "Book data"
// @Success 201 {object} models.BookResponse
// @Router /api/v1/books [post]
func CreateBook(c *gin.Context) {
	var request models.BookRequest

	if err := c.ShouldBindJSON(&request); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "Geçersiz veri formatı: " + err.Error(),
		})
		return
	}

	book := models.Book{
		Title:    request.Title,
		Author:   request.Author,
		Price:    request.Price,
		Category: request.Category,
	}

	result := database.DB.Create(&book)
	if result.Error != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"error": "Kitap oluşturulurken hata oluştu",
		})
		return
	}

	c.JSON(http.StatusCreated, book.ToResponse())
}

// UpdateBook godoc
// @Summary Update a book
// @Description Update book details by ID
// @Tags books
// @Accept json
// @Produce json
// @Param id path int true "Book ID"
// @Param book body models.BookRequest true "Updated book data"
// @Success 200 {object} models.BookResponse
// @Router /api/v1/books/{id} [put]
func UpdateBook(c *gin.Context) {
	id, err := strconv.ParseUint(c.Param("id"), 10, 32)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "Geçersiz ID formatı",
		})
		return
	}

	var book models.Book
	result := database.DB.First(&book, uint(id))

	if result.Error != nil {
		c.JSON(http.StatusNotFound, gin.H{
			"error": "Kitap bulunamadı",
		})
		return
	}

	var request models.BookRequest
	if err := c.ShouldBindJSON(&request); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "Geçersiz veri formatı: " + err.Error(),
		})
		return
	}

	// Kitap verilerini güncelle
	book.Title = request.Title
	book.Author = request.Author
	book.Price = request.Price
	book.Category = request.Category

	result = database.DB.Save(&book)
	if result.Error != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"error": "Kitap güncellenirken hata oluştu",
		})
		return
	}

	c.JSON(http.StatusOK, book.ToResponse())
}

// DeleteBook godoc
// @Summary Delete a book
// @Description Delete book by ID
// @Tags books
// @Accept json
// @Produce json
// @Param id path int true "Book ID"
// @Success 204
// @Router /api/v1/books/{id} [delete]
func DeleteBook(c *gin.Context) {
	id, err := strconv.ParseUint(c.Param("id"), 10, 32)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "Geçersiz ID formatı",
		})
		return
	}

	var book models.Book
	result := database.DB.First(&book, uint(id))

	if result.Error != nil {
		c.JSON(http.StatusNotFound, gin.H{
			"error": "Kitap bulunamadı",
		})
		return
	}

	result = database.DB.Delete(&book)
	if result.Error != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"error": "Kitap silinirken hata oluştu",
		})
		return
	}

	c.JSON(http.StatusNoContent, nil)
}
