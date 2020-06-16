package reviewcontext;

import objects.store.Product;
import objects.store.Review;

import java.util.List;

public interface IReviewContext {

    /**
     * get all reviews with the given product id.
     * @param id the product Id the reviews have to be retrieved with
     * @return the reviews that have the given product id
     */
    List<Review> getByProductId(long id);

    /**
     * get the number of reviews of the product with the given id.
     * @param id the product Id the review count has to be retrieved with
     * @return the number of reviews that have the given product id
     */
    long getProductReviewCount(long id);

    /**
     * get the average rating of the given product.
     * @param product the product of which the average rating has to be calculated for
     * @return the average rating of the product.
     */
    double getProductAvgRating(Product product);
}
