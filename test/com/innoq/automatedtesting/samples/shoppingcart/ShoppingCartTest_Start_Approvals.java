package com.innoq.automatedtesting.samples.shoppingcart;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ShoppingCartTest_Start_Approvals {

    @Test
    void addArticles() throws Exception {
        var story = new ShoppingCartTestStory();

        var stock = mock(Stock.class);
        var article1 = mock(Article.class);
        when(stock.availableUnits(article1)).thenReturn(2);
        var currentUser = mock(CurrentUser.class);
        when(currentUser.customerStatus()).thenReturn(CustomerStatus.GOLD);
        var priceCalculator = mock(PriceCalculator.class);
        when(priceCalculator.calculatePrice(article1, CustomerStatus.GOLD)).thenReturn(BigDecimal.valueOf(9.95));
        var shippingCalculator = mock(ShippingCalculator.class);
        when(shippingCalculator.calculateShipping(BigDecimal.valueOf(9.95))).thenReturn(BigDecimal.valueOf(3.5));
        var shoppingCart = new ShoppingCart(stock, currentUser, priceCalculator, shippingCalculator);

        story.startOfTest(shoppingCart, currentUser);

        shoppingCart.add(article1, 1);
        story.act("Add article");

        var article2 = mock(Article.class);
        when(stock.availableUnits(article2)).thenReturn(3);
        when(priceCalculator.calculatePrice(article2, CustomerStatus.GOLD)).thenReturn(BigDecimal.valueOf(7.5));
        when(shippingCalculator.calculateShipping(BigDecimal.valueOf(32.45))).thenReturn(BigDecimal.valueOf(3.5));

        shoppingCart.add(article2, 3);
        story.act("Add article");

        Approvals.verify(story.fullStory());
    }

    public static class ShoppingCartTestStory {

        private static StringBuilder toVerify;
        private static ShoppingCartPrinter cartPrinter;
        private UserPrinter userPrinter;

        public ShoppingCartTestStory() {
            toVerify = new StringBuilder();
        }

        public void startOfTest(ShoppingCart shoppingCart, CurrentUser currentUser) {
            cartPrinter = new ShoppingCartPrinter(shoppingCart);
            userPrinter = new UserPrinter(currentUser);
            toVerify.append(userPrinter.printUser());
            toVerify.append(cartPrinter.print());
        }

        public void act(String action) {
            toVerify.append("---------\n");
            toVerify.append(action + "\n");
            toVerify.append("---------\n");
            toVerify.append(userPrinter.printUser());
            toVerify.append(cartPrinter.print());
        }

        public String fullStory()
        {
            return toVerify.toString();
        }
    }

}
