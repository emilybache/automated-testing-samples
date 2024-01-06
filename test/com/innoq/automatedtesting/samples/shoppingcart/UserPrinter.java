package com.innoq.automatedtesting.samples.shoppingcart;

public class UserPrinter {

    private static CurrentUser currentUser;

    public UserPrinter(CurrentUser user) {
        currentUser = user;
    }

    public static String printUser() {
        return "Current user status: " + currentUser.customerStatus() + "\n";
    }
}
