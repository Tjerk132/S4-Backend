package reviewcontext;

import objects.store.Product;
import objects.store.Review;

import java.util.List;

public interface IReviewContext {

    List<Review> getByProductId(long id);

    long getProductReviewCount(long id);

    double getProductAvgRating(Product product);
}
