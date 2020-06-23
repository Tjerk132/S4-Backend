package reviewcontext;

import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.UpdateBuilder;
import context.Context;
import enums.ReviewAuthorValue;
import objects.store.Product;
import objects.store.Review;
import util.ReviewCreator;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

public class ReviewContext extends Context<Review> implements IReviewContext {

    public ReviewContext(String connectionString) {
        super(Review.class, connectionString);
        this.reviewCreator = new ReviewCreator(connectionString);
        this.reviewLikeContext = new ReviewLikeContext(connectionString);
    }

    private IReviewLikeContext reviewLikeContext;

    private ReviewCreator reviewCreator;

    @Override
    public Review getById(long id) {

        Review review = null;

        try {
            review = dao.queryForId(id);
            reviewCreator.createReview(review);
        }
        catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
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
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        finally {
            this.close();
        }
    }

    @Override
    public List<Review> getByProductId(long id) {

        List<Review> reviews = null;
        try {
            reviews = dao.queryBuilder()
                    .where()
                    .eq("productId", new SelectArg(id))
                    .query();

            //add pro's and con's to review
            for(Review r : reviews) {
                reviewCreator.createReviewByProduct(r);
                r.setReviewAuthorValue(getReviewAuthorValue(r.getAuthor()));
            }

        }
        catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return reviews;
    }

    @Override
    public List<Review> getByAuthor(String author) {

        List<Review> reviews = null;
        try {
            reviews = dao.queryBuilder()
                    .where()
                    .eq("author", new SelectArg(author))
                    .query();

            //add pro's and con's to review
            for(Review r : reviews) {
                reviewCreator.createReviewByProduct(r);
            }

        }
        catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return reviews;
    }

    @Override
    public long getProductReviewCount(long id) {
        long count = 0;
        try {
            count = dao.queryBuilder()
                .where()
                .eq("productId", new SelectArg(id))
                .countOf();
        }
        catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return count;
    }

    @Override
    public double getProductAvgRating(Product product) {

        List<Review> reviews = getByProductId(product.getId());

        //get total reviewStars of the product
        int totalRating = reviews.stream().mapToInt(Review::getStarRating).sum();

        //return avg reviewStars with 1 decimal if average is present
        return reviews.isEmpty() ? 0 : Math.round((totalRating / (double) reviews.size()) * 10.0) / 10.0;
    }

    @Override
    public ReviewAuthorValue getReviewAuthorValue(String author) {

        List<Review> userReviews = getByAuthor(author);

        int liked = userReviews.stream().mapToInt(Review::getLiked).sum();

        if(liked > 10) {
            return ReviewAuthorValue.VALUED_REVIEWER;
        }
        else return ReviewAuthorValue.NORMAL_REVIEWER;
    }

    @Override
    public Review likeReview(int userId, int reviewId) {

        try {

            boolean alreadyLiked = reviewLikeContext.alreadyLiked(userId, reviewId);

            if(!alreadyLiked) {
                //update like count of review
                Review r = getById(reviewId);

                UpdateBuilder updateBuilder = dao.updateBuilder();

                updateBuilder
                        .where()
                        .eq("id", new SelectArg(reviewId));

                updateBuilder
                        .updateColumnValue("liked", new SelectArg(r.getLiked() + 1))
                        .update();
            }

        }
        catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return getById(reviewId);
    }
}
