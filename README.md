Application link https://adityasinnghh.github.io/Finder/

---

# 🔎 Finder – Lost & Found Web Application

*A smart and simple solution to manage lost and found items within an institution.*

---

## 📘 Overview

**Finder** is a web-based platform created to streamline how students report and recover lost items on campus. Traditionally, lost & found processes depend on manual logbooks or word-of-mouth — often resulting in confusion and unclaimed items. Finder solves this problem by providing a **centralized online portal** where users can:

* Report lost or found items
* Upload details and images
* Search for items posted by others
* Quickly connect owners with finders

Built by a 3-member team — **Aditya**, **Shivam**, and **Piyush** — this project is currently under development and evolving with new features and improvements.

---

## 🧠 Problem Statement

Students frequently lose or misplace items on campus, and there is no simple, accessible, and reliable system to track and retrieve them. Manual handling leads to inefficiency, delays, and unclaimed items.

**Finder aims to digitalize the lost & found process**, offering an organized, fast, and user-friendly interface for managing item records.

---

## 🎯 Project Objectives

* Provide a **centralized platform** for lost & found activities.
* Reduce time and effort in searching for lost belongings.
* Ensure **trust**, **accuracy**, and **security** in reporting items.
* Enable a **modern, responsive, and intuitive UI** for all devices.
* Lay the foundation for advanced features like authentication, notifications, and admin verification.

---

## 🧩 System Workflow Diagram (Explained)

![Image](https://www.researchgate.net/publication/283290085/figure/fig4/AS%3A668308651118598%401536348607358/Lost-and-Found-System-Workflow.png?utm_source=chatgpt.com)

![Image](https://www.researchgate.net/publication/355384108/figure/fig1/AS%3A1080468095148033%401634615076555/Flowchart-of-the-lost-found-system.png?utm_source=chatgpt.com)

**1. User Visits the Finder Web App**
→ Home page provides lost/found listings + search functionality.

**2. User Selects an Action**
→ *Search Item* OR *Report Lost/Found Item*

**3. System Processes Request**

* If *searching*: App fetches matching items (future: from database).
* If *reporting*: User uploads item details, images, and description.

**4. Database Interaction (Future Integration)**
→ All reports stored in MySQL / SQLite for persistence.

**5. Display Results**
→ User sees matching listed items OR confirmation of successful submission.

**6. Admin Verification (Future)**
→ Admin validates item ownership.

**7. Successful Claim**
→ Owner recovers their item.

---

## 🛠️ Tech Stack

### **Frontend**

* HTML5
* CSS3
* JavaScript (Vanilla JS)
* Responsive design (mobile-first approach)

### **Backend**

* Java
* Maven project structure
* (Optional future move: Spring Boot)

### **Database (Planned)**

* MySQL / SQLite

### **Development Tools**

* Git & GitHub
* VS Code / IntelliJ
* Postman for API testing (future)

---

## 📁 Project Structure (Detailed)

```
Finder/
│
├── public/                   # Static frontend files
│   ├── index.html            # Main UI page
│   ├── styles.css            # App styling
│   ├── script.js             # Frontend logic
│   └── assets/               # Images, icons, screenshots
│
├── src/                      # Backend code (Java / Maven)
│   ├── main/
│   │   ├── java/             # Controllers, services (future)
│   │   └── resources/        # Config files
│   └── test/                 # Unit tests (future)
│
├── .gitignore                # Git ignored files list
├── README.md                 # Project documentation
└── pom.xml                   # Maven dependencies & project config
```

---

## 🚀 Features (Detailed Breakdown)

### ✔ **Lost Item Posting**

Users can submit:

* Item name
* Category
* Description
* Date/time lost
* Photo upload (planned)

### ✔ **Found Item Reporting**

Finders can submit found item details so owners can reach them.

### ✔ **Search Functionality**

* Keyword search
* Category filters
* Sorting options (planned)

### ✔ **Responsive UI**

Optimized for:

* Mobile
* Tablet
* Desktop

### ✔ **Fast & Lightweight**

No heavy frameworks in the frontend.

---

## 🔧 Setup & Installation Guide

### **1. Clone the Repository**

```
git clone https://github.com/your-username/finder.git
cd finder
```

### **2. Run the Frontend**

Simply open the file:

```
public/index.html
```

### **3. Run the Backend (Maven)**

Ensure Maven is installed and recognized:

```
mvn -v
```

Then run:

```
mvn clean install
mvn spring-boot:run
```

or if no Spring Boot:

```
mvn compile
mvn exec:java
```

---

## 📸 Screenshots (Add Yours Later)

```
![Home Page](./public/assets/home-screen.png)
![Report Item Page](./public/assets/report-item.png)
![Search Results](./public/assets/search-results.png)
```

---

## 🔮 Future Enhancements (Detailed Roadmap)

### **Phase 1 — Core Improvements**

* Add login/signup for students
* Store item records in MySQL database
* Add pagination & sorting

### **Phase 2 — Admin Dashboard**

* Admin verifies item ownership
* Flag and remove spam reports

### **Phase 3 — Advanced Features**

* Push / email notifications
* Live chat between finder & owner
* QR-based item tagging
* AI-based item image categorization

### **Phase 4 — Deployment**

* Host frontend on Netlify / Vercel
* Host backend on Render / Railway

---

## 🧑‍🤝‍🧑 Team Members & Roles

| Name             | Role               | Responsibilities                         |
| ---------------- | ------------------ | ---------------------------------------- |
| **Aditya Singh** | Frontend Developer | UI creation, layout design, interactions |
| **Shivam**       | Backend Developer  | Java logic, APIs, future DB integration  |
| **Piyush**       | UI/UX & Logic      | Interface improvements, feature logic    |

---

## 🤝 Contribution Guidelines

We welcome contributions!

### **Steps to Contribute**

1. Fork the repo
2. Create a new branch
3. Commit your changes
4. Push branch and open a pull request

### **Code Quality Rules**

* Use meaningful commit messages
* Keep PRs focused on one feature
* Follow clean code principles

---

## 📝 License

This project is licensed under the **MIT License**.
You are free to modify and distribute with attribution.

---

## ⭐ Support the Project

If you like this project, please consider giving it a **star ⭐ on GitHub** — it motivates us to keep improving!

---
