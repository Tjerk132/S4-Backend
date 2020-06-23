package reviewcontext;

import context.IContext;

public interface IReviewLikeContext {

    /**
     * Determine whether the user already liked the review
     *
     * @param userId   the id of the user
     * @param reviewId the id of the review
     * @return true if user already liked the review and false if he didn't
     */
    boolean alreadyLiked(int userId, int reviewId);
}
