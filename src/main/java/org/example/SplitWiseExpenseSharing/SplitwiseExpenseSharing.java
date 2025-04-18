package org.example.SplitWiseExpenseSharing;//3) Design a Splitwise-like Expense Sharing System, where:
//
//* Users can add expenses involving multiple users.
//
//* The system should track who owes how much to whom.
//
//* Must support different expense types: Equal, Exact (extendable to Percent).
//
//* Must allow users to view balances with others.

import java.util.*;

public class SplitwiseExpenseSharing {
    static enum ExpenseType {
        EQUAL,
        EXACT,
        PERCENT
    }

    static class User {
        String id;
        String name;

        User(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    static class Split {
        User user;
        double amount;

        Split(User user, double amount) {
            this.user = user;
            this.amount = amount;
        }
    }

    static class Expense {
        User paidBy;
        double amount;
        List<Split> splits;
        ExpenseType expenseType;


        Expense(User paidBy, double amount, List<Split> splits, ExpenseType expenseType) {
            this.paidBy = paidBy;
            this.amount = amount;
            this.splits = splits;
            this.expenseType = expenseType;
        }
    }

    static class ExpenseService {
        Map<String, Map<String, Double>> balanceSheet = new HashMap<>();

        void addExpense(ExpenseType type, double amount, User paidBy, List<Split> splits) {
            if (type == ExpenseType.EQUAL) {
                double splitAmount = amount / splits.size();
                for (Split split : splits) {
                    split.amount = splitAmount;
                }
            }

            for (Split split : splits) {
                if (split.user.id.equals(paidBy.id)) continue;

                balanceSheet.putIfAbsent(split.user.id, new HashMap<>());
                balanceSheet.get(split.user.id).put(paidBy.id, balanceSheet.get(split.user.id).getOrDefault(paidBy.id, 0.0) + split.amount);

                balanceSheet.putIfAbsent(paidBy.id, new HashMap<>());
                balanceSheet.get(paidBy.id).put(split.user.id, balanceSheet.get(paidBy.id).getOrDefault(split.user.id, 0.0) - split.amount);
            }
        }

        void showBalances(Map<String, User> userMap) {
            boolean isEmpty = true;

            for (String user1 : balanceSheet.keySet()) {
                for (String user2 : balanceSheet.get(user1).keySet()) {
                    double amount = balanceSheet.get(user1).get(user2);
                    if (amount > 0) {
                        isEmpty = false;
                        System.out.println(userMap.get(user1).name + " owes " + userMap.get(user2).name + ": " + String.format("%.2f", amount));
                    }
                }
            }
            if (isEmpty) {
                System.out.println("No Balance");
            }
        }
    }

    public static void main(String[] args) {
        User u1 = new User("u1", "Bharadwaj1");
        User u2 = new User("u2", "Bharadwaj2");
        User u3 = new User("u3", "Bharadwaj3");
        User u4 = new User("u4", "Bharadwaj4");

        Map<String, User> userMap = new HashMap<>();
        userMap.put(u1.id, u1);
        userMap.put(u2.id, u2);
        userMap.put(u3.id, u3);
        userMap.put(u4.id, u4);

        ExpenseService expenseService = new ExpenseService();

        List<Split> splits1 = Arrays.asList(
                new Split(u1, 0),
                new Split(u2, 0),
                new Split(u3, 0),
                new Split(u4, 0)
        );
        expenseService.addExpense(ExpenseType.EQUAL, 120, u1, splits1);

        List<Split> splits2 = Arrays.asList(
                new Split(u2, 0),
                new Split(u3, 0)
        );
        expenseService.addExpense(ExpenseType.EQUAL, 80, u2, splits2);

        expenseService.showBalances(userMap);
    }
}