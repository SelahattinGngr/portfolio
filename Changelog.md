# Changelog

All notable changes to this project will be documented in this file.

---

### [v0.5.0] - 07-12-2025

**Backend Completion: 2FA Architecture, Async Queues & Security Hardening**

- **Özellik (Security):** **Two-Factor Authentication (2FA)** altyapısı tamamlandı. `Google Authenticator` (TOTP) ve `Email OTP` stratejileri dinamik hale getirildi.
- **Mimari (Auth Flow):** Login akışı monolit yapıdan çıkarılıp state-based bir yapıya evrildi; `/signin` (kimlik doğrulama) ve `/verify-2fa` (doğrulama) olarak iki aşamaya bölündü.
- **Mimari (Async):** E-posta gönderimi için **Redis List** tabanlı özel bir **Asenkron İş Kuyruğu (Task Queue)** mimarisi geliştirildi. Producer/Consumer pattern uygulanarak ana thread bloklanmadan mail gönderimi sağlandı.
- **İyileştirme (Redis):** Redis serializasyon stratejisi optimize edildi; Basit anahtarlar (Token, OTP) için `StringRedisTemplate`, kompleks objeler (Email DTO) için `GenericJackson2JsonRedisSerializer` ayrılarak `ClassCastException` riskleri ortadan kaldırıldı.
- **Güvenlik:** **Refresh Token Rotation** mekanizması devreye alındı. Token yenileme işleminde eski token ve device-id Redis'ten anında silinerek "Replay Attack" ve "Zombie Token" riskleri sıfırlandı.
- **Özellik (Analytics):** Site trafiğini ve proje görüntülenme sayılarını takip eden **Analytics Modülü** (`VisitLog` entity, `ViewCount` logic) entegre edildi.
- **Özellik (Contact):** Ziyaretçi mesajlarını veritabanında kalıcı hale getiren **Contact Modülü** eklendi.
- **Refactor (Clean Code):** Tüm entity'ler için `BaseModel` soyutlaması yapılarak `createdAt`/`updatedAt` gibi audit alanları ve kod tekrarları (DRY) merkezi bir yapıya alındı.
- **Veritabanı:** Flyway migration geçmişi konsolide edildi; Analytics, Contact ve 2FA şemaları `V3` sürümünde birleştirildi.
- **Hata Yönetimi:** `GlobalExceptionHandler` sertleştirildi. `MissingRequestCookieException` gibi istemci kaynaklı hatalar düzgünce yakalanırken, 500 hatalarında iç detayların (Information Disclosure) dışarı sızması engellendi.
- **DevOps:** Docker Compose ortam değişkenleri ve SMTP konfigürasyonları production-ready hale getirildi.
- **Refactor (Architecture):** Proje paket yapısı **Domain-Driven Design (DDD)** prensiplerine yaklaştırıldı; Servis katmanı `domain` (İş Mantığı) ve `infra` (Altyapı) olarak ayrıştırıldı, konfigürasyon sınıfları `config` paketinde toplandı.
- **İyileştirme (Observability):** `JwtAuthFilter` mantığı güncellendi; `/actuator/**` uç noktaları filtre denetiminden çıkarılarak health-check işlemlerindeki log kirliliği ve performans kaybı önlendi.
- **Özellik (2FA Setup):** Google Authenticator kurulumu için QR Kod üretim endpoint'i (`/setup-google-2fa`) ve iş mantığı eklendi.

---

### [v0.4.0] - 02-11-2025

**Refactor: Token abstraction, DI hardening, cookie/token services & operational polish**

