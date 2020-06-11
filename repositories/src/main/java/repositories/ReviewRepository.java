package repositories;

import objects.store.Review;
import org.springframework.stereotype.Repository;
import repository.GlobalRepository;
import reviewcontext.ReviewContext;

import java.util.List;

@Repository
public class ReviewRepository extends GlobalRepository<Review> {

    public ReviewRepository() {
        super(new ReviewContext(GlobalRepository.DB_STRING));
        this.reviewContext = (ReviewContext) context;
    }

    private ReviewContext reviewContext;

    public List<Review> getByProductId(long id) {
        return reviewContext.getByProductId(id);
    }
}
