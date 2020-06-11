package reviewcontext;

import context.Context;
import objects.store.Review;
import objects.store.ReviewProCon;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ProConContext extends Context<ReviewProCon> {

    public ProConContext(String connectionString) {
        super(ReviewProCon.class, connectionString, true);
    }

    public Review addProCons(Review review) {

        try {
            List<ReviewProCon> proCons = dao.queryBuilder()
                    .where()
                    .eq("productId", review.getProductId())
                    .and()
                    .eq("reviewId", review.getId())
                    .query();

            review.addPros(getProConContent(proCons, "pro"));

            review.addCons(getProConContent(proCons, "con"));
        }
        catch (SQLException e) {
            logger.warning(e.toString());
        }
        //don't close connection here
        return review;
    }

    @Override
    public ReviewProCon getById(long id) {

        ReviewProCon proCon = null;

        try {
            proCon = dao.queryForId(id);
        }
        catch (SQLException e) {
            logger.warning(e.toString());
        }
        finally {
            this.close();
        }
        return proCon;
    }

    @Override
    public List<ReviewProCon> getAll() {

        List<ReviewProCon> proCons = new LinkedList<>();

        dao.forEach(proCons::add);

        return proCons;
    }

    @Override
    public void add(ReviewProCon reviewProCon) {

        try {
            dao.create(reviewProCon);
        }
        catch (Exception e) {
            logger.warning(e.toString());
        }
        //don't close connection here
    }

    @Override
    public void delete(ReviewProCon reviewProCon) {

        try {
            dao.delete(reviewProCon);
        } catch (SQLException e) {
            logger.warning(e.toString());
        }
        finally {
            this.close();
        }
    }

    public void getProConsByProduct(Review review) {
        try {
            List<ReviewProCon> proCons = dao.queryBuilder()
                    .where()
                    .eq("reviewId", review.getId())
                    .query();

            review.addPros(getProConContent(proCons, "pro"));

            review.addCons(getProConContent(proCons, "con"));
        }
        catch (SQLException e) {
            logger.warning(e.toString());
        }
    }

    private List<String> getProConContent(List<ReviewProCon> proCons, String proConType) {
        return proCons.stream()
            .filter(x -> x.getProConType().equals(proConType))
            .map(ReviewProCon::getContent)
            .collect(Collectors.toList());
    }
}
