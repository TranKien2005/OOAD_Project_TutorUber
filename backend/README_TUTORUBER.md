# TutorUber Backend API

Backend đơn giản cho hệ thống TutorUber - Nền tảng kết nối học viên và gia sư.

## Công nghệ sử dụng

- Java 21
- Spring Boot 3.5.6
- MySQL Database
- Maven
- MapStruct (cho mapping DTO)

## Cấu trúc dự án

```
backend/
├── src/main/java/com/library/backend/
│   ├── entities/          # Domain entities
│   │   ├── User.java
│   │   ├── Student.java
│   │   ├── Tutor.java
│   │   ├── Class.java
│   │   ├── Booking.java
│   │   ├── Rating.java
│   │   └── ...
│   ├── dtos/             # Data Transfer Objects
│   │   ├── requests/
│   │   └── responses/
│   ├── repositories/     # Data access layer
│   ├── services/         # Business logic (Control)
│   │   ├── AuthControl.java
│   │   ├── StudentControl.java
│   │   ├── TutorControl.java
│   │   ├── ClassControl.java
│   │   ├── BookingControl.java
│   │   └── RatingControl.java
│   ├── controllers/      # REST API endpoints (Boundary)
│   ├── mappers/          # Entity-DTO mappers
│   ├── configurations/   # Spring configurations
│   └── exceptions/       # Exception handling
└── src/main/resources/
    └── application.yaml
```

## Cài đặt và chạy

### 1. Cấu hình Database

Tạo database MySQL:
```sql
CREATE DATABASE tutoruber_db;
```

### 2. Cấu hình application.yaml

Cập nhật `src/main/resources/application.yaml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/tutoruber_db
    username: root
    password: your_password
```

### 3. Chạy ứng dụng

```bash
# Sử dụng Maven wrapper
./mvnw spring-boot:run

# Hoặc trên Windows
mvnw.cmd spring-boot:run
```

Server sẽ chạy tại: `http://localhost:8080/api`

## API Endpoints

### Authentication

#### Đăng ký
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "password": "password123",
  "fullName": "John Doe",
  "email": "john@example.com",
  "phone": "0123456789",
  "role": "STUDENT"  // STUDENT, TUTOR, hoặc ADMIN
}
```

#### Đăng nhập
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "password123"
}
```

#### Đổi mật khẩu
```http
PUT /api/auth/change-password/{userId}
Content-Type: application/json

{
  "currentPassword": "password123",
  "newPassword": "newpassword456",
  "confirmPassword": "newpassword456"
}
```

### Student APIs

#### Xem hồ sơ học viên
```http
GET /api/students/{studentId}/profile
```

#### Cập nhật hồ sơ học viên
```http
PUT /api/students/{studentId}/profile
Content-Type: application/json

{
  "fullName": "John Updated",
  "email": "newemail@example.com",
  "phone": "0987654321",
  "avatar": "url_to_avatar",
  "bio": "Bio description",
  "preferenceSubjects": "Math, Physics",
  "budgetMin": 50000,
  "budgetMax": 100000,
  "location": "Hanoi"
}
```

### Tutor APIs

#### Xem hồ sơ gia sư
```http
GET /api/tutors/{tutorId}/profile
```

#### Cập nhật hồ sơ gia sư
```http
PUT /api/tutors/{tutorId}/profile
Content-Type: application/json

{
  "fullName": "Jane Tutor",
  "email": "jane@example.com",
  "phone": "0123456789",
  "avatar": "url_to_avatar",
  "bio": "Experienced tutor",
  "hourlyRate": 75000,
  "specialization": "Mathematics",
  "education": "Master in Education",
  "yearsOfExperience": 5
}
```

#### Tìm kiếm gia sư
```http
POST /api/tutors/search
Content-Type: application/json

{
  "subject": "Math",
  "location": "Hanoi",
  "minRate": 50000,
  "maxRate": 100000,
  "minRating": 4.0,
  "specialization": "Mathematics",
  "verificationStatus": "APPROVED"
}
```

### Class APIs

