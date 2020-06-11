package nl.fhict.s4.restserver.controllers;

import enums.Category;
import nl.fhict.s4.restserver.config.exceptions.CustomNotFoundException;
import objects.Product;
import objects.ShoppingCart;
import objects.TopRatedSuggestion;
import org.springframework.web.bind.annotation.*;
import repositories.ProductRepository;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    public ProductController() {
        repository = new ProductRepository();
    }

    private ProductRepository repository;

    @GetMapping(produces = MediaType.APPLICATION_JSON)
    public List<Product> allProducts(){
        return repository.getAll();
    }

    @GetMapping(path = "{id}",
    produces = MediaType.APPLICATION_JSON)
    public Product getProduct(@PathVariable Integer id) {
        Product p = repository.getById(id);
        if(p != null) {
            return p;
        }
        else throw new CustomNotFoundException(Product.class.getSimpleName(), id);
    }

    @PostMapping(path = "add",
    consumes = MediaType.APPLICATION_JSON,
    produces = MediaType.APPLICATION_JSON)
    public Product addProduct(@RequestBody Product product) {
        repository.add(product);
        return product;
    }

    @DeleteMapping(path = "delete",
    consumes = MediaType.APPLICATION_JSON,
    produces = MediaType.APPLICATION_JSON)
    public Product deleteProduct(@RequestBody Product product) {
        repository.delete(product);
        return product;
    }

    @GetMapping(path = "categories",
    produces = MediaType.APPLICATION_JSON)
    public Category[] getCategories() {
        return Category.values();
    }

    @GetMapping(path = "categories/{category}",
    produces = MediaType.APPLICATION_JSON)
    public List<Product> getByCategory(@PathVariable String category) {
        return repository.getByCategory(category);
    }

    @GetMapping(path = "name/{name}",
    produces = MediaType.APPLICATION_JSON)
    public List<Product> getByName(@PathVariable String name) {
        return repository.getProductsByName(name);
    }

    @PostMapping(path = "removeBasketItems",
    consumes = MediaType.APPLICATION_JSON,
    produces = MediaType.APPLICATION_JSON)
    public ShoppingCart removeBasketItems(@RequestBody ShoppingCart shoppingCart) {
        repository.removeBasketProductsFromStore(shoppingCart);
        return shoppingCart;
    }

    @GetMapping(path = "topRated",
    produces = MediaType.APPLICATION_JSON)
    public List<TopRatedSuggestion>  getTopRated() {
        return repository.getTopRatedSuggestions();
    }

}
