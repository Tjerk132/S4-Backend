package objects;

import java.util.List;

public class ShoppingCart {

    private List<CartItem> products;

    public ShoppingCart(List<CartItem> products) {
        this.products = products;
    }

    public List<CartItem> getProducts() {
        return products;
    }

}
