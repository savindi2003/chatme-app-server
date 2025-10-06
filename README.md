# 💬 ChatMe Server – Java REST Backend for Chat Application

[![Backend](https://img.shields.io/badge/backend-Java-orange)]()
[![Framework](https://img.shields.io/badge/framework-Servlets%20%26%20Hibernate-blue)]()
[![Database](https://img.shields.io/badge/database-MySQL-lightgrey)]()
[![Architecture](https://img.shields.io/badge/architecture-REST%20API-green)]()
[![License](https://img.shields.io/badge/license-MIT-success)]()

---

## 🧩 Overview

**ChatMe Server** is the backend service for the **ChatMe React Native mobile application**.  
It provides RESTful APIs for user authentication, real-time chatting (text + image), and friend management.

The backend is built with **Java Servlets** and **Hibernate ORM** to handle database operations efficiently and securely.  
It uses **MySQL** for data persistence and is optimized for scalability and multi-user chat sessions.

---

## ⚙️ Features

- 🔐 **User Authentication**
  - Secure login & registration endpoints
- 🧑‍🤝‍🧑 **Friend Management**
  - Add, load, and manage friend lists
- 💬 **Chat Messaging**
  - Send and receive text & image messages
  - Delete message support
- 🖼️ **Image Upload API**
  - Multipart form-data file handling
- 👁️ **Privacy Control**
  - Manage visibility of profile picture, mobile number, and online status
- 🗄️ **Hibernate ORM**
  - Smooth interaction with MySQL using entity mappings
- 🔁 **JSON Response Format**
  - All responses are sent as structured JSON

---

## 🧱 Tech Stack

| Layer | Technology |
|--------|-------------|
| **Language** | Java |
| **Framework** | Java Servlets |
| **ORM** | Hibernate |
| **Database** | MySQL |
| **Communication** | RESTful JSON APIs |
| **Server** | GlassFish / Tomcat |
| **Build Tool** | Maven / NetBeans |

---

## 🗄️ Database Configuration

  ### 🔧 Hibernate Configuration (`hibernate.cfg.xml`)

  ```bash
  <property name="hibernate.connection.driver_class">com.mysql.cj.jdbc.Driver</property>
        <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/chatme_db</property>
        <property name="hibernate.connection.username">root</property>
        <property name="hibernate.connection.password">your_password</property>
        <property name="hibernate.dialect">org.hibernate.dialect.MySQL8Dialect</property>
   ```
   ### 💾 Database Setup
   - Create MySQL database: `chatme_db`
   - Import SQL dump: `chatme_db.sql`
   - Update connection settings in: `hibernate.cfg.xml`
   - Deploy the project on GlassFish or Tomcat server.

---

## 🔗 Integration with ChatMe Mobile

This backend connects with the ChatMe React Native app through REST API endpoints.

-  Frontend Repo: ChatMe Mobile https://github.com/savindi2003/chatme-app-mobile.git

---

## 👩‍💻 Author
**Savindi Duleesha**  
📧 savindi@example.com  
🌐 [Portfolio](https://savindi2003.github.io/my-portfolio/)

---

## 📜 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---



