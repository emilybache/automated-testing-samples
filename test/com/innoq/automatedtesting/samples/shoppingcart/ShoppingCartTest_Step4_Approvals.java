package com.innoq.automatedtesting.samples.shoppingcart;

import org.approvaltests.Approvals;
import org.approvaltests.strings.Printable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartTest_Step4_Approvals {

    @Mock
    Stock stock;
    @Mock
    CurrentUser currentUser;
    @Mock
    PriceCalculator priceCalculator;
    @Mock
    ShippingCalculator shippingCalculator;

    @InjectMocks
    ShoppingCart shoppingCart;

    @Test
    public void should_calculate_subtotal_and_total_amount_if_two_items_with_different_prices_and_quantities_are_added() throws Exception {
        // given
        Article article1 = givenAnArticle().withPrice(9.95).availableInStock().andGetIt();
        Article article2 = givenAnArticle().withPrice(7.5).availableInStock().andGetIt();
        givenShippingAmount(3.5);

        // when
        shoppingCart.add(article1, 1);
        shoppingCart.add(article2, 3);

        // then
        Approvals.verify(new ShoppingCartPrinter(shoppingCart).print());
    }

    @Test
    public void two_items() throws Exception {
        var story = new PrintableStory("should calculate subtotal and total amount if two items with different prices and quantities are added");

        Article article1 = givenAnArticle().withPrice(9.95).availableInStock().andGetIt();
        Article article2 = givenAnArticle().withPrice(7.5).availableInStock().andGetIt();
        givenShippingAmount(3.5);
        story.given(
                new Printable<>(currentUser, UserPrinter::print),
                new Printable<>(shoppingCart, ShoppingCartPrinter::print)
        );

        shoppingCart.add(article1, 1);
        story.when("add article1");
        shoppingCart.add(article2, 3);
        story.when("add article2");

        Approvals.verify(story.then());
    }

    @BeforeEach
    void setupCurrentUser() {
        when(currentUser.customerStatus()).thenReturn(CustomerStatus.REGULAR);
    }

    private ArticleMocker givenAnArticle() {
        return new ArticleMocker(stock, priceCalculator);
    }

    private void givenShippingAmount(double amount) {
        when(shippingCalculator.calculateShipping(any(BigDecimal.class))).thenReturn(BigDecimal.valueOf(amount));
    }

}
