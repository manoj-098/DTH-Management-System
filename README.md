# 📡 DTH (Direct-To-Home) Management System – Backend

A backend system built using **Spring Boot** to manage DTH services, including user subscriptions, device management, transactions, and support operations. The system is secured with **Basic Authentication** and tested via **Swagger UI**.

<br>

## 🛠️ Tech Stack

- **Backend**: Spring Boot
- **Database**: MySQL
- **API Testing**: Swagger UI
- **Authentication**: Basic Authentication
- **Architecture**: Layered (Controller → Service → Repository)

<br>

## 📌 Key Features

- 🔐 Role-based access (Admin, Service Provider, User)
- 👥 User and Service Provider management
- 📦 Subscription and Plan management
- 📺 Device assignment and activation tracking
- 💳 Transaction processing
- 🛠️ Support Ticket generation and resolution
- 🛡️ Basic Authentication for securing APIs

<br>

## 🧩 Entities Overview

| Entity       | Description                                        |
|--------------|----------------------------------------------------|
| `User`       | Stores user details with roles (USER / ADMIN)     |
| `Subscription` | Links users to active plans                      |
| `Plan`       | Defines available DTH service plans                |
| `Transaction`| Records payment transactions for subscriptions    |
| `Ticket`     | Used for customer support and issue tracking       |
| `Device`     | Represents set-top boxes or receivers assigned     |

<br>

## ✅ Completed Tasks

- [x] RESTful API development with Spring Boot
- [x] MySQL integration with JPA/Hibernate
- [x] Layered architecture implementation
- [x] Role-based access control
- [x] Basic Authentication
- [x] Swagger UI integration for testing APIs
- [x] Designed Class Diagram & System Architecture

<br>

## 📂 Project Structure (Simplified)
```
src/
├── controller/       # API endpoints
├── service/          # Business logic
├── repository/       # JPA Repositories
├── model/            # Entity classes
├── config/           # Security configuration
└── DthManagementSystemApplication.java
```

<br>


## 🔗 Project Links

- 🚀 **Live Repository**   : [GitHub Link](https://github.com/manoj-098/DTH-Management-System)
- 🧱 **Class Diagram**     : [View on Google Drive](https://drive.google.com/file/d/1deXVclNSWhhBY7yygzW9RbmII4jkTVJg/view?usp=sharing)
- 🧭 **System Architecture Diagram**: [View on Google Slides](https://docs.google.com/presentation/d/1jXJaw4jKcN04QT7vP_NUraOR3cfE3QkGuLiO4wMwrQY/edit?usp=sharing)

<br>

## 🧪 How to Test the APIs

1. **Run the Spring Boot application**
2. Visit: `http://localhost:8080/swagger-ui/index.html`
3. Authenticate using Basic Auth credentials
4. Try out endpoints for CRUD operations on all entities

<br>

## 🧱 Future Enhancements

- 🔐 JWT-based Authentication and Authorization
- 📊 Admin dashboard for analytics
- 📧 Email notifications on ticket updates
- 🌐 React frontend integration (planned)

<br>

## 🙋‍♂️ Author

**Manoj V**  
Java Developer | Backend Enthusiast | Spring Boot Learner  
📫 [LinkedIn](https://www.linkedin.com/in/manoj-098)

<br>

## 📃 License

This project is open-source and available under the [MIT License](LICENSE).
