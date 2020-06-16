package reviewcontext;

import objects.store.Review;

public interface IProConContext {

    Review addProCons(Review review);

    void getProConsByProduct(Review review);
}
