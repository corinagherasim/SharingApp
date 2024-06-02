# Sharing App for bikes

A Java-based application to manage bike shop operations depending on the email you login with. I used Object-Oriented Programming (OOP) principles to make a system that provides an intuitive interface for administrators (view history, add or remove bikes etc.) and users (borrow, reserve or return a bike). 

## Technologies used:
* Java: Core application logic
* Swing: Graphical User Interface
* MySQL: Database management

## Project structure
### 1. Models
The Models package contains the primary entities of the application. Each entity represents a real-world object in the bike rental environment. The main classes include:
- **Bike**: Represents a bike with attributes like model, category, and availability status.
- **Person**: Represents a customer or staff member with attributes such as name and email address.
- **User**: It extends the Person class and can borrow, reserve or return bikes.
- **Admin**: It extends the Person class and can manage the app and the bikes in store. 
- **Transaction**: Represents a record of bike rentals, returns, and reservations made by users.

### 2. Interfaces
Interfaces in this application define the structure for various operations, ensuring consistency and modularity.

- **Searchable Interface**: Defines methods for searching bikes and people based on different criteria.
- **Rentable Interface**: Defines methods for handling bike rental operations.

### 3. Enum
Enums are used to define a set of constants for specific attributes, providing better type safety and readability.

- **Availability**: Enum for storing different availability statuses of bikes (AVAILABLE, RESERVED, BORROWED).
- **BikeCategory**: Enum for storing the bike categories so that they are grouped in sections by them (MAN, WOMAN, KID).

### 4. Utils
Utility classes provide auxiliary functions that support the main application logic.

- **DBConnection**: Manages the connection to the MySQL database, ensuring efficient and secure database operations.

### 5. Custom exceptions
Custom exceptions enhance error handling by providing specific feedback when invalid data is encountered.

- **BikeNotFoundException**: Thrown when a bike is not found based on specific criteria.
- **UserNotFoundException**: Thrown when a user or admin is not found based on specific criteria.

### 6. GUI
The GUI package encompasses all the graphical user interface components, built using Java Swing. These components provide a user-friendly way to interact with the app.

- **AdminMenu**: This is the menu for admins based on the email they login with.
- **UserMenu**: This is the menu for users based on the email they login with.

### 7. CRUD
Service classes in the CRUD package perform operations for creating, reading, updating, and deleting database records. Each service class corresponds to an entity in the Models package and uses the DAO (Data Access Object) pattern to interact with the database.

- **BikeDAO**: Used for the details about the bikes.
- **PersonDAO**: Used for the details about the people, whether they are users or admins.
- **TransactionDAO**: Used for the history of bike rentals.


