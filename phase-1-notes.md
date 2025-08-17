# 📚 Phase 1 - Infrastructure Setup Notları

## 🎯 Temel Kavramlar - Tek Cümle Açıklamalar

### **Kubernetes Nedir?**
> **Kubernetes = Docker container'larını otomatik yöneten, ölçeklendiren ve izleyen orkestra sistemi.**

### **Minikube Nedir?**
> **Minikube = Öğrenme amaçlı tek bilgisayarda Kubernetes cluster'ı simüle eden tool.**

### **Istio Neden Kurduk?**
> **Istio = Mikroservisler arası trafiği yöneten, resilience patterns (circuit breaker, retry, timeout) sağlayan service mesh.**

## 🧬 Sidecar Pattern Nedir?

### **Sidecar Olmadan (Normal Pod)**
```
┌─────────────────┐
│      Pod        │
│  ┌───────────┐  │
│  │   nginx   │  │  ← Tek container
│  │ (Port 80) │  │
│  └───────────┘  │
└─────────────────┘
```

### **Sidecar İle (Istio Pod)**
```
┌─────────────────────────────┐
│           Pod               │
│  ┌───────────┐ ┌─────────┐  │
│  │   nginx   │ │  Envoy  │  │  ← İki container
│  │ (Port 80) │ │ (Proxy) │  │
│  └───────────┘ └─────────┘  │
│       │             │       │
│       └─── Network ──┘      │
└─────────────────────────────┘
```

### **Sidecar Pattern Avantajları**
- **Separation of Concerns**: Ana uygulama business logic'e odaklanır
- **Reusability**: Aynı proxy her serviste kullanılır
- **Zero Code Change**: Mevcut uygulamaya kod ekleme gereksiz
- **Centralized Management**: Tüm proxy'ler merkezi yönetilir

## 🏗️ Istio'nun 3 Ana Bileşeni

### 1. **Data Plane (Envoy Proxy)**
- **Ne yapar**: Her servise sidecar olarak eklenir
- **Görevi**: Tüm network trafiğini yakalar, izler, yönetir

### 2. **Control Plane (istiod)**  
- **Ne yapar**: Tüm Envoy proxy'leri merkezi yönetir
- **Görevi**: Policy'leri dağıtır, configuration yapar

### 3. **Observability Components**
- **Ne yapar**: Metrics, logs, traces toplar
- **Görevi**: Sistem durumunu izler, raporlar

## 🏷️ Namespace Kavramı

### **Namespace Nedir?**
> **Namespace = Aynı cluster içinde kaynakları gruplandıran sanal sınırlar.**

### **Neden Kullanıyoruz?**
- **İzolasyon**: Her proje kendi namespace'inde
- **Organizasyon**: Kaynaklar düzenli
- **Güvenlik**: Projeler birbirini etkilemez
- **Cleanup**: Namespace sil → tüm kaynaklar gider

### **Projeler Arası Geçiş**
```bash
# Yeni namespace oluştur
kubectl create namespace project-a
kubectl create namespace project-b

# Namespace'ler arası geçiş
kubectl config set-context --current --namespace=project-a  # project-a'ya geç
kubectl config set-context --current --namespace=project-b  # project-b'ye geç

# Hangi namespace'deyim?
kubectl config view --minify | grep namespace
```

## 🐳 Docker Registry (Image Kaynağı)

### **Nginx Image Nereden Geldi?**
```bash
--image=nginx
# ↓ Otomatik expand edilir
--image=docker.io/library/nginx:latest
```

### **Registry Hierarchysi**
1. **docker.io** = Docker Hub domain
2. **library** = Official images namespace  
3. **nginx** = Image name
4. **latest** = Tag (version)

### **Diğer Registry Örnekleri**
- **Google**: `gcr.io/project/image`
- **AWS**: `123456789.dkr.ecr.region.amazonaws.com/image`
- **Private**: `company.com/team/image`

## 📋 Phase 1 - Komut Özeti

