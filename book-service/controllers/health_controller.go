package controllers

import (
	"book-service/database"
	"database/sql"

	"github.com/gin-gonic/gin"
	"github.com/heptiolabs/healthcheck"
)

var health healthcheck.Handler

func init() {
	health = healthcheck.NewHandler()

	health.AddLivenessCheck("database", func() error {
		sqlDB, err := database.DB.DB()
		if err != nil {
			return err
		}
		return sqlDB.Ping()
	})

	health.AddReadinessCheck("database", func() error {
		sqlDB, err := database.DB.DB()
		if err != nil {
			return err
		}

		stats := sqlDB.Stats()
		if stats.OpenConnections > 10 {
			return sql.ErrConnDone
		}
		return sqlDB.Ping()
	})
}

func HealthCheck(c *gin.Context) {
	health.LiveEndpoint(c.Writer, c.Request)
}

func ReadinessCheck(c *gin.Context) {
	health.ReadyEndpoint(c.Writer, c.Request)
}
