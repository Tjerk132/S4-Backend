package objects;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "review-likes")
public class ReviewLike {

    public ReviewLike() {
        // ORMLite needs a no-arg constructor
    }

    public ReviewLike(int userId, int reviewId) {
        this.userId = userId;
        this.reviewId = reviewId;
    }

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private int userId;

    @DatabaseField
    private int reviewId;
}
