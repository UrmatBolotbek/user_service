# User Service

User Service is a core microservice in our social network platform that manages user profiles and all user-related data. It plays a vital role in connecting startup creators, helping them build great teams by maintaining user identities, social relationships, and personalized settings across the application.

---

## Overview

- **Comprehensive User Management:**  
  Manages user accounts including credentials, profile information, contact details, and profile pictures. Supports unique identifiers, email and phone validation, and password security.

- **Social Relationships:**  
  Facilitates social interactions through followers, followees, mentorship relationships, and event participation. It also handles recommendations, ratings, and contacts to enhance user networking and collaboration.

- **Extended Functionality:**  
  Integrates with modules for events, goals, skills, and promotions. It supports embedded user profile images and preferences for contact methods, ensuring a personalized user experience.

---

## Data Model Highlights

The core `User` entity includes key fields such as:
- **Basic Info:**  
  `id`, `username`, `email`, `phone`, `password`, and `active` status.
- **Profile Details:**  
  `aboutMe`, profile picture (with multiple sizes), locale, and social metadata.
- **Social Interactions:**  
  Lists of followers, followees, mentorships, events, recommendations, and contacts.
- **Additional Attributes:**  
  Country, city, experience, timestamps (`createdAt` and `updatedAt`), and flags for banned status.
- **Embedded and Associated Entities:**  
  Includes embedded profile picture details, one-to-one relationships with contact preferences, premium and promotion details, as well as various many-to-many and one-to-many relationships for events, goals, and skills.

---

### Technologies Used

- [Spring Boot](https://spring.io/projects/spring-boot) – Main framework for building the application.
- [PostgreSQL](https://www.postgresql.org/) – Primary relational database.
- [Redis](https://redis.io/) – Used as a cache and for pub/sub messaging.
- [Testcontainers](https://testcontainers.com/) – For isolated testing with a real database.
- [Liquibase](https://www.liquibase.org/) – For managing database schema migrations.
- [Gradle](https://gradle.org/) – Build system.

### Database & Infrastructure

- **Database:**  
  PostgreSQL is managed in a separate service ([infra](../infra)). Liquibase automatically applies necessary migrations at application startup.
- **Redis:**  
  Redis is deployed as a single instance in the [infra](../infra) service, used for caching and session management.
- **Testing:**  
  Integration tests use Testcontainers to run an isolated PostgreSQL instance. The project demonstrates data access using both JdbcTemplate and JPA (Hibernate).

---

### Conclusion

User Service is essential for maintaining user data and facilitating social interactions within our platform.Its robust design, built on a standardized Spring Boot template, ensures scalability and smooth integration with other microservices.This service empowers startup creators by providing a secure and feature-rich user management system.
