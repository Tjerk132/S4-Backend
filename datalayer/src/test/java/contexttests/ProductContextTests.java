package contexttests;

import enums.Category;
import objects.store.Product;
import objects.store.TopRatedSuggestion;
import org.junit.Assert;
import org.junit.Test;
import productcontext.IProductContext;
import productcontext.ProductContext;
import util.DbConnections;

import java.util.List;

public class ProductContextTests {

    private IProductContext context = new ProductContext(DbConnections.inMemoryDB());

    @Test
    public void testGetByCategory() {
        List<Product> products = context.getByCategory(Category.ELECTRONICS);

        Assert.assertTrue(products.stream().allMatch(x -> x.getCategory() == Category.ELECTRONICS));
    }

    @Test
    public void testGetByName() {
        List<Product> products = context.getProductsByName("ball");

        Assert.assertTrue(products.stream().allMatch(x -> x.getName().contains("ball")));
    }

    @Test
    public void testGetTopRatedSuggestions() {
        List<TopRatedSuggestion> topRatedSuggestions = context.getTopRatedSuggestions();

        Assert.assertEquals(5, topRatedSuggestions.size());
        Assert.assertTrue(topRatedSuggestions.stream().noneMatch(x -> x.getAvgRating() < 3.0));
    }
}
