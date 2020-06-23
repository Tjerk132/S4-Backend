package contexttests;

import enums.ReviewAuthorValue;
import objects.store.Product;
import objects.store.Review;
import org.junit.Assert;
import org.junit.Test;
import reviewcontext.IReviewContext;
import reviewcontext.ReviewContext;
import util.DbConnections;

import java.util.List;

public class ReviewContextTests {

    private IReviewContext context = new ReviewContext(DbConnections.inMemoryDB());

    @Test
    public void testProductReviewCount() {

        long count = context.getProductReviewCount(1);

        Assert.assertEquals(5, count);
    }

    @Test
    public void testGetByProductId() {
        List<Review> reviews = context.getByProductId(1);

        Assert.assertFalse(reviews.isEmpty());
        Assert.assertTrue(reviews.stream().allMatch(x -> x.getProductId() == 1));
    }

    @Test
    public void testGetProductAvgRating() {
        Product p = new Product();
        p.setId(1);

        double rating = context.getProductAvgRating(p);

        Assert.assertEquals(4.0, rating, 0);
    }

    @Test
    public void testGetByAuthor() {
        List<Review> reviews = context.getByAuthor("tjerk");

        Assert.assertEquals(7, reviews.size());
        Assert.assertTrue(reviews.stream().allMatch(x -> x.getAuthor().equals("tjerk")));
    }

    @Test
    public void testGetReviewAuthorValue() {
        ReviewAuthorValue value = context.getReviewAuthorValue("tjerk");

        Assert.assertEquals(ReviewAuthorValue.VALUED_REVIEWER, value);
    }
}
