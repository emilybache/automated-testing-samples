package com.innoq.automatedtesting.samples.shoppingcart;

import java.util.stream.Collectors;

public class ShoppingCartPrinter {

    private final ShoppingCart cart;

    public ShoppingCartPrinter(ShoppingCart cart) {
        this.cart = cart;
    }

    public String print() {
        String articles = cart.items()
                .stream()
                .map(item -> STR."        Article (quantity: \{item.quantity()}, price: \{item.amount()})")
                .collect(Collectors.joining("\n"));
        String result = STR."""
                ShoppingCart:
                    Articles:
                \{articles}
                    Subtotal:\t\{cart.subtotalAmount()}
                    Shipping:\t\{cart.shippingAmount()}
                    Total:\t\{cart.totalAmount()}

                """;
        return result;
    }
}
