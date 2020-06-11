package objects.store;

public class TopRatedSuggestion {

    public TopRatedSuggestion(Product product, double avgRating) {

        this.product = product;
        this.avgRating = avgRating;
    }

    private Product product;

    private double avgRating;

    public Product getProduct() {
        return product;
    }

    public double getAvgRating() {
        return avgRating;
    }
}
