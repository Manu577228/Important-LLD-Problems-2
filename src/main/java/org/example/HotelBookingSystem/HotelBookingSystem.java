//4. Design a Hotel Booking System similar to OYO or Booking.com that:
//
//* Allows hotels to register and list their rooms.
//
//* Users can search for hotels by city, check-in, check-out dates.
//
//* Users can book available rooms.
//
//* Shows room availability in real-time.
//
//* Cancels bookings and releases rooms.

import java.util.*;
import java.time.*;

public class HotelBookingSystem {
    enum RoomType {
        SINGLE, DOUBLE, SUITE
    }

    static class User {
        String id;
        String name;
        String email;

        User(String id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }
    }

    static class Room {
        String id;
        RoomType roomType;
        double price;
        Map<LocalDate, Boolean> isBooked;

        Room(String id, RoomType roomType, double price) {
            this.id = id;
            this.roomType = roomType;
            this.price = price;
            this.isBooked = new HashMap<>();
        }

        boolean isAvailable(LocalDate in, LocalDate out) {
            for (LocalDate date = in; date.isBefore(out); date = date.plusDays(1)) {
                if (isBooked.getOrDefault(date, false)) return false;
            }
            return true;
        }

        void book(LocalDate in, LocalDate out) {
            for (LocalDate date = in; date.isBefore(out); date = date.plusDays(1)) {
                isBooked.put(date, true);
            }
        }

        void cancel(LocalDate in, LocalDate out) {
            for (LocalDate date = in; date.isBefore(out); date = date.plusDays(1)) {
                isBooked.remove(date);
            }
        }
    }

    static class Hotel {
        String id;
        String name;
        String location;
        List<Room> rooms;

        Hotel(String id, String name, String location) {
            this.id = id;
            this.name = name;
            this.location = location;
            this.rooms = new ArrayList<>();
        }

        void addRoom(Room room) {
            rooms.add(room);
        }
    }

    static class Booking {
        String id;
        User user;
        Room room;
        LocalDate checkIn;
        LocalDate checkOut;

        Booking(String id, User user, Room room, LocalDate checkIn, LocalDate checkOut) {
            this.id = id;
            this.user = user;
            this.room = room;
            this.checkIn = checkIn;
            this.checkOut = checkOut;
        }

        void confirm() {
            room.book(checkIn, checkOut);
        }

        void cancel() {
            room.cancel(checkIn, checkOut);
        }
    }

    static class BookingSystem {
        List<Hotel> hotels = new ArrayList<>();

        void addHotel(Hotel hotel) {
            hotels.add(hotel);
        }

        List<Room> search(String city, LocalDate checkIn, LocalDate checkOut) {
            List<Room> availableRooms = new ArrayList<>();
            for (Hotel h : hotels) {
                if (!h.location.equalsIgnoreCase(city)) continue;
                for (Room r : h.rooms) {
                    if (r.isAvailable(checkIn, checkOut)) {
                        availableRooms.add(r);
                    }
                }
            }
            return availableRooms;
        }

        Booking book(User user, Room room, LocalDate checkIn, LocalDate checkOut) {
            if (room.isAvailable(checkIn, checkOut)) {
                String bookingId = UUID.randomUUID().toString();
                Booking b = new Booking(bookingId, user, room, checkIn, checkOut);
                b.confirm();
                return b;
            }
            return null;
        }
    }

    public static void main(String[] args) {
        BookingSystem sys = new BookingSystem();

        Hotel h1 = new Hotel("H1", "The Bharadwaj","Bengaluru");
        Room r1 = new Room("R1", RoomType.SINGLE, 1000);
        Room r2 = new Room("R2", RoomType.DOUBLE, 1500);

        h1.addRoom(r1);
        h1.addRoom(r2);
        sys.addHotel(h1);

        User u1 = new User("U1", "Gowri", "Gowri@testmail.com");
        LocalDate checkIn = LocalDate.of(2025, 3, 20);
        LocalDate checkOut = LocalDate.of(2025, 3, 23);

        List<Room> rooms = sys.search("Bengaluru", checkIn, checkOut);
        System.out.println("Available Rooms: " + rooms.size());

        if (!rooms.isEmpty()) {
            Booking b = sys.book(u1, rooms.get(0), checkIn, checkOut);
            if (b != null) {
                System.out.println("Room Booked ! Booking ID : " + b.id);
            } else {
                System.out.println("Room is no longer available. ");
            }
        }
    }
}

