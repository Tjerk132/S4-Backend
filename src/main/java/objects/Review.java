package objects;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

@DatabaseTable(tableName = "reviews")
public class Review {

    public Review() {
        // ORMLite needs a no-arg constructor

        this.pros = new LinkedList<>();
        this.cons = new LinkedList<>();
    }

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private int productId;

    @DatabaseField
    private String author;

    @DatabaseField
    private Long timeMillis;

    private String date;

    @DatabaseField
    private String title;

    @DatabaseField
    private String content;

    @DatabaseField
    private int starRating;

    @DatabaseField
    private int liked;

    private List<String> pros;

    private List<String> cons;

    public void addPros(List<String> pros) {
        if(!pros.isEmpty()) {
            this.pros.addAll(pros);
        }
    }

    public void addCons(List<String> cons) {
        if(!cons.isEmpty()) {
            this.cons.addAll(cons);
        }
    }

    public List<String> getPros() {
        return this.pros;
    }

    public List<String> getCons() {
        return this.cons;
    }

    public int getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public int getStarRating() {
        return starRating;
    }

    public long getTimeMillis() { return timeMillis; }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(long timeMillis) {
        this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Timestamp(timeMillis));
    }

    public void setTimeMillis(Long l) {
        this.timeMillis = l;
    }

    public String getDate() {
        return date;
    }

}
