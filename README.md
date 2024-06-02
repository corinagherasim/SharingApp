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

### 6. Abstract classes
The **Report** abstract class serves as a blueprint for generating reports on overdue rentals within the Bike Shop. It encapsulates the shared logic and structure needed for report generation, ensuring consistency and reducing code duplication across different types of reports. The classes **ReportBorrow** and **ReportReservation** extends it and uses for different types of bike actions.

### 7. GUI
The GUI package encompasses all the graphical user interface components, built using Java Swing. These components provide a user-friendly way to interact with the app.

- **AdminMenu**: This is the menu for admins based on the email they login with.
- **UserMenu**: This is the menu for users based on the email they login with.

### 8. CRUD
Service classes in the CRUD package perform operations for creating, reading, updating, and deleting database records. Each service class corresponds to an entity in the Models package and uses the DAO (Data Access Object) pattern to interact with the database.

- **BikeDAO**: Used for the details about the bikes.
- **PersonDAO**: Used for the details about the people, whether they are users or admins.
- **TransactionDAO**: Used for the history of bike rentals.

## Database
The application uses MySQL. The database schema is designed to efficiently store and manage data related to bike, people and history of bike rentals.

## Design Patterns
The application employs several design patterns to ensure code quality and maintainability:

- **Singleton**: Ensures that only one instance of the DBConnection class exists, providing a global point of access to the database and only one instance of the shop.
- **DAO (Data Access Object)**: Separates the data access logic from the business logic, making the code easier to manage and test.

## GUI
The graphical user interface, developed using Java Swing, is designed to be intuitive and user-friendly. The main entry point of the application presents two primary options: Login and Register.

### Main Screen
<image src = "https://github.com/corinagherasim/SharingApp/assets/94368761/25a2579c-14a2-4d76-9eb8-7458abd2c53a" width="400" height="400">

### Register Screen
Upon clicking the "Register" button, users are directed to the registration screen. Here, they need to provide the following information:
<br>
<image src = "https://github.com/corinagherasim/SharingApp/assets/94368761/280b3e02-d879-4ff3-b1b2-87fcb3c7880d" width="400" height="400">

Email Validation:
The registration process includes an email validation mechanism:
* The email must contain an "@" symbol for users.
* For admin registrations, the email must end with "@shop.com".

User Verification:
Before creating a new user or admin account, the system verifies that no other users or admins already exist with the provided email address. This check ensures the uniqueness of each account.

Adding to Database:
Once the provided information is validated and verified, the new user or admin account is created and added to the database. This involves securely storing the user's details, including hashing the password for added security.

### Login Screen
Clicking the "Login" button directs users to the login screen, where they must enter their email and password.
<br>
<image src = "https://github.com/corinagherasim/SharingApp/assets/94368761/117db0f6-f675-4f0e-a04c-573e962305ae" width="400" height="400">
<br>
Authentication:
The system authenticates the provided email and password against the stored credentials in the database.
Based on the email domain, users are directed to the appropriate menu:
* Admin Menu: For emails ending with "@shop.com".
* User Menu: For all other valid emails.

### Admin Menu

The Admin Menu provides various administrative functions that the users should not have access to.
<br>
<image src = "https://github.com/corinagherasim/SharingApp/assets/94368761/e036e56c-576a-4c74-9bf9-fb5f302c0229" width="400" height="400">
<br>
**Display All Users and Admins**: Displays the informations about the people in the database grouped by their role (first admins, then users).

**Display All Bikes**: Shows the informations about the bikes and if they are borrowed or reserved also the person who did the action and the reservation or borrow date.

**Add Bike**: Adds a bike to the database. First, the admin has to choose between the categories, then the model of the bike. The id will be the biggest id alredy in the database + 1.

**Remove Bike**: The admin can remove from the database a bike that is not borrowed.

**Search Bike By Model**: The admin can search a bike by its model and if one is found it will show the informations about it.

**Generate Overdue Borrow Report**: If a user borrows a bike and doesn't return it within 14 days it will be shown here.

**Reset Overdue Reservations**: If a user reserves a bike and doesn't borrow it within 7 days the admin can reset the availability for that bike.

**History**: Is a list of all the actions that were performed in the bike shop and it includes the action (borrow, reserve or return), the name of the person who did it, the id of the bike and the date.


### User Menu

The User Menu shows the functionalities that should be used by normal costumers.
<br>
<image src = "https://github.com/corinagherasim/SharingApp/assets/94368761/97aeac8a-41c8-4c6a-8622-950382fc9f22" width="400" height="400">
<br>

**Display All Bikes**: Shows the informations about the bikes and if they are borrowed or reserved it will only show their availability.

**Search Bike By Model**: The user can search a bike by its model and if one is found it will show the informations about it, except the person who is borrowed or reserved by.

**Borrow Bike**: Shows the user a dropdown of the bikes that can be borrowed and he has to choose from them. The bikes are valid if their availability status is AVAILABLE, if they are RESERVED by another user and their reservation date was more than 7 days ago or if they are RESERVED by the user that is trying to borrow them.

**Reserve Bike**: Shows the user a dropdown of the bikes that can be reserved and he has to choose from them. The bikes are valid if their availability status is AVAILABLE or if they are RESERVED by another user and their reservation date was more than 7 days ago.

**Return Bike**: Shows the user a dropdown of the bikes that he borrowed and didn't return and he has to choose from them.

## Main Class
The Main class serves as the entry point of the application. It initializes the GUI and manages the overall behavior of the login and register, ensuring that the application starts correctly and provides the menu needed.
