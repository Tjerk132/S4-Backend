package reviewcontext;

import context.IContext;
import objects.store.Review;
import objects.store.ReviewProCon;

/**
 * Interface provided for the ProConContext with
 * documentation for the interface itself.
 *
 * @author Tjerk Sevenich
 */
public interface IProConContext extends IContext<ReviewProCon> {

    /**
     * add the pros and cons to the review
     * @param review the review to get the pros and cons by
     */
    void addProCons(Review review);

    /**
     * get all pros and cons by thr productId of the review
     * @param review the review the pros and cons have to be added to
     */
    void getProConsByProduct(Review review);
}