#### Tạo lớp học (Tutor)
```http
POST /api/classes/tutor/{tutorId}
Content-Type: application/json

{
  "subject": "Mathematics",
  "description": "Basic algebra and calculus",
  "fee": 75000,
  "maxStudents": 10,
  "location": "Hanoi",
  "schedules": [
    {
      "dayOfWeek": "MONDAY",
      "startTime": "09:00",
      "endTime": "11:00",
      "location": "Room A1"
    }
  ]
}
```

#### Cập nhật lớp học (Tutor)
```http
PUT /api/classes/{classId}/tutor/{tutorId}
Content-Type: application/json

{
  "subject": "Advanced Mathematics",
  "description": "Updated description",
  "fee": 80000,
  "status": "ACTIVE"
}
```

#### Xóa lớp học (Tutor)
```http
DELETE /api/classes/{classId}/tutor/{tutorId}
```

#### Xem chi tiết lớp học
```http
GET /api/classes/{classId}
```

#### Xem danh sách lớp của gia sư
```http
GET /api/classes/tutor/{tutorId}
```

#### Xem tất cả lớp đang hoạt động
```http
GET /api/classes/active
```

### Booking APIs

#### Đặt lớp học (Student)
```http
POST /api/bookings/student/{studentId}
Content-Type: application/json

{
  "classId": 1
}
```

#### Xác nhận booking (Tutor)
```http
PUT /api/bookings/{bookingId}/confirm/tutor/{tutorId}
```

#### Hủy booking (Student)
```http
PUT /api/bookings/{bookingId}/cancel/student/{studentId}?reason=Schedule conflict
```

#### Xem chi tiết booking
```http
GET /api/bookings/{bookingId}
```

#### Xem danh sách booking của học viên
```http
GET /api/bookings/student/{studentId}
```

#### Xem danh sách booking của gia sư
```http
GET /api/bookings/tutor/{tutorId}
```

### Rating APIs

#### Đánh giá gia sư (Student)
```http
POST /api/ratings/student/{studentId}
Content-Type: application/json

{
  "bookingId": 1,
  "score": 5,
  "comment": "Excellent tutor!"
}
```

#### Xem đánh giá của gia sư
```http
GET /api/ratings/tutor/{tutorId}
```

#### Xem đánh giá của học viên
```http
GET /api/ratings/student/{studentId}
```

## Use Cases đã triển khai

### Student Use Cases
1. ✅ Đăng ký tài khoản
2. ✅ Đăng nhập
3. ✅ Cập nhật hồ sơ
4. ✅ Tìm kiếm gia sư
5. ✅ Đặt lớp học
6. ✅ Hủy booking
7. ✅ Đánh giá gia sư

### Tutor Use Cases
1. ✅ Đăng ký tài khoản
2. ✅ Đăng nhập
3. ✅ Tạo lớp học
4. ✅ Cập nhật thông tin lớp
5. ✅ Xóa lớp
6. ✅ Chấp nhận học viên (confirm booking)
7. ✅ Xem hồ sơ học viên

## Response Format

Tất cả API responses đều theo format:

```json
{
  "data": { ... },
  "message": "Success message"
}
```

## Error Handling

Lỗi sẽ được trả về với format:

```json
{
  "error": {
    "code": "ERROR_CODE",
    "message": "Error description"
  }
}
```

## Lưu ý

- **Security**: Hiện tại đã tắt JWT, Redis, và Throttling để đơn giản hóa. Chỉ sử dụng cho mục đích demo.
- **Password**: Mật khẩu được lưu trực tiếp (không mã hóa) - CHỈ dành cho development.
- **Database**: Sử dụng `ddl-auto: update` - Hibernate tự động tạo/cập nhật schema.

## Các cải tiến trong tương lai

- [ ] Thêm JWT authentication
- [ ] Mã hóa mật khẩu
- [ ] Thêm pagination cho list APIs
- [ ] Thêm validation nâng cao
- [ ] Thêm API documentation (Swagger/OpenAPI)
- [ ] Tích hợp payment gateway
- [ ] Tích hợp email service
- [ ] Thêm chat/messaging feature
