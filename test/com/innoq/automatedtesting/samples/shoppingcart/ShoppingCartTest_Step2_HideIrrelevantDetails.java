package com.innoq.automatedtesting.samples.shoppingcart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * - hide irrelevant details
 * - focus on behavior
 */
@ExtendWith(MockitoExtension.class)
public class ShoppingCartTest_Step2_HideIrrelevantDetails {

    @Mock Stock stock;
    @Mock CurrentUser currentUser;
    @Mock PriceCalculator priceCalculator;
    @Mock ShippingCalculator shippingCalculator;

    @InjectMocks ShoppingCart shoppingCart;


    @Test
    public void add() throws Exception {
        // arrange
        var article1 = mock(Article.class);
        var article2 = mock(Article.class);
        setupAvailableInStock(article1, article2);
        setupCalculatedPrice(article1, 9.95);
        setupCalculatedPrice(article2, 7.5);
        setupShippingAmount(3.5);

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

    @BeforeEach
    void setupCurrentUser() {
        when(currentUser.customerStatus()).thenReturn(CustomerStatus.REGULAR);
    }

    private void setupAvailableInStock(Article... articles) {
        Arrays.stream(articles).forEach(article -> when(stock.availableUnits(article)).thenReturn(9999));
    }

    private void setupCalculatedPrice(Article article, double price) {
        when(priceCalculator.calculatePrice(eq(article), any(CustomerStatus.class))).thenReturn(BigDecimal.valueOf(price));
    }

    private void setupShippingAmount(double amount) {
        when(shippingCalculator.calculateShipping(any(BigDecimal.class))).thenReturn(BigDecimal.valueOf(amount));
    }

}
