package util;

import objects.store.Review;
import objects.store.ReviewProCon;
import reviewcontext.ProConContext;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ReviewCreator {

    public ReviewCreator(String connectionString) {
        this.proConContext = new ProConContext(connectionString);
    }

    private ProConContext proConContext;

    private void setMillisToTimeStamp(Review review) {
        //convert time millis to timeStamp (mySQL doesn't support 13 digit timeStamp millis)
        long timeMillis = review.getTimeMillis();
        review.setDate(timeMillis);
    }

    public void setTimeStampToMillis(Review review) {

        long timeMillis = 0;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            timeMillis = df.parse(review.getDate()).getTime();
        }
        catch (ParseException e) {}

        review.setDate(0);
        review.setTimeMillis(timeMillis);
    }

    public void createReview(Review review) {
        setMillisToTimeStamp(review);
        proConContext.addProCons(review);
    }

    public void createReviewByProduct(Review review) {
        setMillisToTimeStamp(review);
        proConContext.getProConsByProduct(review);
    }


    public void proConContextAddPros(List<String> pros, int reviewId, int productId) {
        //String is the content itself
        for(String proContent : pros) {
            ReviewProCon pro = new ReviewProCon(reviewId, productId, "pro", proContent);
            proConContext.add(pro);
        }
    }

    public void proConContextAddCons(List<String> cons, int reviewId, int productId) {
        //String is the content itself
        for(String conContent : cons) {
            ReviewProCon con = new ReviewProCon(reviewId, productId, "con", conContent);
            proConContext.add(con);
        }
    }
}
