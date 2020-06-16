package modeltests;

import enums.AdminActivityStatus;
import objects.store.Product;
import objects.user.AdminActivity;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMethod;

public class AdminActivityTests {

    @Test
    public void testCreateAdminActivity() {
        AdminActivity adminActivity = new AdminActivity(1, HttpMethod.POST, AdminActivityStatus.SUCCESS, Product.class.getSimpleName(), null, 25);
        Assert.assertEquals("Product", adminActivity.getType());
        Assert.assertEquals("none", adminActivity.getException());
        Assert.assertEquals(25, adminActivity.getItemId());
    }

    @Test
    public void testAdminActivityWithException() {
        AdminActivity adminActivity = new AdminActivity(1, HttpMethod.POST, AdminActivityStatus.FAILED, Product.class.getSimpleName(), "Null pointer", 25);
        Assert.assertEquals("Null pointer", adminActivity.getException());
        Assert.assertEquals(0, adminActivity.getItemId());
    }
}
