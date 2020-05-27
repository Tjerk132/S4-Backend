package repositories;

import objects.Product;
import objects.ShoppingCart;
import objects.TopRatedSuggestion;
import org.springframework.stereotype.Repository;
import productcontext.ProductContext;
import repository.GlobalRepository;

import java.util.List;

@Repository
public class ProductRepository extends GlobalRepository<Product> {

    public ProductRepository() {
        super(new ProductContext(GlobalRepository.DB_STRING));
        this.productContext = (ProductContext) context;
    }

    private ProductContext productContext;

    public List<Product> getByCategory(String category) {
        return productContext.getByCategory(category);
    }

    public List<Product> getProductsByName(String name) {
        return productContext.getProductsByName(name);
    }

    public void removeBasketProductsFromStore(ShoppingCart shoppingCart) { productContext.removeBasketProductsFromStore(shoppingCart); }

    public List<TopRatedSuggestion> getTopRatedSuggestions() {
        return productContext.getTopRatedSuggestions();
    }
}
