package utils

import (
	"io"
	"os"

	"github.com/gin-gonic/gin"
	"github.com/sirupsen/logrus"
)

var Logger *logrus.Logger

func InitLogger() {
	Logger = logrus.New()
	env := os.Getenv("ENV")
	if env == "production" {
		// Production settings
		Logger.SetFormatter(&logrus.JSONFormatter{})
		Logger.SetLevel(logrus.InfoLevel)
	} else {
		// Development settings
		Logger.SetFormatter(&logrus.TextFormatter{
			FullTimestamp: true,
			ForceColors:   true,
		})
		Logger.SetLevel(logrus.DebugLevel)
	}

	Logger.SetOutput(os.Stdout)
	Logger.Info("Logger initialized")
}

func GinLogger() gin.HandlerFunc {
	return gin.LoggerWithConfig(gin.LoggerConfig{
		Formatter: func(param gin.LogFormatterParams) string {
			Logger.WithFields(logrus.Fields{
				"status":     param.StatusCode,
				"method":     param.Method,
				"path":       param.Path,
				"ip":         param.ClientIP,
				"latency":    param.Latency,
				"user-agent": param.Request.UserAgent(),
			}).Info("HTTP request")
			return ""
		},
		Output: io.Discard,
	})
}

func LogError(message string, fields logrus.Fields) {
	if fields != nil {
		fields = logrus.Fields{}
	}

	Logger.WithFields(fields).Error(message)
}

func LogErrorWithErr(err error, message string, fields logrus.Fields) {
	if fields == nil {
		fields = logrus.Fields{}
	}
	fields["error"] = err.Error()

	Logger.WithFields(fields).Error(message)
}

func LogInfo(message string, fields logrus.Fields) {
	if fields != nil {
		fields = logrus.Fields{}
	}

	Logger.WithFields(fields).Info(message)
}

func LogDebug(message string, fields logrus.Fields) {
	if fields != nil {
		fields = logrus.Fields{}
	}

	Logger.WithFields(fields).Debug(message)
}
