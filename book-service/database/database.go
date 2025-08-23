package database

import (
	"book-service/models"
	"log"

	"gorm.io/driver/sqlite"
	"gorm.io/gorm"
)

var DB *gorm.DB

func Connect() {
	var err error
	DB, err = gorm.Open(sqlite.Open("book.db"), &gorm.Config{})
	if err != nil {
		panic("Failed to connect to database")
	}
	log.Println("Database connection successful!")

	err = DB.AutoMigrate(&models.Book{})
	if err != nil {
		log.Fatal("Migration error:", err)
	}

	// Seed initial data
	SeedInitialData()
}

func SeedInitialData() {
	// Database'de zaten veri var mı kontrol et
	var count int64
	DB.Model(&models.Book{}).Count(&count)
	
	if count > 0 {
		log.Println("Database already contains books, skipping seed data")
		return
	}

	log.Println("Seeding initial book data...")
	
	initialBooks := []models.Book{
		{
			Title:    "Go Programming Language",
			Author:   "Alan Donovan",
			Price:    45.99,
			Category: "Technology",
		},
		{
			Title:    "Clean Code",
			Author:   "Robert C. Martin",
			Price:    39.99,
			Category: "Programming",
		},
		{
			Title:    "Design Patterns",
			Author:   "Gang of Four",
			Price:    55.00,
			Category: "Software Engineering",
		},
		{
			Title:    "Microservices Patterns",
			Author:   "Chris Richardson",
			Price:    49.99,
			Category: "Architecture",
		},
		{
			Title:    "Docker Deep Dive",
			Author:   "Nigel Poulton",
			Price:    42.50,
			Category: "DevOps",
		},
	}

	for _, book := range initialBooks {
		if err := DB.Create(&book).Error; err != nil {
			log.Printf("Error seeding book '%s': %v", book.Title, err)
		} else {
			log.Printf("Seeded book: %s by %s", book.Title, book.Author)
		}
	}
	
	log.Println("Initial book data seeding completed")
}
