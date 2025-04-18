package org.example.ParkingLot;//1. Design a Parking Lot System
//        Design a Parking Lot System where vehicles of different sizes (bike, car, truck) can be parked. The system should manage entry, exit, parking slots, and display availability.
//
//        📌 Subparts:
//
//        What are the vehicle types and how do we accommodate them?
//
//        How are parking slots assigned (First Available / Nearest / Priority)?
//
//        How do we calculate parking fees (per hour, fixed rate, etc.)?
//
//        How to handle multiple floors or sections in the lot?
//
//        Should we allow pre-booking of slots?
//
//        How to manage entry/exit gates and issue parking tickets?

import java.util.*;

enum VehicleType {
    BIKE, CAR, TRUCK
}

class Vehicle {
    String number;
    VehicleType type;

    Vehicle(String number, VehicleType type) {
        this.number = number;
        this.type = type;
    }
}

class ParkingSlot {
    int id;
    VehicleType type;
    boolean isOccupied;
    Vehicle vehicle;

    ParkingSlot(int id, VehicleType type) {
        this.id = id;
        this.type = type;
        this.isOccupied = false;
    }

    boolean canFitVehicle(Vehicle vehicle) {
        return this.type == vehicle.type && !isOccupied;
    }

    void park(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.isOccupied = true;
    }

    void unpark() {
        this.vehicle = null;
        this.isOccupied = false;
    }
}

class ParkingFloor {
    int floorNo;
    List<ParkingSlot> slots;

    ParkingFloor(int floorNo, int bikeSlots, int carSlots, int truckSlots) {
        this.floorNo = floorNo;
        this.slots = new ArrayList<>();

        int id = 1;

        for (int i = 0; i < bikeSlots; i++) {
            slots.add(new ParkingSlot(id++, VehicleType.BIKE));
        }

        for (int i = 0; i < carSlots; i++) {
            slots.add(new ParkingSlot(id++, VehicleType.CAR));
        }

        for (int i = 0; i < truckSlots; i++) {
            slots.add(new ParkingSlot(id++, VehicleType.TRUCK));
        }
    }

    ParkingSlot assignSlot(Vehicle v) {
        for (ParkingSlot slot : slots) {
            if (slot.canFitVehicle((v))) {
                slot.park(v);
                return slot;
            }
        }
        return null;
    }

    boolean unpark(String vehicleNo) {
        for (ParkingSlot slot : slots) {
            if (slot.isOccupied && slot.vehicle.number.equals((vehicleNo))) {
                slot.unpark();
                return true;
            }
        }
        return false;
    }

    void printStatus() {
        for (ParkingSlot slot : slots) {
            System.out.println(
                    "SLot ID: " + slot.id +
                            ", Type: " + slot.type +
                            ", Occupied: " + slot.isOccupied
            );
        }
    }
}

public class ParkingLot {
    List<ParkingFloor> floors;

    public ParkingLot(int floorCount, int bikePerFloor, int carPerFloor, int truckPerFloor) {
        this.floors = new ArrayList<>();

        for (int i = 0; i < floorCount; i++) {
            floors.add(new ParkingFloor(i + 1, bikePerFloor, carPerFloor, truckPerFloor));
        }
    }

    public boolean parkVehicle(Vehicle v) {
        for (ParkingFloor floor : floors) {
            ParkingSlot s = floor.assignSlot(v);
            if (s != null) {
                System.out.println("Vehicle Parked at floor " + floor.floorNo + ", Slot " + s.id);
                return true;
            }
        }

        System.out.println("No available slot for the vehicle type" + v.type);
        return false;
    }

    public boolean unparkVehicle(String vehicleNo) {
        for (ParkingFloor floor : floors) {
            if (floor.unpark(vehicleNo)) {
                System.out.println("Vehicle unparked from the floor " + floor.floorNo);
                return true;
            }
        }
        System.out.println("vehicle not found");
        return false;
    }

    public void getStatus() {
        for (ParkingFloor floor : floors) {
            System.out.println("Floor: " + floor.floorNo);
            floor.printStatus();
        }
    }

    public static void main(String[] args) {
        ParkingLot p = new ParkingLot(2, 2, 2, 1);

        p.parkVehicle(new Vehicle("KA-01-BI-1234", VehicleType.BIKE));
        p.parkVehicle(new Vehicle("KA-02-CA-9999", VehicleType.CAR));
        p.parkVehicle(new Vehicle("KA-03-IT-1234", VehicleType.TRUCK));

        p.getStatus();

        p.unparkVehicle("KA-01-BI-1234");

        p.getStatus();
    }
}


