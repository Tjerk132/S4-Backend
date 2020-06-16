package productcontext;

import com.j256.ormlite.stmt.SelectArg;
import context.Context;
import context.IContext;
import enums.AdminActivityStatus;
import objects.store.CartItem;
import objects.store.Product;
import objects.store.ShoppingCart;
import objects.store.TopRatedSuggestion;
import reviewcontext.IReviewContext;
import reviewcontext.ReviewContext;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class ProductContext extends Context<Product> implements IProductContext {

    public ProductContext(String connectionString) {
        super(Product.class, connectionString);
        this.reviewContext = new ReviewContext(connectionString);
    }

    private IReviewContext reviewContext;

    @Override
    public List<Product> getAll() {

        List<Product> products = new LinkedList<>();

        dao.forEach(product -> {
            product.setReviewCount(reviewContext.getProductReviewCount(product.getId()));
            products.add(product);
        });

        return products;
    }

    @Override
    public Product getById(long id) {

        Product product = null;

        try {
            product = dao.queryForId(id);
            product.setReviewCount(reviewContext.getProductReviewCount(id));
        }
        catch (SQLException e) {
            logger.warning(e.toString());
        }
        return product;
    }

    @Override
    public void add(Product product) {

        try {
            dao.create(product);
        }
        catch (Exception e) {
            logger.warning(e.toString());
        }
    }

    @Override
    public void delete(Product product) {

        try {
            dao.delete(product);
        }
        catch (SQLException e) {
            logger.warning(e.toString());
        }
    }

    @Override
    public void update(Product product) {
        try {
            dao.update(product);

        } catch (SQLException e) {
            logger.warning(e.toString());

        }
    }

    @Override
    public List<Product> getByCategory(String category) {

        List<Product> products = new LinkedList<>();
        try {
            products = dao.queryBuilder()
                    .where()
                    .eq("category", new SelectArg(category))
                    .query();

            for (Product p : products) {
                p.setReviewCount(reviewContext.getProductReviewCount(p.getId()));
            }
        }
        catch (SQLException e) {
            logger.warning(e.toString());
        }
        return products;
    }

    @Override
    public List<Product> getProductsByName(String productName) {

        List<Product> products = new LinkedList<>();
        try {
            products = dao.queryBuilder()
                    .where()                            //filter on given name
                    .like("name", new SelectArg('%' + productName + '%'))
                    .query();

            for (Product p : products) {
                p.setReviewCount(reviewContext.getProductReviewCount(p.getId()));
            }
        }
        catch (SQLException e) {
            logger.warning(e.toString());
        }
        return products;
    }

    @Override
    public void deleteBasketProductsFromStore(ShoppingCart shoppingCart) {
        for(CartItem cartItem : shoppingCart.getProducts()) {
            int quantity = cartItem.getQuantity();
            int productId = cartItem.getProduct().getId();

            try {

                Product p = dao.queryBuilder().where()
                        .eq("id", productId)
                        .queryForFirst();

                int remainingQuantity = p.getStockCount() - quantity;
                //count can't be lower then 0
                p.setStockCount(Math.min(remainingQuantity, 0));

                dao.update(p);

            } catch (SQLException e) {
                logger.warning(e.toString());

            }
        }
        this.close();
    }

    @Override
    public List<TopRatedSuggestion> getTopRatedSuggestions() {

        List<TopRatedSuggestion> topRatedSuggestions = new LinkedList<>();

        for(Product p : getAll()) {
            topRatedSuggestions.add(new TopRatedSuggestion(p, reviewContext.getProductAvgRating(p)));
        }
        //order top rated suggestions by rating and reverse to get high to low
        topRatedSuggestions.sort(Comparator.comparingDouble(TopRatedSuggestion::getAvgRating).reversed());

        topRatedSuggestions = topRatedSuggestions.subList(0, 5);

        return topRatedSuggestions;
    }
}