- **Ekleme:** `SecurityConfig` güncellendi; 'CorsConfigurationSource' bean’i eklendi, CORS politikaları merkezi hale getirildi. Rol bazlı erişim kontrolleri `hasRole` ile netleştirildi.
- **Ekleme:** `TokenProvider` arayüzü tanımlandı; token üretimi/doğrulama soyutlandı (JWT implementasyonu `JwtService` ile sınırlandırıldı).
- **Ekleme:** `JwtService` yeniden yapılandırıldı; access ve refresh token’lar için ayrı `SecretKey` yönetimi, `role` claim extraction, expiration ve validation yapısı merkezi hale getirildi.
- **Ekleme:** `TokenProvider` arayüzü entegre edilerek JWT servisinin soyut katmanı tanımlandı.
- **Ekleme:** `UserDetailsImpl` sınıfı ile kullanıcı kimliği ve rol bilgisinin JWT akışına doğrudan entegrasyonu sağlandı.
- **Değişiklik:** `AuthService` artık doğrudan `JwtService` yerine `TokenProvider` bağımlılığına sahip — bağımlılık inversiyonu (DIP) uygulandı.
- **İyileştirme:** Auth akışı parçalandı; `CookieFactory`, `CookieService`, `TokenService`, `TokenStoreService` gibi tek sorumluluklu bileşenlere ayrıldı (SRP).
- **Güvenlik:** Cookie üretimi ve yönetimi merkezi `CookieService`/`CookieUtil` ile standardize edildi (HttpOnly, Secure, SameSite politikaları vurgulandı).
- **İyileştirme:** Redis tabanlı token store soyutlandı (`TokenStoreService` → `TokenService`), token revocation ve device-id takibi için hazır pattern eklendi.

---

### [v0.3.0] - 25-10-2025

**Security, Stability, and Infrastructure Enhancements**

- **Ekleme:** Global `ExceptionHandler` ve `ApiResponse` yapısı entegre edilerek tüm API yanıtları kurumsal formatta standart hale getirildi.
- **Ekleme:** PostgreSQL bağlantısı sağlandı ve JPA + Flyway migration yapısı kuruldu.
- **Ekleme:** Domain tabanlı hata sınıfları (`BusinessException`, `ValidationException`, `EntityNotFoundException`) oluşturuldu.
- **Ekleme:** TraceId tabanlı hata izleme sistemi eklendi (her hata log’u benzersiz kimlikle takip edilebilir).
- **Ekleme:** Dockerfile üretim ortamına hazır hale getirildi; `Docker Compose` dosyası PostgreSQL, Redis ve uygulama servisleriyle entegre edildi.
- **Ekleme:** `CookieUtil` sınıfı ile `HttpOnly` + `Secure` flag’li cookie yönetimi uygulandı.
- **İyileştirme:** `TokenStoreService` yeniden yapılandırıldı, Redis üzerinde token saklama ve geçerlilik kontrolü optimize edildi.
- **Güvenlik:** Access ve refresh token yönetimi tamamen `HttpOnly cookie` tabanına taşındı.

---

### [v0.2.0] - 20-10-2025

**JWT Authentication, Redis, and Auth Service Integration**

- **Ekleme:** `AuthService` ile kullanıcı giriş, çıkış ve token yenileme akışları tamamlandı.
- **Ekleme:** `JwtService` ile access/refresh token üretimi, doğrulaması ve kullanıcı adı çıkarımı eklendi.
- **Ekleme:** `RedisTemplate` tabanlı token saklama altyapısı oluşturuldu (`TokenStoreService`).
- **Ekleme:** `JwtAuthFilter` ve `SecurityConfig` düzenlenerek istek bazlı JWT doğrulama aktif hale getirildi.
- **Ekleme:** `AuthController` ile `/signin`, `/signout`, `/refresh` uç noktaları tanımlandı.
- **Ekleme:** `CookieDto` ve `CookieUtil` yapıları ile cihaz bazlı token yönetimi sağlandı.
- **İyileştirme:** Spring Boot Security yapılandırması sadeleştirildi, gereksiz filter ayrıştırıldı.

---

### [v0.1.0] - 13-10-2025

**Initial Core Setup**

- **Ekleme:** Spring Boot projesi temel konfigürasyonlarla oluşturuldu.
- **Ekleme:** Redis entegrasyonu hazırlandı.
- **Ekleme:** Swagger/OpenAPI ile temel API dokümantasyonu oluşturuldu.
- **Ekleme:** H2 veritabanı geliştirme ortamında aktif hale getirildi.
- **Ekleme:** Temel `Admin` kimlik doğrulama mekanizması eklendi.

---
