// routes/routes.go
package routes

import (
	"book-service/controllers"

	"github.com/gin-gonic/gin"
	swaggerFiles "github.com/swaggo/files"
	ginSwagger "github.com/swaggo/gin-swagger"
)

func SetupRoutes(r *gin.Engine) {
	// Health check endpoints
	r.GET("/health", controllers.HealthCheck)
	r.GET("/ready", controllers.ReadinessCheck)

	r.GET("/swagger/*any", ginSwagger.WrapHandler(swaggerFiles.Handler))

	// API v1 group
	v1 := r.Group("/api/v1")
	{
		// Book endpoints
		books := v1.Group("/books")
		{
			books.GET("", controllers.GetBooks)          // GET /api/v1/books
			books.GET("/:id", controllers.GetBook)       // GET /api/v1/books/123
			books.POST("", controllers.CreateBook)       // POST /api/v1/books
			books.PUT("/:id", controllers.UpdateBook)    // PUT /api/v1/books/123
			books.DELETE("/:id", controllers.DeleteBook) // DELETE /api/v1/books/123
		}
	}
}
