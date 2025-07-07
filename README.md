# 🩺 Medical Appointment System

Hệ thống đặt lịch khám bệnh bao gồm:
- 🖥️ Web Backend: Spring Boot (RESTful API)
- 📱 Ứng dụng Android (Java/Kotlin) kết nối đến backend qua API

---

## 📁 Cấu trúc thư mục

medical-appointment-system/
├── backend/ # Web backend (Spring Boot)
├── mobile-android/ # Android app (Java/Kotlin)
├── .gitignore
├── README.md


## 📥 Hướng dẫn clone project về

### ✅ Clone về máy:

```bash
git clone https://github.com/<your-username>/medical-appointment-system.git
📌 Thay <your-username> bằng tên GitHub của bạn.
Ví dụ: https://github.com/AnhKietVo/medical-appointment-system.git

🖥️ Chạy backend (Spring Boot)
⚙ Yêu cầu:
Java 17+

Maven hoặc Gradle

MySQL hoặc PostgreSQL (tuỳ cấu hình)

▶ Cách chạy:
bash
Copy
Edit
cd medical-appointment-system/backend
./mvnw spring-boot:run
Hoặc mở bằng IntelliJ / Eclipse, tìm class Application.java và bấm Run.

Mặc định backend sẽ chạy tại: http://localhost:8080/api

📱 Chạy ứng dụng Android
⚙ Yêu cầu:
Android Studio (Arctic Fox trở lên)

Android SDK & Emulator hoặc thiết bị thật

▶ Cách chạy:
bash
Copy
Edit
cd medical-appointment-system/mobile-android
Mở thư mục mobile-android/ bằng Android Studio

Bấm File > Sync Gradle

Cập nhật API URL trong Retrofit hoặc phần cấu hình app:

java
Copy
Edit
String BASE_URL = "http://<your-ip>:8080/api/";
⚠ Nếu chạy trên thiết bị thật, thay <your-ip> bằng địa chỉ IP máy đang chạy backend.

Bấm Run để build app

🛠 Công nghệ sử dụng
Thành phần	Công nghệ
Backend	Spring Boot, REST API, JPA
Android	Java/Kotlin, Retrofit
Database	MySQL
Auth	JWT (tuỳ chọn)

🧪 Tính năng chính
📆 Người dùng đặt lịch khám bệnh

👨‍⚕️ Bác sĩ xem và quản lý lịch hẹn

🧾 Ghi chú, kê đơn, chẩn đoán

🔐 Đăng nhập, phân quyền người dùng

📱 App Android giao diện thân thiện

