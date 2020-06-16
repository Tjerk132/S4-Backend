package modeltests;

import objects.store.Review;
import org.junit.Assert;
import org.junit.Test;

public class ReviewTests {

    @Test
    public void testCreateReview() {
        Review r = new Review();
        Assert.assertNotNull(r.getPros());
        Assert.assertNotNull(r.getCons());
    }

    @Test
    public void testSetReviewDate() {
        Review r = new Review();
        r.setDate(1592320159579L);
        Assert.assertEquals("2020-06-16 17:09:19", r.getDate());
    }
}
