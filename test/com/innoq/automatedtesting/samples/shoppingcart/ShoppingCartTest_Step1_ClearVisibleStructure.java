package com.innoq.automatedtesting.samples.shoppingcart;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Disabled("to reduce distractions during demo")
public class ShoppingCartTest_Step1_ClearVisibleStructure {

    @Test
    public void addTwoArticles() throws InsufficientUnitsInStockException {
        // arrange
        var article1 = mock(Article.class);
        var article2 = mock(Article.class);

        var stock = mock(Stock.class);
        when(stock.availableUnits(article1)).thenReturn(2);
        when(stock.availableUnits(article2)).thenReturn(3);

        var currentUser = mock(CurrentUser.class);
        when(currentUser.customerStatus()).thenReturn(CustomerStatus.GOLD);

        var priceCalculator = mock(PriceCalculator.class);
        when(priceCalculator.calculatePrice(article1, CustomerStatus.GOLD)).thenReturn(BigDecimal.valueOf(9.95));
        when(priceCalculator.calculatePrice(article2, CustomerStatus.GOLD)).thenReturn(BigDecimal.valueOf(7.5));

        var shippingCalculator = mock(ShippingCalculator.class);
        when(shippingCalculator.calculateShipping(BigDecimal.valueOf(9.95))).thenReturn(BigDecimal.valueOf(3.5));
        when(shippingCalculator.calculateShipping(BigDecimal.valueOf(32.45))).thenReturn(BigDecimal.valueOf(3.5));

        var shoppingCart = new ShoppingCart(stock, currentUser, priceCalculator, shippingCalculator);

        // act
        shoppingCart.add(article1, 1);
        shoppingCart.add(article2, 3);

        // assert
        assertEquals(2, shoppingCart.items().size());

        assertEquals(1, shoppingCart.items().get(0).quantity());
        assertEquals(BigDecimal.valueOf(9.95), shoppingCart.items().get(0).amount());

        assertEquals(3, shoppingCart.items().get(1).quantity());
        assertEquals(BigDecimal.valueOf(22.50), shoppingCart.items().get(1).amount());

        assertEquals(BigDecimal.valueOf(32.45), shoppingCart.subtotalAmount());
        assertEquals(BigDecimal.valueOf(3.5), shoppingCart.shippingAmount());
        assertEquals(BigDecimal.valueOf(35.95), shoppingCart.totalAmount());
    }

}
