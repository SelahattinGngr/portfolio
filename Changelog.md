# Changelog

All notable changes to this project will be documented in this file.

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
- **Test & Operasyon:** `TokenProvider` sayesinde unit testlerde kolayca `MockTokenProvider` kullanılabilecek; bunun için test scaffold önerildi.

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
