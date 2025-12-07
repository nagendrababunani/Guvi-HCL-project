# Real-Time Customer Feedback Aggregator

A Java-based application to collect, store, manage, and analyze customer feedback in real time using MongoDB and Doubly Linked List data structure.

##  Overview

This project provides an automated solution for feedback handling, enabling:

- Add new feedback
- Display all feedback
- Search feedback by ID
- Update feedback record
- Delete feedback
- Calculate average rating

The system integrates Java with MongoDB for persistent storage while using Doubly Linked List for efficient in-memory operations.

---

##  Technologies Used

- Java (JDK 8+)
- MongoDB
- MongoDB Java Driver
- IntelliJ / Eclipse / NetBeans
- MongoDB Compass (optional)

---

##  System Requirements

### Hardware
- Minimum: Intel i3, 4GB RAM
- Recommended: Intel i5 or higher, 8GB RAM

### Software
- Windows / Linux / macOS
- MongoDB 4.0+
- Any Java IDE

---

##  Architecture

1. Start application  
2. Connect with MongoDB database  
3. Load existing feedback into Doubly Linked List  
4. Show menu operations  
5. Perform CRUD & rating analysis  
6. Sync changes with MongoDB  
7. Exit program

---

##  Project Structure

src/
└── RealTimeCustomerFeedbackAggregator.java

yaml
Copy code

---

## 🧩 Features Breakdown

✔ CLI user menu  
✔ Doubly linked list for fast access  
✔ Real-time database operations  
✔ Rating analytics  

---

##  How to Run

### 1. Start MongoDB Service

Default URI:

mongodb://localhost:27017

shell
Copy code

### 2. Create Database & Collection

Database: customer_feedback_db
Collection: feedback

perl
Copy code

### 3. Compile and Execute

```sh
javac RealTimeCustomerFeedbackAggregator.java
java RealTimeCustomerFeedbackAggregator
 Output Examples
Console interaction (menu input/output)

MongoDB Compass entries display

(Add your screenshots here)

 Conclusion
This project demonstrates:

Java + MongoDB integration

Object-oriented principles

Data structure usage

Automated feedback processing

Real-time analytics

It improves accuracy, performance, and decision-making based on customer responses.

 Author
BUCHAKKAGARI NAGENDRA BABU
Under HCL GUVI Training Certification

 Support
If you like this project, please give it a  on GitHub!

yaml
Copy code

---

If you want variations like:

✔ README with badges  
✔ README with table of contents  
✔ README with screenshots section pre-linked  

Just tell me — I’ll generate it!
