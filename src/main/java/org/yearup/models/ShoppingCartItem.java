package org.yearup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ShoppingCartItem
{
    private Product product;
    private int quantity = 1;
    private BigDecimal discountPercent = BigDecimal.ZERO; // as percent, e.g., 10 = 10%

    public ShoppingCartItem() { }

    public ShoppingCartItem(Product product, int quantity) {
        this.product = product;
        setQuantity(quantity);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 1) {
            this.quantity = 1;
        } else {
            this.quantity = quantity;
        }
    }

    public void incrementQuantity(int amount) {
        if (amount > 0) {
            this.quantity += amount;
        }
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        if (discountPercent == null || discountPercent.compareTo(BigDecimal.ZERO) < 0) {
            this.discountPercent = BigDecimal.ZERO;
        } else if (discountPercent.compareTo(BigDecimal.valueOf(100)) > 0) {
            this.discountPercent = BigDecimal.valueOf(100);
        } else {
            this.discountPercent = discountPercent;
        }
    }

    @JsonIgnore
    public int getProductId() {
        return product != null ? product.getProductId() : 0;
    }

    public BigDecimal getLineTotal() {
        if (product == null) return BigDecimal.ZERO;

        BigDecimal basePrice = product.getPrice();
        BigDecimal qty = BigDecimal.valueOf(quantity);

        BigDecimal subTotal = basePrice.multiply(qty);
        BigDecimal discountAmount = subTotal.multiply(discountPercent)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        return subTotal.subtract(discountAmount);
    }
}