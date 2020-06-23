package repositories;

import objects.store.Review;
import org.springframework.stereotype.Repository;
import repository.GlobalRepository;
import reviewcontext.IReviewContext;
import reviewcontext.ReviewContext;

import java.util.List;

@Repository
public class ReviewRepository extends GlobalRepository<Review> {

    public ReviewRepository() {
        this.reviewContext = (IReviewContext) getContext(new ReviewContext(GlobalRepository.db_String), IReviewContext.class, Review.class);
    }

    private IReviewContext reviewContext;

    public List<Review> getByProductId(long id) {
        return reviewContext.getByProductId(id);
    }

    public Review likeReview(int userId, int reviewId) { return reviewContext.likeReview(userId, reviewId); }
}
