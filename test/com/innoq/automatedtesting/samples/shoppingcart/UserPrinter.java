package com.innoq.automatedtesting.samples.shoppingcart;

public class UserPrinter {

    private CurrentUser currentUser;

    public UserPrinter(CurrentUser user) {
        currentUser = user;
    }

    public String print() {
        return print(this.currentUser);
    }
    public static String print(CurrentUser user) {
        return STR."Current user status: \{user.customerStatus()}\n";
    }
}
