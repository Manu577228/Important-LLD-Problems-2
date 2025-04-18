//5) Design a Ride Sharing System similar to Uber or Ola that handles :
//
//        * Onboarding Drivers and Riders
//
//        * Matching Riders to Nearby Drivers
//
//        * Trip Request, Start, and End
//
//        * Payment and Trip History

import java.util.*;

class Location {
    int x, y;

    Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    double distanceTo(Location other) {
        int dx = this.x - other.x;
        int dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}

abstract class User {
    String id, name;
    Location location;

    User(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    void updateLocation(Location location) {
        this.location = location;
    }
}

class Driver extends User {
    boolean isAvailable;

    Driver(String id, String name, Location location) {
        super(id, name, location);
        this.isAvailable = true;
    }
}

class Rider extends User {
    Rider(String id, String name, Location location) {
        super(id, name, location);
    }
}

class Ride {
    String id;
    Driver driver;
    Rider rider;
    String status;

    Ride(String id, Driver driver, Rider rider) {
        this.id = id;
        this.driver = driver;
        this.rider = rider;
        this.status = "Requested";
    }
}

public class RideSharingSystem {
    Map<String, Driver> drivers = new HashMap<>();
    Map<String, Rider> riders = new HashMap<>();
    Map<String, Ride> rides = new HashMap<>();

    public void registerDriver(String id, String name, Location location) {
        drivers.put(id, new Driver(id, name, location));
    }

    public void registerRider(String id, String name, Location location) {
        riders.put(id, new Rider(id, name, location));
    }

    public void updateDriverLocation(String driverId, Location location) {
        if (drivers.containsKey(driverId)) {
            drivers.get(driverId).updateLocation(location);
        }
    }

    public String requestRide(String riderId) {
        Rider rider = riders.get(riderId);
        Driver nearestDriver = null;
        double minDistance = Double.MAX_VALUE;

        for (Driver d : drivers.values()) {
            if (d.isAvailable) {
                double dist = d.location.distanceTo(rider.location);
                if (dist < minDistance) {
                    minDistance = dist;
                    nearestDriver = d;
                }
            }
        }

        if (nearestDriver == null) return null;

        nearestDriver.isAvailable = false;
        String rideId = UUID.randomUUID().toString();
        Ride ride = new Ride(rideId, nearestDriver, rider);
        rides.put(rideId, ride);
        return rideId;
    }

    public void startRide(String rideId) {
        if (rides.containsKey(rideId)) {
            rides.get(rideId).status = "Ongoing";
        }
    }

    public void endRide(String rideId) {
        if (rides.containsKey(rideId)) {
            Ride ride = rides.get(rideId);
            ride.status = "Completed";
            ride.driver.isAvailable = true;
        }
    }

    public void printRideStatus(String rideId) {
        if (rides.containsKey(rideId)) {
            Ride ride = rides.get(rideId);
            System.out.println("ride Status: " + ride.status);
        }
    }

    public boolean isDriverAvailable(String driverId) {
        return drivers.containsKey(driverId) && drivers.get(driverId).isAvailable;
    }

    public static void main(String[] args) {
        RideSharingSystem rss = new RideSharingSystem();

        rss.registerDriver("d1", "DriverOne", new Location(0, 0));
        rss.registerDriver("d2", "DriverTwo", new Location(5, 5));

        rss.registerRider("r1", "RiderOne", new Location(1, 1));

        String rideId = rss.requestRide("r1");

        System.out.println("Ride ID: " + rideId);

        rss.startRide(rideId);
        rss.printRideStatus(rideId);

        rss.endRide(rideId);
        rss.printRideStatus(rideId);

        System.out.println("Driver d1 available ? " + rss.isDriverAvailable("d1"));
    }
}
