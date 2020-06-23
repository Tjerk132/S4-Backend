package reviewcontext;

import enums.ReviewAuthorValue;
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
     * get all reviews with the given author's name.
     * @param author the author's name the reviews have to be retrieved with
     * @return the reviews that have the given author
     */
    List<Review> getByAuthor(String author);

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

    /**
     * get the author value for a review.
     * @param author the author of the review of which the author value has to be calculated for
     * @return the author value of the review.
     */
    ReviewAuthorValue getReviewAuthorValue(String author);

    /**
     * update the review liked count.
     *
     * @param userId id of the user that submits the like
     * @param reviewId id of the review of which the liked count has to be updated for
     * @return the review with updated like count
     */
    Review likeReview(int userId, int reviewId);
}
