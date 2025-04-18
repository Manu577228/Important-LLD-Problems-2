//9. Design a Notification System that can send different types of notifications :
//
//* Email Notification
//
//* SMS Notification
//
//* Push Notification
//
//* You should be able to extend it in the future for more notification types without modifying the existing code.
//
//        ✅ Features to be Supported :
//
//* Add multiple notification channels (Email, SMS, Push).
//
//* Trigger notifications through a single interface.
//
//* Easily extensible to new types of notifications.
//
//* Respect the Open/Closed Principle (OCP from SOLID).

interface NotificationSender {
    void send(String to, String message);
}

class EmailSender implements NotificationSender {
    public void send(String to, String message) {
        System.out.println("Sending Email to " + to);
        System.out.println("Message: " + message);
    }
}

class SMSSender implements NotificationSender {
    public void send(String to, String message) {
        System.out.println("Sending SMS to " + to);
        System.out.println("Message: " + message);
    }
}

class PushSender implements NotificationSender {
    public void send(String to, String message) {
        System.out.println("Sending Push Notification to " + to);
        System.out.println("Message: " + message);
    }
}

public class NotificationSystem {
    private NotificationSender sender;

    public NotificationSystem(NotificationSender sender) {
        this.sender = sender;
    }

    public void notifyUser(String to, String message) {
        sender.send(to, message);
    }

    public static void main(String[] args) {
        NotificationSystem emailNotification = new NotificationSystem(new EmailSender());
        emailNotification.notifyUser("gowri@testmail.com", "Welcome to our platform!");

        NotificationSystem smsNotification = new NotificationSystem(new SMSSender());
        smsNotification.notifyUser("+91-1234567890", "Your OTP is 123456");

        NotificationSystem pushNotification = new NotificationSystem(new PushSender());
        pushNotification.notifyUser("userDevice123", "You have a new message!");
    }
}