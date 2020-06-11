package objects.store;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "pros-and-cons")
public class ReviewProCon {

    public ReviewProCon() {
        // ORMLite needs a no-arg constructor
    }

    public ReviewProCon(int reviewId, int productId, String proConType, String content) {
        this.reviewId = reviewId;
        this.productId = productId;
        this.proConType = proConType;
        this.content = content;
    }

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private int reviewId;

    @DatabaseField
    private int productId;

    @DatabaseField
    private String proConType;

    @DatabaseField
    private String content;

    public String getContent() {
        return content;
    }

    public String getProConType() {
        return proConType;
    }
}
