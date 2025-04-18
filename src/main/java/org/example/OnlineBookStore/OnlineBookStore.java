package org.example.OnlineBookStore;//2) Design a system like Amazon Books where users can search, view, purchase, review, and rate books online.
//
//📚 Subparts to Design
//
//* Book Catalog (search, filter, categories)
//
//* User Account (signup, login, user roles)
//
//* Shopping Cart and Checkout
//
//* Payment Integration
//
//* Order Management
//
//* Book Reviews & Ratings
//
//* Inventory Management
//
//* Admin Dashboard (add/update/delete books)
//
//* Wishlist and Recommendations (optional)

import java.util.*;

class Book {
    int id;
    String title;
    String author;
    double price;
    int stock;
    List<Review> reviews;

    Book(int id, String title, String author, double price, int stock) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.stock = stock;
        this.reviews = new ArrayList<>();
    }

    void addReview(Review r) {
        reviews.add(r);
    }
}

class Review {
    User user;
    int rating;
    String comment;

    Review(User user, int rating, String comment) {
        this.user = user;
        this.rating = rating;
        this.comment = comment;
    }
}

class User {
    int id;
    String name;
    String email;
    String password;
    ShoppingCart cart;
    List<Order> orders;

    User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.cart = new ShoppingCart();
        this.orders = new ArrayList<>();
    }
}

class ShoppingCart {
    Map<Book, Integer> items = new HashMap<>();

    void addBook(Book b, int quantity) {
        items.put(b, items.getOrDefault(b, 0) + quantity);
    }

    void removeBook(Book b) {
        items.remove(b);
    }

    double getTotal() {
        double total = 0;
        for (Map.Entry<Book, Integer> entry : items.entrySet()) {
            total += entry.getKey()
                    .price * entry.getValue();
        }
        return total;
    }
}

class Order {
    static int idCounter = 1;
    int orderId;
    List<Book> books;
    double amount;
    Date date;
    String status;

    Order(List<Book> books, double amount) {
        this.orderId = idCounter++;
        this.books = new ArrayList<>(books);
        this.amount = amount;
        this.date = new Date();
        this.status = "PLACED";
    }
}

class BookStore {
    Map<String, User> users = new HashMap<>();
    List<Book> inventory = new ArrayList<>();

    void registerUser(User u) {
        users.put(u.email, u);
    }

    User loginUser(String email, String password) {
        User u = users.get(email);
        if (u != null && u.password.equals(password)) return u;
        return null;
    }

    List<Book> searchByTitle(String title) {
        List<Book> result = new ArrayList<>();
        for (Book b : inventory) {
            if (b.title.toLowerCase().contains(title.toLowerCase())) {
                result.add(b);
            }
        }
        return result;
    }

    void addBookToInventory(Book b) {
        inventory.add(b);
    }

    void checkout(User u) {
        List<Book> purchasedBooks = new ArrayList<>();
        for (Map.Entry<Book, Integer> e : u.cart.items.entrySet()) {
            Book b = e.getKey();
            int q = e.getValue();
            if (b.stock >= q) {
                b.stock -= q;
                purchasedBooks.add(b);
            }
        }

        double total = u.cart.getTotal();
        Order o = new Order(purchasedBooks, total);
        u.orders.add(o);
        u.cart = new ShoppingCart();
    }
}

public class OnlineBookStore {
    public static void main(String[] args) {
        BookStore system = new BookStore();

        Book b1 = new Book(1, "Java Fundementals", "Bharadwaj", 499.0, 10);
        system.addBookToInventory(b1);

        User u1 = new User(101, "Gowri", "gowri@testmail.com", "Pass");
        system.registerUser(u1);

        User loginUser = system.loginUser("gowri@testmail.com", "Pass");
        if (loginUser != null) {
            loginUser.cart.addBook(b1, 1);
            system.checkout(loginUser);

            System.out.println("order placed! Total orders: " + loginUser.orders.size());
        } else {
            System.out.println("Login Failed.");
        }
    }
}