package com.innoq.automatedtesting.samples.shoppingcart;

public class UserPrinter {
    public static String print(CurrentUser user) {
        return STR."Current user status: \{user.customerStatus()}\n";
    }
}
