# TutorUber Docker Configuration Summary

## Overview
This document describes the complete Docker setup for the TutorUber backend application.

## Services

### 1. PostgreSQL Database
- **Container Name**: `tutoruber-postgres`
- **Image**: `postgres:17.6-alpine`
- **Port**: 5432 (mapped to host port 5432)
- **Database Name**: `tutoruber_db`
- **Username**: `postgres`
- **Password**: `postgres123`
- **Volume**: `tutoruber-pgdata` (persistent storage)
- **Network**: `tutoruber-network`

### 2. Backend Application
- **Container Name**: `tutoruber-backend`
- **Build**: From local Dockerfile
- **Port**: 8080 (mapped to host port 8080)
- **Dependencies**: PostgreSQL database
- **Network**: `tutoruber-network`
- **Context Path**: `/api`

## Environment Variables

All configuration is centralized in `.env` file:

```env
# Database Configuration
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres123
POSTGRES_DB_NAME=tutoruber_db
POSTGRES_PORT=5432
POSTGRES_PORT_OUTSIDE=5432
POSTGRES_HOST=postgres

# Backend Configuration
BACKEND_PORT=8080
BACKEND_PORT_OUTSIDE=8080

# Redis (disabled but available for future use)
SPRING_REDIS_HOST=redis
SPRING_REDIS_PORT=6379

# JWT (disabled but available for future use)
JWT_KEY=your-secret-key-here-change-in-production
```

## Application Configuration

### application.yaml
- **Spring Application Name**: `tutoruber-backend`
- **Database Driver**: PostgreSQL
- **JPA DDL**: `update` (auto-create/update tables)
- **Show SQL**: `true` (for debugging)
- **Hibernate Dialect**: PostgreSQLDialect
- **Server Port**: Uses `BACKEND_PORT` from environment
- **Context Path**: `/api`

## Network Architecture

All services communicate through a dedicated bridge network `tutoruber-network`:
- `postgres` container accessible as `postgres:5432` from backend
- `backend` container accessible as `localhost:8080` from host machine

## Data Initialization

### InitConfig.java
Automatically seeds database with sample data on first run:

**Admin Account:**
- Username: `admin`
- Password: `admin123`
- Email: admin@tutoruber.com

**Student Accounts:**
- Username: `student1` / `student2` / `student3`
- Password: `password123`
- Each with unique profiles, preferences, and budgets

**Tutor Accounts:**
- Username: `tutor1` / `tutor2` / `tutor3`
- Password: `password123`
- Complete profiles with ratings, certifications, and specializations

**Sample Data:**
- 3 Students with profiles
- 3 Tutors with profiles and certificates
- 4 Classes (Math, Physics, English, Chemistry)
- 6 Schedules for classes
- 6 Bookings
- 4 Ratings

## Docker Commands

### Start all services:
```powershell
docker-compose up -d
```

### Stop all services:
```powershell
docker-compose down
```

### View logs:
```powershell
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f backend
docker-compose logs -f postgres
```

### Rebuild and restart:
```powershell
docker-compose down
docker-compose build --no-cache
docker-compose up -d
```

### Remove all data (including database):
```powershell
docker-compose down -v
```

## API Endpoints

All endpoints are accessible at `http://localhost:8080/api`

### Authentication
- **POST** `/api/auth/register` - Register new user
  - Body: `{username, password, fullName, email, phone, role}`
  - Response: User details
- **POST** `/api/auth/login` - Login user
  - Body: `{username, password}`
  - Response: `{token, user}`

### Students
- **GET** `/api/students/{studentId}/profile` - Get student profile
- **PUT** `/api/students/{studentId}/profile` - Update student profile
  - Body: `{preferenceSubjects, budgetMin, budgetMax, location, bio, avatar}`

### Tutors
- **GET** `/api/tutors/{tutorId}/profile` - Get tutor profile
- **PUT** `/api/tutors/{tutorId}/profile` - Update tutor profile
  - Body: `{bio, hourlyRate, specialization, education, yearsOfExperience, avatar}`
- **POST** `/api/tutors/search` - Search tutors
  - Body: `{subject, minRate, maxRate, minRating, verified}` (all optional)

