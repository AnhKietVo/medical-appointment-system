# 🩺 Medical Appointment System
Hệ thống đặt lịch hẹn khám bệnh, bao gồm backend Spring Boot và ứng dụng di động Android. Dự án hỗ trợ triển khai qua Docker.
## 📦 Tech Stack

- 🧠 **Backend**: Spring Boot (Java)
- 🗄️ **Database**: MySQL
- 📱 **Mobile**: Android (Java)
- 🐳 **Container**: Docker, Docker Compose
## 🚀 Cài đặt 
```text
Yêu cầu:
Docker & Docker Compose đã cài sẵn
Git
```
### 1. Clone project

```bash
git clone https://github.com/<your-username>/medical-appointment-system.git
cd medical-appointment-system/backend
```
### 2.Kiểm tra file .env
```env
# MySQL Configuration
MYSQL_ROOT_PASSWORD=root
MYSQL_DATABASE=appointmentdb
MYSQL_USER=user
MYSQL_PASSWORD=123456

# Spring Boot DataSource
SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/appointmentdb
SPRING_DATASOURCE_USERNAME=user
SPRING_DATASOURCE_PASSWORD=123456
```

### 3.Chạy với Docker Compose

```bash
docker-compose up --build
```
Ứng dụng sẽ chạy tại: http://localhost:8080


## Cấu trúc project
```text
medical-appointment-system/
├── backend/
    ├── app/
    │   ├── src
    │   ├── target/       
    │   ├── Dockerfile
    │   └── pom.xml
    ├── docker-compose.yml
    ├── .env
├── mobile-android/
│   └── ...
└── README.md
```
