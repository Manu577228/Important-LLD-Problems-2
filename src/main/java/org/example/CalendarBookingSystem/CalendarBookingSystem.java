//10. Design a Calendar Booking System where users can:
//
//* Add a new booking (with start time and end time).
//
//* Check if a booking can be added without overlapping.
//
//* View existing bookings.

import java.util.*;

public class CalendarBookingSystem {
    private TreeSet<Meeting> meetings;

    public CalendarBookingSystem() {
        meetings = new TreeSet<>();
    }

    public boolean addMeeting(int start, int end) {
        if (start >= end) return false;

        Meeting newMeeting = new Meeting(start, end);

        Meeting floor = meetings.floor(newMeeting);

        if (floor != null && floor.end > start) return false;

        Meeting ceiling = meetings.ceiling(newMeeting);

        if (ceiling != null && end > ceiling.start) return false;

        meetings.add(newMeeting);
        return true;
    }

    public List<Meeting> getAllMeetings() {
        return new ArrayList<>(meetings);
    }

    private static class Meeting implements Comparable<Meeting> {
        int start, end;

        public Meeting(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public int compareTo(Meeting other) {
            return this.start - other.start;
        }

        public String toString() {
            return "[" + start + "," + end + "]";
        }
    }

    public static void main(String[] args) {
        CalendarBookingSystem cbs = new CalendarBookingSystem();

        System.out.println(cbs.addMeeting(10, 20));
        System.out.println(cbs.addMeeting(15, 25));
        System.out.println(cbs.addMeeting(20, 30));
        System.out.println(cbs.addMeeting(5, 10));
        System.out.println(cbs.addMeeting(8, 12));

        System.out.println("Scheduled Meetings:");
        for (Meeting m : cbs.getAllMeetings()) {
            System.out.println(m);
        }
    }
}

