package com.carrentalsystem;

import java.io.*;
import java.util.*;

class InvalidRentalException extends Exception {
    public InvalidRentalException(String message) {
        super(message);
    }
}

abstract class Vehicle implements Serializable {
    private String brand;
    private String model;
    private double rentalPrice;
    private boolean isAvailable;
    private Date returnDate;

    public Vehicle(String brand, String model, double rentalPrice) throws InvalidRentalException {
        if (rentalPrice <= 0) throw new InvalidRentalException("Rental Price Must Be Positive.");
        this.brand = brand;
        this.model = model;
        this.rentalPrice = rentalPrice;
        this.isAvailable = true;
        this.returnDate = null;
    }

    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public double getRentalPrice() { return rentalPrice; }
    public boolean isAvailable() { return isAvailable; }
    public Date getReturnDate() { return returnDate; }

    public void rentVehicle(int days) {
        isAvailable = false;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, days);
        this.returnDate = calendar.getTime();
    }

    public void returnVehicle() {
        isAvailable = true;
        returnDate = null;
    }

    public abstract String getDetails();
}

class Car extends Vehicle {
    private int numDoors;
    private double engineCapacity;

    public Car(String brand, String model, double rentalPrice, int numDoors, double engineCapacity) throws InvalidRentalException {
        super(brand, model, rentalPrice);
        if (numDoors <= 0 || engineCapacity <= 0) throw new InvalidRentalException("Invalid Car Details");
        this.numDoors = numDoors;
        this.engineCapacity = engineCapacity;
    }

    public String getDetails() {
        return "Car: " + getBrand() + " " + getModel() + " | Doors: " + numDoors + " | Engine: " + engineCapacity + "L | Price: " + getRentalPrice() + " Pkr";
    }
}

class Bike extends Vehicle {
    private boolean hasHelmet;

    public Bike(String brand, String model, double rentalPrice, boolean hasHelmet) throws InvalidRentalException {
        super(brand, model, rentalPrice);
        this.hasHelmet = hasHelmet;
    }

    public String getDetails() {
        return "Bike: " + getBrand() + " " + getModel() + " | Helmet: " + (hasHelmet ? "Yes" : "No") + " | Price: " + getRentalPrice() + " Pkr";
    }
}

class RentalManager {
    private List<Vehicle> availableVehicles;
    private static final String VEHICLE_FILE = "Vehicle.dat";
    private static final String RENTAL_FILE = "rentalHistory.txt";

    public RentalManager() {
        availableVehicles = new ArrayList<>();
        loadVehiclesFromFile();
    }

    public void addVehicle(Vehicle vehicle) {
        availableVehicles.add(vehicle);
        saveVehiclesToFile();
    }

    public void rentVehicle(String brand, String model, int days, String paymentMethod) throws InvalidRentalException {
        if (days < 1 || days > 30)
            throw new InvalidRentalException("Rental Days Must be Between 1 to 30 Days");

        for (Vehicle ve : availableVehicles) {
            if (ve.getBrand().equalsIgnoreCase(brand) && ve.getModel().equalsIgnoreCase(model) && ve.isAvailable()) {
                double totalCost = ve.getRentalPrice() * days;
                System.out.println("Total Cost For: " + days + " Days " + totalCost + " Pkr");
                if (!processPayment(totalCost, paymentMethod)) {
                    System.out.println("Payment failed. Rental cancelled.");
                    return;
                }
                ve.rentVehicle(days);
                saveRentalToFile(ve, days, totalCost, paymentMethod);
                saveVehiclesToFile();
                System.out.println("Rental successful: " + ve.getDetails());
                return;
            }
        }
        throw new InvalidRentalException("Vehicle not available for rent.");
    }

    private boolean processPayment(double amount, String method) {
        System.out.println("Processing payment of " + amount + " Pkr via " + method + "...");
        return true;
    }

    private void saveRentalToFile(Vehicle vehicle, int days, double totalCost, String paymentMethod) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RENTAL_FILE, true))) {
            writer.write("Rented: " + vehicle.getDetails() + " | Days: " + days + " | Cost: " + totalCost + " | Payment: " + paymentMethod);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving rental history.");
        }
    }

    private void saveVehiclesToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(VEHICLE_FILE))) {
            out.writeObject(availableVehicles);
        } catch (IOException e) {
            System.out.println("Error saving vehicle data.");
        }
    }

    @SuppressWarnings("unchecked")
    private void loadVehiclesFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(VEHICLE_FILE))) {
            availableVehicles = (List<Vehicle>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No existing vehicle data found.");
        }
    }

    public void displayAvailableVehicles() {
        if (availableVehicles.isEmpty()) {
            System.out.println("No vehicles available.");
        } else {
            for (Vehicle v : availableVehicles) {
                System.out.println(v.getDetails() + " | " + (v.isAvailable() ? "Available" : "Rented (Return: " + v.getReturnDate() + ")"));
            }
        }
    }
}

public class CarRentalSystem {
    public static void main(String[] args) {
        RentalManager rentManager = new RentalManager();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\nCar Rental System Menu:");
            System.out.println("1. Display Available Vehicles");
            System.out.println("2. Rent a Vehicle");
            System.out.println("3. Exit");
            System.out.print("Enter Your Choice: ");
            int userChoice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (userChoice) {
                case 1:
                    rentManager.displayAvailableVehicles();
                    break;
                case 2:
                    System.out.println("Enter Brand: ");
                    String rentBrand = sc.nextLine();
                    System.out.println("Enter Model: ");
                    String rentModel = sc.nextLine();
                    System.out.println("Enter Rental Days: ");
                    int rentDays = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter Payment Method: ");
                    String paymentMethod = sc.nextLine();
                    try {
                        rentManager.rentVehicle(rentBrand, rentModel, rentDays, paymentMethod);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 3:
                    System.exit(0);
            }
        }
    }
}
