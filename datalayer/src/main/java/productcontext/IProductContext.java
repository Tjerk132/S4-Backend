package productcontext;

import context.IContext;
import objects.store.Product;
import objects.store.ShoppingCart;
import objects.store.TopRatedSuggestion;

import java.util.List;

public interface IProductContext {

    List<Product> getByCategory(String category);

    List<Product> getProductsByName(String productName);

    void deleteBasketProductsFromStore(ShoppingCart shoppingCart);

    List<TopRatedSuggestion> getTopRatedSuggestions();
}
