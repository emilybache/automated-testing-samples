package com.innoq.automatedtesting.samples.shoppingcart;

import org.approvaltests.Approvals;
import org.approvaltests.strings.Printable;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

public class ShoppingCartTest_Step1a_Approvals {

    @Test
    void add() throws Exception {
        var scenario = new PrintableScenario("Add Articles");

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

        scenario.startOfTest(
               new Printable<>(shoppingCart, ShoppingCartPrinter::print),
               new Printable<>(currentUser, UserPrinter::print)
        );

        shoppingCart.add(article1, 1);
        scenario.act("Add article");

        var article2 = mock(Article.class);
        when(stock.availableUnits(article2)).thenReturn(3);
        when(priceCalculator.calculatePrice(article2, CustomerStatus.GOLD)).thenReturn(BigDecimal.valueOf(7.5));
        when(shippingCalculator.calculateShipping(BigDecimal.valueOf(32.45))).thenReturn(BigDecimal.valueOf(3.5));

        shoppingCart.add(article2, 3);
        scenario.act("Add article");

        Approvals.verify(scenario.print());
    }

}
