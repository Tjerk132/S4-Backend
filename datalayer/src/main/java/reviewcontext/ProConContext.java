package reviewcontext;

import com.j256.ormlite.stmt.SelectArg;
import context.Context;
import objects.store.Review;
import objects.store.ReviewProCon;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class ProConContext extends Context<ReviewProCon> implements IProConContext {

    public ProConContext(String connectionString) {
        super(ReviewProCon.class, connectionString);
    }

    @Override
    public void addProCons(Review review) {

        try {
            List<ReviewProCon> proCons = dao.queryBuilder()
                    .where()
                    .eq("productId", new SelectArg(review.getProductId()))
                    .and()
                    .eq("reviewId", new SelectArg(review.getId()))
                    .query();

            review.addPros(getProConContent(proCons, "pro"));

            review.addCons(getProConContent(proCons, "con"));
        }
        catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    @Override
    public void getProConsByProduct(Review review) {
        try {
            List<ReviewProCon> proCons = dao.queryBuilder()
                    .where()
                    .eq("reviewId", new SelectArg(review.getId()))
                    .query();

            review.addPros(getProConContent(proCons, "pro"));

            review.addCons(getProConContent(proCons, "con"));
        }
        catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    private List<String> getProConContent(List<ReviewProCon> proCons, String proConType) {
        return proCons.stream()
            .filter(x -> x.getProConType().equals(proConType))
            .map(ReviewProCon::getContent)
            .collect(Collectors.toList());
    }
}
