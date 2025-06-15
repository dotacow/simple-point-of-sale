# 🧾 SimplePointOfSale

A lightweight JavaFX-based Point of Sale system connected to a MySQL database. Built for managing product sales, users, and analytics through a clean, modular interface.

> ✅ Developed as a final project for the **Object Oriented Programming 2** course at **Applied Science University, Jordan**.  
> 🧑‍🏫 Supervised by **Dr. Radwan Batieha**.

---

## 📸 Screenshots

> _You can insert screenshots of login, dashboard, and manage scenes here._

---

## 🔧 Features

- 👤 **User Authentication** – Login system with roles.
- 🛍️ **Product Management** – Add, update, and delete products.
- 💸 **Sales Management** – Record and delete sales transactions.
- 📊 **Statistics Dashboard** – View sales statistics per user/date.
- 🎨 **Theme Switcher** – Supports multiple CSS themes (e.g. Dark, Nordic, Gruvbox).
- 📤 **Export to CSV** – Export tables to CSV files.
- 🧩 **Modular MVC Architecture**

---

## 🧪 Technologies Used

- **JavaFX** – UI framework.
- **MySQL** – Relational database.
- **JDBC** – Database connection.
- **CSS** – Custom themes.
- **NetBeans** – Development environment.

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
