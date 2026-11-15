# Product Platform

- Manage data product
- User authentication: register dan login
- Otomatisasi product imports dari DummyJSON menggunakan scheduler

## 1. Project Architecture

| Service | Port | Responsibility |
|---|---:|---|
| Eureka Server | 8761 | Service discovery and registration |
| API Gateway | 8084 | API routing and JWT authentication |
| Auth Service | 8081 | User registration, login, and JWT generation |
| Product Service | 8082 | Product management and database operations |
| Scheduler Service | 8083 | Get smartphone products from DummyJSON and sending them to Product Service |

### Communication Flow

- Clients mengakses endpoint melalui API Gateway.
- API Gateway validasi JWT tokens sebelum mengeksekusi request yang protected.
- Auth Service handle endpoint untuk registrasi and login user.
- Product Service handle CRUD product, search dengan kata kunci, dan import product dari DummyJSON.
- Scheduler Service mengambil products dari DummyJSON menggunakan Feign Client and mengirim data tersebut ke Product Service.
- Eureka Server mengelola service yang terpisah sehingga dapat saling berkomunikasi.

## 2. Kebutuhan Instalasi

- JDK 21
- Maven
- PostgreSQL
- Postman (for API testing)

## 3. Environment Configuration

Sebelum run aplikasi, configure database connections dan environment variables di setiap service `application.yaml`.

Required databases:

- `auth_platform` – untuk Auth Service.
- `product_platform` – untuk Product Service.

Configure JWT secret sebagai environment variable:

**Windows PowerShell:**

```powershell
$env:JWT_SECRET="your-secret-key"
```

Gunakan JWT secret yang sama untuk konfigurasi di Auth Service dan API Gateway.

## 4. How to Run

Run the services dengan urutan sebagai berikut.

### Step 1 – Start Infrastructure

Start PostgreSQL

Pastikan database `auth_platform` dan `product_platform` dapat diakses.

### Step 2 – Start Eureka Server

Run `EurekaServerApplication` main class dari `eureka-server` modul.

Eureka Dashboard:

```text
http://localhost:8761
```

### Step 3 – Start Auth Service

Run `AuthServiceApplication` main class dari `auth_service` module.

Port: `8081`

### Step 4 – Start Product Service

Run `ProductServiceApplication` main class dari `product_service` module.

Port: `8082`

### Step 5 – Start Scheduler Service

Run `SchedulerServiceApplication` main class dari `scheduler_service` module.

Port: `8083`

Scheduler Service akan:
- Import smartphone products langsung setelah aplikasi berjalan.
- Execute import yang sudah dijadwalkan setiap 2 menit.
- Mengambil maksimum 5 product per execution.
- Mengirim product ke Product Service dengan Feign Client.

### Step 6 – Start API Gateway

Run `ApiGatewayApplication` main class dari `api-gateway` module.

Port: `8084`

Setelah semua service berjalan, cek Eureka Dashboard dan pastikan bahwa semua service sudah terdaftar dengan tanda UP.

## 5. Maven Commands

Untuk build individual service, buka direktori per modulnya dan run:

```powershell
mvn clean package
```

Untuk menjalankan individual service dengan Maven, run:

```powershell
mvn spring-boot:run
```

Atau, run main application class langsung dari IntelliJ IDEA.

## 6. API Access

API Gateway

```text
http://localhost:8084
```

Endpoints:

| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/register` | Register a new user |
| POST | `/auth/login` | Authenticate and obtain a JWT |
| POST | `/scheduler/import` | 🔒 Manually trigger product import |
| POST | `/product` | 🔒 Create a product |
| GET | `/product?huruf=a` | 🔒 Search products by letter |
| GET | `/product/{id}` | 🔒 Get product details |
| PUT | `/product/{id}` | 🔒 Update a product |
| DELETE | `/product/{id}` | 🔒 Delete a product |

🔒 Protected endpoints butuh:

```http
Authorization: Bearer <JWT_TOKEN>
```

Internal product import endpoint is:

```text
POST /internal/products/import
```

Untuk internal scheduler-service ke product-service. Tidak untuk exposed ke API Gateway: 404 Not Found.

## 7. Decision Notes (Catatan Keputusan)

### 1. Not Using `context-path`

Setiap service saat ini hanya memiliki satu Controller yang akan dihit melalui client dengan API Gateway. Setiap prefix dari `@RequestMapping` di dalam Controller sudah cukup untuk mendaftarkan path service tersebut ke API Gateway.
Prefix dari `@RequestMapping` juga cukup bagi API Gateway untuk mengidentifikasi dan meneruskan request ke endpoint yang sesuai.
Contohnya request dengan `/auth/**` di-route ke Auth Service, sementara `/products/**` request di-route ke Product Service. Meskipun tanpa `context-path` prefix endpoint sudah cukup jelas dan menjaga simplifikasi API URL.

### 2. Using 3600000 Expired Time

Menurut saya, 1 jam menjadi waktu yang cukup seimbang untuk memenuhi keamanan dan kenyamanan pengguna. Dalam pertimbangan security, expired time token yang sebentar lebih aman terhadap resiko pencurian token. Namun, user juga perlu login ulang ketika token kadarluarsa.
Sistem Product Platform memiliki operasi protected untuk CRUD product. 1 jam menjadi waktu yang praktis bagi user sebelum perlu login ulang dan waktu 1 jam tidak terlalu lama dalam pertimbangan keamanan.

### 3. Detect Double Import Product

```
if (request.getDummyJsonId() == null || productRepository.existsByDummyJsonId(request.getDummyJsonId())) {
    skipped++;
    continue;
}
```
Handling double data dilakukan oleh `ProductServiceImpl`. Saat `scheduler-service` mengambil data dari DummyJson, id pada data tersebut disimpan oleh ResponseDTO dan dikirimkan ke Product Import DTO class.
Pada `importProducts`, id dari DummyJson akan disimpan juga ke dalam database bersamaan dengan data-data lain. Setiap ada data baru yang ingin dimasukkan ke dalam database, maka `importProducts` akan memeriksa dahulu id dari DummyJson.
Data yang diterima oleh Product Service akan dicek terlebih dulu apakah di dalam tabel mst_product sudah memiliki data dengan dummyJsonId yang sama.
Apabila data tersebut ditemukan, maka data double tersebut tidak akan dimasukkan lagi ke dalam database.

### 4. Using Eureka for Service Discovery

Eureka memberikan fasilitas bagi service-service yang tidak saling terkait untuk bisa saling berkomunikasi. Karena aplikasi terdiri dari beberapa independent service yang butuh untuk saling berkomunikasi, misalnya Scheduler Service dan Product Service.
Dengan Eureka, service bisa saling berkomunikasi dengan menggunakan nama service yang dituju dan telah terdaftar ke Eureka, sehingga tidak perlu untuk menuliskan path dan port setiap service.
Untuk skala yang lebih besar, melalui Eureka, service bisa memiliki entitas lebih dari satu. Hal ini akan membantu apabila task yang perlu dikerjakan oleh aplikasi banyak dan dalam jumlah yang besar.

### 5. Using API Gateway for Authentication and Routing

Validasi JWT Token di Gateway membantu agar setiap service tidak perlu mengimplementasi JWT Filter yang sama. Dengan API Gateway juga sudah menyimpan user credentials berupa username di dalam header request, service lain yang membutuhkan pengecekan identitas pengguna dapat langsung mengambil melalui header `X-User-Id`.
API Gateway juga menyediakan jalur masuk yang sama yaitu `http://localhost:8084` meskipun sebenarnya service lain berjalan di port yang berbeda-beda.

### 6. Using Synchronous Feign Communication for Product Import

Synchronous process dipilih karena terdapat proses sequensial yang terjadi dari API pihak ketiga. Hasil dari data products yang diimport dari DummyJson akan digunakan kembali untuk dimapping ke products. Lalu dikirim ke Product Service.
Karena data yang diambil hanya 5 data dalam sekali request dan scheduler juga dijadwalkan untuk berjalan setiap 2 menit, maka proses synchronous sudah cukup memenuhi kebutuhan sistem dan jadi lebih mudah untuk di-maintain.

---

## 8. Notes

- API Gateway adalah entry point untuk requests yang menghadap client.
- Internal komunikasi service-to-service menggunakan Feign Client.
- Scheduler Service tidak mengakses Product Service database secara langsung.
- Internal product import endpoint tidak di-exposed melalui API Gateway.
