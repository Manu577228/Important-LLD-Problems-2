//8. We need to design a scalable Online Movie Ticket Booking System where users can:
//
//        * Browse Movies, Theaters, and ShowTimings.
//
//        * Select Seats, Book Tickets, Cancel Bookings.
//
//        * The system should also handle seat-locking, payments, and booking confirmation.

import java.util.*;

enum SeatType {
    REGULAR, PREMIUM, RECLINER
}

enum BookingStatus {
    PENDING, CONFIRMED, CANCELLED
}

class Seat {
    String seatNumber;
    SeatType type;
    boolean isAvailable;

    Seat(String seatNumber, SeatType type) {
        this.seatNumber = seatNumber;
        this.type = type;
        this.isAvailable = true;
    }
}

class Screen {
    int screenId;
    List<Seat> seats;

    Screen(int screenId, int totalSeats) {
        this.screenId = screenId;
        this.seats = new ArrayList<>();
        for (int i = 1; i <= totalSeats; i++) {
            seats.add(new Seat("S" + i, SeatType.REGULAR));
        }
    }
}

class Theater {
    String name;
    String address;
    List<Screen> screens;

    Theater(String name, String address) {
        this.name = name;
        this.address = address;
        this.screens = new ArrayList<>();
    }

    void addScreen(Screen screen) {
        screens.add(screen);
    }
}

class City {
    String name;
    List<Theater> theaters;

    City(String name) {
        this.name = name;
        this.theaters = new ArrayList<>();
    }

    void addTheater(Theater t) {
        theaters.add(t);
    }
}

class Movie {
    String name;
    int duration;

    Movie(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }
}

class Show {
    Movie movie;
    Screen screen;
    Date startTime;
    Map<String, Boolean> seatAvailability;

    Show(Movie movie, Screen screen, Date startTime) {
        this.movie = movie;
        this.screen = screen;
        this.startTime = startTime;
        this.seatAvailability = new HashMap<>();

        for (Seat s : screen.seats) {
            seatAvailability.put(s.seatNumber, true);
        }
    }

    boolean isSeatAvailable(String seatNumber) {
        return seatAvailability.getOrDefault(seatNumber, false);
    }

    void bookSeat(String seatNumber) {
        seatAvailability.put(seatNumber, false);
    }

    void releaseSeat(String seatNumber) {
        seatAvailability.put(seatNumber, true);
    }
}

class PersonAccount {
    int id;
    String name;
    String email;

    PersonAccount(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}

class Booking {
    int bookingId;
    Show show;
    List<String> seatNumbers;
    BookingStatus status;

    Booking(int bookingId, Show show, List<String> seatNumbers) {
        this.bookingId = bookingId;
        this.show = show;
        this.seatNumbers = seatNumbers;
        this.status = BookingStatus.PENDING;
    }

    void confirmBooking() {
        this.status = BookingStatus.CONFIRMED;
    }

    void cancelBooking() {
        this.status = BookingStatus.CANCELLED;
        for (String s : seatNumbers) {
            show.releaseSeat(s);
        }
    }
}

public class OnlineMovieTicketBookingSystem {
    public static void main(String[] args) {
        City city = new City("Bengaluru");

        Theater pvr = new Theater("PVR Cinemas", "Basavangudi");
        Screen screen1 = new Screen(1, 10);
        pvr.addScreen(screen1);
        city.addTheater(pvr);

        Movie movie = new Movie("Avengers", 180);

        Show morningShow = new Show(movie, screen1, new Date());

        PersonAccount customer = new PersonAccount(1, "Gowri", "gowri@testmail.com");

        List<String> selectedSeats = Arrays.asList("S1", "S2");

        boolean allAvailable = true;
        for (String s : selectedSeats) {
            if (!morningShow.isSeatAvailable(s)) {
                allAvailable = false;
                break;
            }
        }

        if (allAvailable) {
            for (String s : selectedSeats) {
                morningShow.bookSeat(s);
            }

            Booking booking = new Booking(101, morningShow, selectedSeats);
            booking.confirmBooking();

            System.out.println("Booking Confirmed for " + customer.name + "!");
            System.out.println("Seats Booked: " + selectedSeats);
            System.out.println("Movie: " + movie.name + " | Theater: " + pvr.name);
        } else {
            System.out.println("Some selected seats are not available");
        }
    }
}
