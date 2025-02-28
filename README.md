# CarRentalManagement
Overview  
The Car Rental System is a Java-based OOP project that allows users to rent and return vehicles, track rental history, and process payments. The system follows object-oriented programming principles, including abstraction, inheritance, encapsulation, and polymorphism. It also incorporates file handling to store rental details persistently.

Features  
- OOP-based design  
- Vehicle management (add, rent, and return vehicles)  
- Custom exception handling (`InvalidRentalException` for rental validation)  
- File handling (stores vehicle data and rental history)  
- Payment processing simulation  
- Rental availability tracking  

Class Structure  

Vehicle (Abstract Class)  
- Stores brand, model, rental price, and availability status  
- Provides methods to rent and return vehicles  

Car & Bike (Inherit from Vehicle)  
- Car: Includes number of doors and engine capacity  
- Bike: Includes helmet availability  

RentalManager (Manages Rentals & File Operations)  
- Stores available vehicles  
- Handles rental processing, returns, and payment validation  
- Saves and loads data from files (Vehicle.dat & rentalHistory.txt)  

InvalidRentalException (Custom Exception)  
- Ensures valid rental conditions (rental duration between 1-30 days)  

How to Run  

1. Compile the project:  
   ```bash
   javac com/carrentalsystem/*.java
