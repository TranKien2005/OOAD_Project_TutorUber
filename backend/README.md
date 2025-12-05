# 🎓 TutorUber Backend API

> Simple CRUD backend for TutorUber - Online Tutoring Platform

## 🚀 Quick Start

### Prerequisites
- Docker Desktop installed and running
- Port 8080 and 5432 available

### Start Backend
```powershell
cd backend
docker-compose up -d
```

Wait ~15 seconds for initialization, then test:
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method POST -ContentType "application/json" -Body '{"username":"student1","password":"password123"}'
```

**Expected:** `success: true` ✅

## 📚 Documentation

- **[API_REFERENCE.md](API_REFERENCE.md)** - Complete API documentation with examples
- **[DOCKER_SETUP.md](DOCKER_SETUP.md)** - Docker configuration & commands
- **[HANDOVER_CHECKLIST.md](HANDOVER_CHECKLIST.md)** - Integration checklist for frontend
- **[README_TUTORUBER.md](README_TUTORUBER.md)** - Project overview & architecture

## 🔗 API Base URL

```
http://localhost:8080/api
```

## 🧪 Test Accounts

| Role    | Username | Password    | User ID |
|---------|----------|-------------|---------|
| Student | student1 | password123 | 2       |
| Student | student2 | password123 | 3       |
| Student | student3 | password123 | 4       |
| Tutor   | tutor1   | password123 | 5       |
| Tutor   | tutor2   | password123 | 6       |
| Tutor   | tutor3   | password123 | 7       |
| Admin   | admin    | admin123    | 1       |

## 📋 Main Endpoints

### Authentication
```javascript
POST /api/auth/register
POST /api/auth/login
```

### Students
```javascript
GET    /api/students/{studentId}/profile
PUT    /api/students/{studentId}/profile
```

### Tutors
```javascript
GET    /api/tutors/{tutorId}/profile
PUT    /api/tutors/{tutorId}/profile
POST   /api/tutors/search
```

### Classes
```javascript
POST   /api/classes
GET    /api/classes/{classId}
PUT    /api/classes/{classId}
DELETE /api/classes/{classId}
GET    /api/classes/tutor/{tutorId}
```

### Bookings
```javascript
POST   /api/bookings
GET    /api/bookings/{bookingId}
PUT    /api/bookings/{bookingId}/confirm
PUT    /api/bookings/{bookingId}/cancel
GET    /api/bookings/student/{studentId}
GET    /api/bookings/class/{classId}
```

### Ratings
```javascript
POST   /api/ratings
GET    /api/ratings/tutor/{tutorId}
```

## 💡 Example Usage

### Login
```javascript
const response = await fetch('http://localhost:8080/api/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    username: 'student1',
    password: 'password123'
  })
});

const result = await response.json();
console.log(result);
// {
//   "success": true,
//   "message": "Login successful",
//   "data": {
//     "token": "simple-token-2",
//     "user": { ... }
//   }
// }
```

### Search Tutors
```javascript
const response = await fetch('http://localhost:8080/api/tutors/search', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    subject: 'Mathematics',
    minRating: 4.5
  })
});

const result = await response.json();
console.log(result.data); // Array of tutors
```

## 🔧 Docker Commands

```powershell
# Start
docker-compose up -d

# Stop
docker-compose down

# View logs
docker-compose logs -f backend

# Rebuild
docker-compose up -d --build

# Reset database
docker-compose down -v
docker-compose up -d
```

## 📦 Response Format

All responses follow this structure:

```json
{
  "success": true,
  "message": "Operation description",
  "data": { }
}
```

## ⚙️ Configuration

All settings in `.env`:
```env
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres123
POSTGRES_DB_NAME=tutoruber_db
BACKEND_PORT=8080
```

## 🗃️ Sample Data

Database is auto-initialized with:
- ✅ 1 Admin + 3 Students + 3 Tutors
- ✅ 4 Classes (Math, Physics, English, Chemistry)
- ✅ 6 Bookings
- ✅ 4 Ratings

## 🔒 Security Note

**This is a demo/development setup:**
- No JWT authentication required
- Passwords stored in plain text
- CORS enabled for all origins

**For production**, enable:
- JWT authentication
- Password encryption
- CORS whitelist
- Rate limiting

## 🛠️ Tech Stack

- Spring Boot 3.5.6
- Java 21
- PostgreSQL 17.6
- Docker & Docker Compose
- MapStruct 1.6.3
- Lombok

## 📞 Troubleshooting

**Container won't start?**
```powershell
docker-compose logs backend
```

**Port 8080 already in use?**
```powershell
# Change BACKEND_PORT_OUTSIDE in .env
BACKEND_PORT_OUTSIDE=8081
```

**Database connection failed?**
```powershell
docker ps  # Check if postgres is running
docker-compose restart postgres
```

## ✅ Integration Checklist

- [ ] Docker Desktop running
- [ ] Containers started: `docker-compose up -d`
- [ ] Test login endpoint
- [ ] Check API documentation
- [ ] Review sample data
- [ ] Test frontend connection

---

**Happy Coding! 🚀**

For detailed API documentation, see [API_REFERENCE.md](API_REFERENCE.md)
