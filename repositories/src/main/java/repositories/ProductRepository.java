package repositories;

import enums.Category;
import objects.store.Product;
import objects.store.ShoppingCart;
import objects.store.TopRatedSuggestion;
import org.springframework.stereotype.Repository;
import productcontext.IProductContext;
import productcontext.ProductContext;
import repository.GlobalRepository;

import java.util.List;

@Repository
public class ProductRepository extends GlobalRepository<Product> {

    public ProductRepository() {
        this.productContext = (IProductContext) getContext(new ProductContext(GlobalRepository.db_String), IProductContext.class, Product.class);
    }

    private IProductContext productContext;

    public List<Product> getByCategory(Category category) {
        return productContext.getByCategory(category);
    }

    public List<Product> getProductsByName(String name) {
        return productContext.getProductsByName(name);
    }

    public void removeBasketProductsFromStore(ShoppingCart shoppingCart) { productContext.deleteBasketProductsFromStore(shoppingCart); }

    public List<TopRatedSuggestion> getTopRatedSuggestions() {
        return productContext.getTopRatedSuggestions();
    }
}
