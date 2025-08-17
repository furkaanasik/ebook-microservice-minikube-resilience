# 🚀 E-Book Platform - Mikroservis Projesi Özeti

> **Readme.md dosyası claude sonnet 4 ile oluşturulmuştur.**

## 🎯 Proje Amacı
Resilience patterns öğrenmek için minimal mikroservis mimarisi

## 🛠️ Teknoloji Stack
* **User Service**: Java Spring Boot (Port: 8080)
* **Book Service**: Go Gin (Port: 8081)
* **Order Service**: Python Flask (Port: 8082)
* **Orchestration**: Kubernetes (Minikube)
* **Service Mesh**: Istio + Envoy
* **Event Streaming**: Kafka
* **Observability**: Jaeger + Prometheus + Loki + Grafana

## 📊 Basit Veritabanı
* **users**: id, username, email, password
* **books**: id, title, author, price, category
* **orders**: id, user_id, book_id, order_date, status

## 🔄 İş Akışı
1. Kullanıcı kayıt → User Service
2. Kitapları gör → Book Service
3. Sipariş ver → Order Service (diğer servisleri çağırır)

## 🛡️ Test Edilecek Resilience Patterns
* **Book Service çökerse** → Fallback: "Kitaplar yüklenemiyor"
* **User Service yavaşsa** → Timeout: "Oturum açma zaman aşımı"
* **Order hatası** → Retry: 3 kez deneme
* **Circuit Breaker** → Istio otomatik

## 🎯 Learning Goals
* Mikroservis mimarisi
* Resilience patterns (Circuit Breaker, Timeout, Retry, Fallback)
* Service Mesh (Istio)
* Complete Observability (Traces, Metrics, Logs)
* Event-driven architecture
* Production-ready deployment

## 📋 Development Sırası

### **Phase 1: Foundation** 
* [x] **Minikube + Istio kurulumu**
* [ ] **Basic Observability stack** (Prometheus + Grafana)

### **Phase 2: Core Services**
* [x] **User Service (Java Spring Boot)**
* [x] **User Service Kubernetes deployment + Istio config**
* [ ] **Book Service (Go Gin)** 
* [ ] **Book Service Kubernetes deployment + Istio config**

### **Phase 3: Integration & Testing**
* [ ] **Order Service (Python Flask)**
* [ ] **Order Service Kubernetes deployment + Istio config**
* [ ] **Service-to-Service communication test**

### **Phase 4: Advanced Features**
* [ ] **Complete Observability** (Jaeger + Loki)
* [ ] **Kafka event streaming**
* [ ] **Resilience patterns test** (Circuit breaker, timeout, retry)
