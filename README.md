# Simple Point of Sale (POS) System  
> JavaFX + MySQL | OOP2 Project – Applied Science University

## 📚 Project Overview

This is a desktop-based **Point of Sale (POS)** system built using **JavaFX** for the GUI and **MySQL** for persistent data storage. It was created as a semester project for the **Object Oriented Programming 2 (OOP2)** course at Applied Science University, Jordan.

The system enables basic sales management, product inventory handling, and user authentication. Designed with modular and object-oriented architecture, this project demonstrates practical applications of Java's OOP features, JavaFX interface design, and integration with SQL databases.

> 🧑‍🏫 **Supervisor**: Dr. Radwan Batieha  
> 🎓 **Course**: Object Oriented Programming 2 (OOP2)  
> 🏫 **University**: Applied Science University

---

## ✨ Features

### 🔐 User Authentication
- Login with username and password
- Role-based access (Admin vs Sales Staff)

### 📦 Product Management
- Add, edit, delete, and list products
- Image support for products (optional)
- Quantity and price tracking

### 💵 Sales Management
- Create sales transactions
- Display and manage previous sales
- Delete a sale (with confirmation and disclaimer)

### 🧑 User Management
- List and manage registered users (admin only)

---

## 🧰 Technologies Used

| Tech          | Description                                |
|---------------|--------------------------------------------|
| Java          | Core language, using OOP principles        |
| JavaFX        | GUI framework                              |
| MySQL         | Relational database                        |
| JDBC          | Java Database Connectivity API             |
| Scene Builder | (optional) FXML UI prototyping             |

---

## 📁 Project Structure

```bash
src/
├── controllers/
│   ├── DashboardController.java
│   ├── ProductController.java
│   ├── SaleController.java
│   ├── StatisticsController.java
│   └── UserController.java
│
├── main/
│   └── Main.java
│
├── models/
│   ├── Product.java
│   ├── Sale.java
│   └── User.java
│
├── themes/
│   ├── base.css
│   ├── cat.css
│   ├── dark.css
│   ├── gruvbox.css
│   ├── light.css
│   └── nordic.css
│
├── utils/
│   ├── CSVExporter.java
│   ├── DBHelper.java
│   ├── ResHelper.java
│   └── ThemeManager.java
│
├── views/
│   ├── cashierScenes/
│   │   ├── CheckCashScene.java
│   │   └── MakeSaleScene.java
│   │
│   ├── globalScenes/
│   │   ├── DashBoardScene.java
│   │   ├── LoginView.java
│   │   └── SideBarComponent.java
│   │
│   └── managersScenes/
│       ├── ManageProductsScene.java
│       ├── ManageSalesScene.java
│       ├── ManageUsersScene.java
│       └── ViewSalesStatsScene.java
