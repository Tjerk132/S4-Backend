package reviewcontext;

import com.j256.ormlite.stmt.SelectArg;
import context.Context;
import objects.ReviewLike;

import java.sql.SQLException;
import java.util.logging.Level;

public class ReviewLikeContext extends Context<ReviewLike> implements IReviewLikeContext {

    ReviewLikeContext(String connectionString) {
        super(ReviewLike.class, connectionString);
    }

    @Override
    public boolean alreadyLiked(int userId, int reviewId) {

        try {
            boolean liked =  dao.queryBuilder()
                    .where()
                    .eq("userId", new SelectArg(userId))
                    .and()
                    .eq("reviewId", new SelectArg(reviewId))
                    .countOf() != 0;

            if(!liked) {
                //add liked record if user didn't like the review before
                add(new ReviewLike(userId, reviewId));
            }
            return liked;
        }
        catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return true;
    }
}
