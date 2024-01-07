package com.innoq.automatedtesting.samples.shoppingcart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class ShoppingCart {

    private final Stock stock;
    private final CurrentUser currentUser;
    private final PriceCalculator priceCalculator;
    private final ShippingCalculator shippingCalculator;

    private List<Item> items;
    private BigDecimal subtotalAmount;
    private BigDecimal shippingAmount;
    private BigDecimal totalAmount;

    public ShoppingCart(Stock stock, CurrentUser currentUser, PriceCalculator priceCalculator, ShippingCalculator shippingCalculator) {
        this.stock = stock;
        this.currentUser = currentUser;
        this.priceCalculator = priceCalculator;
        this.shippingCalculator = shippingCalculator;

        this.items = new ArrayList<>();
        this.subtotalAmount = BigDecimal.ZERO;
        this.shippingAmount = BigDecimal.ZERO;
        this.totalAmount = BigDecimal.ZERO;
    }

    public void add(Article article, int quantity) throws InsufficientUnitsInStockException {
        var availableUnits = stock.availableUnits(article);
        // Bug - reset availableUnits
        // availableUnits = 0;
        if (quantity > availableUnits)
            throw new InsufficientUnitsInStockException();
        var customerStatus = currentUser.customerStatus();
        var price = priceCalculator.calculatePrice(article, customerStatus);
        // Bug - use wrong customer status
        // price = priceCalculator.calculatePrice(article, CustomerStatus.PLATINUM);
        // Bug - use wrong price
        // price = BigDecimal.ONE;
        this.items.add(new Item(article, quantity, price));
        // Bug - add item twice
        //this.items.add(new Item(article, quantity, price));
        calculateTotals();
    }

    public void remove(Article article) {
        try {
            changeQuantity(article, 0);
        } catch (InsufficientUnitsInStockException e) {
            throw new IllegalStateException(e);
        }
        calculateTotals();
    }

    public void changeQuantity(Article article, int quantity) throws InsufficientUnitsInStockException {
        var item = items.stream().filter(i -> i.article.equals(article)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Article not in ShoppingCart"));
        if (quantity == 0)
            items.remove(item);
        else if (quantity > item.quantity && quantity > stock.availableUnits(article))
            throw new InsufficientUnitsInStockException();
        else
            item.changeQuantity(quantity);
        calculateTotals();
    }

    private void calculateTotals() {
        this.subtotalAmount = items.stream().map(Item::amount).reduce(BigDecimal.ZERO, BigDecimal::add);
        this.shippingAmount = shippingCalculator.calculateShipping(subtotalAmount);
        // Bug - reset shipping amount
        //this.shippingAmount = BigDecimal.ZERO;
        this.totalAmount = this.subtotalAmount.add(this.shippingAmount);
    }

    public BigDecimal subtotalAmount() {
        return subtotalAmount;
    }

    public BigDecimal shippingAmount() {
        return shippingAmount;
    }

    public BigDecimal totalAmount() {
        return totalAmount;
    }

    public List<Item> items() {
        return items;
    }

    public int numberOfItems() {
        return items.size();
    }

    public static class Item {
        private final Article article;
        private final BigDecimal price;
        private int quantity;
        private BigDecimal amount;

        public Item(Article article, int quantity, BigDecimal price) {
            this.article = article;
            this.quantity = quantity;
            this.price = price;
            this.amount = price.multiply(BigDecimal.valueOf(quantity));
        }

        public String articleDescription() {
            String description = STR."\{article.brand()} - \{article.name()} - \{article.color()} - \{article.size()}";
            // Bug - item description truncated
            // description = description.substring(0, 5);
            return description;
        }

        public int quantity() {
            return quantity;
        }

        public BigDecimal amount() {
            return amount;
        }

        public void changeQuantity(int quantity) {
            this.quantity = quantity;
            this.amount = price.multiply(BigDecimal.valueOf(quantity));
        }

        public Article article() {
            return article;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Item.class.getSimpleName() + "[", "]")
                    .add("article=" + article)
                    .toString();
        }
    }
}
