package main

import (
	"book-service/database"
	"book-service/routes"
	"book-service/utils"
	"os"

	_ "book-service/docs"

	"github.com/gin-gonic/gin"
)

// @title Book Service API
// @version 1.0
// @description A simple book management service

// @host localhost:8081
func main() {
	utils.InitLogger()

	database.Connect()

	if os.Getenv("ENV") == "production" {
		gin.SetMode(gin.ReleaseMode)
	}

	r := gin.New()
	r.Use(gin.Logger())
	r.Use(gin.Recovery())

	routes.SetupRoutes(r)

	port := os.Getenv("PORT")
	if port == "" {
		port = "8081"
	}

	utils.LogInfo("Book Service starting", map[string]interface{}{
		"port":    port,
		"version": "1.0.0",
		"mode":    gin.Mode(),
	})

	if err := r.Run(":" + port); err != nil {
		utils.LogErrorWithErr(err, "Server not starting", nil)
		os.Exit(1)
	}

}
