# 🚀 E-Book Platform - Mikroservis Projesi Özeti

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

## 🚀 Kurulum Sırası

```bash
# 1. Minikube
minikube start --memory=8192 --cpus=4

# 2. Istio
istioctl install --set values.defaultRevision=default -y
kubectl label namespace default istio-injection=enabled

# 3. Basic Observability
kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.19/samples/addons/prometheus.yaml
kubectl apply -f https://raw.githubusercontent.com/istio/istio/release-1.19/samples/addons/grafana.yaml

# 4. Kafka (Phase 4'te)
kubectl create namespace kafka
kubectl apply -f 'https://strimzi.io/install/latest?namespace=kafka' -n kafka
```

## 📊 Monitoring Access

```bash
# Grafana Dashboard
kubectl port-forward svc/grafana 3000:3000 -n istio-system

# Jaeger Tracing (Phase 4'te)
kubectl port-forward svc/jaeger 16686:16686 -n istio-system

# Kiali Service Mesh
istioctl dashboard kiali
```

## 🎯 Learning Goals
* Mikroservis mimarisi
* Resilience patterns (Circuit Breaker, Timeout, Retry, Fallback)
* Service Mesh (Istio)
* Complete Observability (Traces, Metrics, Logs)
* Event-driven architecture
* Production-ready deployment

## 📋 Güncellenmiş Development Sırası

### **Phase 1: Foundation** 
* [ ] **Minikube + Istio kurulumu**
* [ ] **Basic Observability stack** (Prometheus + Grafana)

### **Phase 2: Core Services**
* [ ] **User Service (Java Spring Boot)**
* [ ] **User Service Kubernetes deployment + Istio config**
* [ ] **Book Service (Go Gin)** 
* [ ] **Book Service Kubernetes deployment + Istio config**

### **Phase 3: Integration & Testing**
* [ ] **Order Service (Python Flask)**
* [ ] **Order Service Kubernetes deployment + Istio config**
* [ ] **Service-to-Service communication test**

### **Phase 4: Advanced Features**
* [ ] **Complete Observability** (Jaeger + Loki ekleme)
* [ ] **Kafka event streaming**
* [ ] **Resilience patterns test** (Circuit breaker, timeout, retry)

## 🔄 Neden Bu Sıra:
1. **Erken feedback**: Her servis yazıldıktan hemen sonra deploy edip test
2. **Incremental complexity**: Basit HTTP ile başlayıp async events'e geçiş  
3. **Debug kolaylığı**: Her adımda çalışan bir sistem
4. **Real-world approach**: Production'da da böyle adım adım

**Bu mimari Netflix, Uber gibi büyük şirketlerin kullandığı modern approach! 🌟**