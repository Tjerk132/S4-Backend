package reviewcontext;

import context.Context;
import objects.store.Product;
import objects.store.Review;
import util.ReviewCreator;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ReviewContext extends Context<Review> {

    public ReviewContext(String connectionString) {
        super(Review.class, connectionString, true);
        this.reviewCreator = new ReviewCreator(connectionString);
    }

    private ReviewCreator reviewCreator;

    @Override
    public Review getById(long id) {

        Review review = null;

        try {
            review = dao.queryForId(id);
            reviewCreator.createReview(review);
        }
        catch (SQLException e) {
            logger.warning(e.toString());
        }
        return review;
    }

    @Override
    public List<Review> getAll() {

        List<Review> reviews = new LinkedList<>();

        dao.forEach(review -> {
            reviewCreator.createReview(review);
            reviews.add(review);
        });

        return reviews;
    }

    @Override
    public void add(Review review) {

        if(review == null || review.getStarRating() < 1 || review.getStarRating() > 5) {
            return;
        }
        reviewCreator.setTimeStampToMillis(review);

        try {
            dao.create(review);

            int reviewId = review.getId();
            int productId = review.getProductId();

            reviewCreator.proConContextAddPros(review.getPros(), reviewId, productId);
            reviewCreator.proConContextAddCons(review.getCons(), reviewId, productId);
        }
        catch (Exception e) {
            logger.warning(e.toString());
        }
        finally {
            this.close();
        }
    }

    public List<Review> getByProductId(long id) {
        List<Review> reviews = null;
        try {
            reviews = dao.queryBuilder()
                    .where()
                    .eq("productId", id)
                    .query();

            //add pro's and con's to review
            for(Review r : reviews) {
                reviewCreator.createReviewByProduct(r);
            }

        }
        catch (SQLException e) {
            logger.warning(e.toString());

        }
        return reviews;
    }

    public long getProductReviewCount(long id) {
        long count = 0;
        try {
            count = dao.queryBuilder()
                .where()
                .eq("productId", id)
                .countOf();
        }
        catch (SQLException e) {
            logger.warning(e.toString());
        }
        return count;
    }

    public double getProductAvgRating(Product product) {

        List<Review> reviews = getByProductId(product.getId());

        //get total reviewStars of the product
        int totalRating = reviews.stream().mapToInt(Review::getStarRating).sum();

        //return avg reviewStars with 1 decimal if average is present
        return reviews.isEmpty() ? 0 : Math.round((totalRating / (double) reviews.size()) * 10.0) / 10.0;
    }

}