### Classes
- **POST** `/api/classes` - Create class (tutor)
  - Body: `{tutorId, subject, description, fee, maxStudents, location, schedules: [{dayOfWeek, startTime, endTime}]}`
- **GET** `/api/classes/{classId}` - Get class details
- **PUT** `/api/classes/{classId}` - Update class (tutor)
  - Body: `{subject, description, fee, maxStudents, location, status, schedules}`
- **DELETE** `/api/classes/{classId}` - Delete class (tutor)
- **GET** `/api/classes/tutor/{tutorId}` - Get all classes by tutor

### Bookings
- **POST** `/api/bookings` - Create booking (student)
  - Body: `{studentId, classId}`
- **GET** `/api/bookings/{bookingId}` - Get booking details
- **PUT** `/api/bookings/{bookingId}/confirm` - Confirm booking (tutor)
- **PUT** `/api/bookings/{bookingId}/cancel` - Cancel booking (student)
  - Body: `{cancellationReason}` (optional)
- **GET** `/api/bookings/student/{studentId}` - Get student's bookings
- **GET** `/api/bookings/class/{classId}` - Get class bookings

### Ratings
- **POST** `/api/ratings` - Submit rating (student)
  - Body: `{studentId, tutorId, bookingId, score, comment}`
- **GET** `/api/ratings/tutor/{tutorId}` - Get tutor's ratings

## Security Configuration

**Current Setup (Simplified for Demo):**
- No authentication required
- All endpoints are publicly accessible
- Passwords stored in plain text
- CORS enabled for all origins

**Note**: This is a demo configuration. For production:
- Re-enable JWT authentication
- Add password encryption
- Restrict CORS origins
- Add rate limiting

## Database Schema

Tables created automatically by JPA:
- `users` (base table for User inheritance)
- `students` (extends users)
- `tutors` (extends users)
- `admins` (extends users)
- `student_profiles`
- `tutor_profiles`
- `classes`
- `schedules`
- `bookings`
- `ratings`
- `certificates`

## Verification Checklist

✅ PostgreSQL database configured
✅ Environment variables centralized in .env
✅ Application connects to PostgreSQL
✅ Auto-initialization of sample data
✅ All services on same network
✅ Persistent volume for database
✅ Simplified security (no authentication)
✅ Redis removed (not used)
✅ JWT removed (not used)
✅ Throttling removed (not used)

## Development Workflow

1. **First Time Setup:**
   ```powershell
   docker-compose up -d
   ```
   Database will be initialized with sample data automatically.

2. **Check Application:**
   ```powershell
   docker-compose logs -f backend
   ```
   Look for "Database initialization completed successfully!"

3. **Test Endpoints:**
   ```powershell
   # Test login
   Invoke-WebRequest -Uri "http://localhost:8080/api/auth/login" -Method POST -ContentType "application/json" -Body '{"username":"student1","password":"password123"}'
   ```

4. **Code Changes:**
   ```powershell
   # Rebuild and restart
   docker-compose up -d --build
   ```

5. **Reset Database:**
   ```powershell
   # Remove volume to reset data
   docker-compose down -v
   docker-compose up -d
   ```

## Troubleshooting

### Container won't start
```powershell
# Check logs
docker-compose logs backend

# Verify environment variables
docker-compose config
```

### Database connection failed
```powershell
# Ensure postgres is running
docker ps | Select-String postgres

# Check postgres logs
docker-compose logs postgres

# Test connection from backend container
docker exec tutoruber-backend ping postgres
```

### Port already in use
```powershell
# Find process using port 8080
Get-NetTCPConnection -LocalPort 8080

# Kill the process or change BACKEND_PORT_OUTSIDE in .env
```

## Production Recommendations

When deploying to production, consider:

1. **Security**:
   - Re-enable JWT authentication
   - Use password hashing (BCrypt)
   - Secure environment variables
   - Restrict CORS origins

2. **Database**:
   - Use managed PostgreSQL service
   - Regular backups
   - Connection pooling
   - Read replicas for scaling

3. **Application**:
   - Use production-ready Dockerfile
   - Health checks
   - Resource limits
   - Logging aggregation

4. **Infrastructure**:
   - Use Kubernetes or similar orchestration
   - Load balancer
   - SSL/TLS certificates
   - Monitoring and alerting