### **1. Minikube Kurulumu**
```bash
# Minikube indir ve kur
curl -LO https://github.com/kubernetes/minikube/releases/latest/download/minikube-linux-amd64
sudo install minikube-linux-amd64 /usr/local/bin/minikube

# Cluster başlat
minikube start --driver=docker --memory=8192 --cpus=4

# Durum kontrol
minikube status
```

### **2. Istio Kurulumu**  
```bash
# Istio indir
curl -L https://istio.io/downloadIstio | sh -
cd istio-*

# Executable yap
chmod +x bin/istioctl

# Istio'yu cluster'a kur
./bin/istioctl install --set values.defaultRevision=default -y

# Version kontrol
./bin/istioctl version
```

### **3. Namespace Setup**
```bash
# Özel namespace oluştur
minikube kubectl -- create namespace ebook-microservice

# Default namespace'i değiştir
minikube kubectl -- config set-context --current --namespace=ebook-microservice

# Istio sidecar injection aktif et
minikube kubectl -- label namespace ebook-microservice istio-injection=enabled

# Label kontrol
minikube kubectl -- get namespace ebook-microservice --show-labels
```

### **4. Test Deployment (Nginx)**
```bash
# Nginx deployment oluştur
minikube kubectl -- create deployment nginx-test --image=nginx

# Pod durumu kontrol (2/2 Ready beklenir)
minikube kubectl -- get pods

# Service expose et
minikube kubectl -- expose deployment nginx-test --type=NodePort --port=80

# Browser'da aç
minikube service nginx-test -n ebook-microservice

# Test cleanup
minikube kubectl -- delete deployment nginx-test
minikube kubectl -- delete service nginx-test
```

## 🔍 Komut Açıklamaları

### **Deployment Komutları**
```bash
# create deployment = Pod'ları yöneten üst seviye controller oluştur
kubectl create deployment <name> --image=<image>

# expose = Deployment'ı dış erişime aç
kubectl expose deployment <name> --type=NodePort --port=<port>

# delete = Kubernetes objesini sil
kubectl delete <resource-type> <name>
```

### **Pod Lifecycle**
```bash
# get pods = Çalışan container'ları listele
kubectl get pods

# describe = Detaylı bilgi (events, logs, config)
kubectl describe pod <pod-name>

# logs = Container loglarını gör
kubectl logs <pod-name>
```

### **Service Types**
- **ClusterIP**: Sadece cluster içi erişim
- **NodePort**: Node'un port'undan dış erişim
- **LoadBalancer**: Cloud load balancer (minikube'da simüle)

## ✅ Phase 1 Başarı Kriterleri

### **Infrastructure Ready**
- ✅ Minikube cluster Running
- ✅ Istio control plane Running  
- ✅ istiod pod 1/1 Ready
- ✅ Istio ingress gateway Running

### **Namespace Ready**
- ✅ ebook-microservice namespace created
- ✅ istio-injection=enabled label set
- ✅ Current context switched

### **Sidecar Injection Working**  
- ✅ Test pod shows 2/2 Ready (nginx + envoy)
- ✅ Service mesh networking functional
- ✅ Nginx welcome page accessible

## 🎯 Phase 1 Özeti

**Ne Kuruldu:**
1. **Kubernetes cluster** (Minikube)
2. **Service mesh** (Istio)  
3. **Dedicated namespace** (ebook-microservice)
4. **Sidecar injection** (Otomatik Envoy proxy)

**Ne Test Edildi:**
1. **Container deployment** (nginx)
2. **Service networking** (NodePort expose)
3. **Istio integration** (2 containers per pod)
4. **Browser connectivity** (Welcome page)

**Sonuç:**
- ✅ **Infrastructure hazır** mikroservisler için
- ✅ **Resilience patterns** otomatik aktif
- ✅ **Service mesh** çalışıyor
- ✅ **Clean namespace** production servislere hazır

---

**Sonraki Adım:** Phase 2 - User Service (Java Spring Boot) development! 🚀