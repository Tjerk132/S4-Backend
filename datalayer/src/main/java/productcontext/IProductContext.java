package productcontext;

import enums.Category;
import objects.store.Product;
import objects.store.ShoppingCart;
import objects.store.TopRatedSuggestion;

import java.util.List;

/**
 * Interface provided for the ProductContext with
 * documentation for the interface itself.
 *
 * @author Tjerk Sevenich
 */
public interface IProductContext {

    /**
     * get all products from the database with the given category.
     * @param category identifier for products to be searched by
     * @return products that have been found with the given category
     */
    List<Product> getByCategory(Category category);

    /**
     * get all products from the database with the given name.
     * @param productName identifier for products to be searched by
     * @return products that have been found with their name containing the given name
     */
    List<Product> getProductsByName(String productName);

    /**
     * lower the stockCount of all products from the shoppingCart accordingly.
     * @param shoppingCart containing the products with the quantity that needs to be lowered
     */
    void deleteBasketProductsFromStore(ShoppingCart shoppingCart);

    /**
     * get the top rated products from the database.
     * @return the top rated products from the store with their average rating.
     */
    List<TopRatedSuggestion> getTopRatedSuggestions();
}
