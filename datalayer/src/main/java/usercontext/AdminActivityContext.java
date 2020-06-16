package usercontext;

import context.Context;
import enums.AdminActivityStatus;
import objects.user.AdminActivity;
import objects.user.UserInfo;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.logging.Level;

public class AdminActivityContext extends Context<AdminActivity> implements IAdminActivityContext {

    public AdminActivityContext(String connectionString) {
        super(AdminActivity.class, connectionString);
    }

    @Override
    public void add(HttpMethod method, AdminActivityStatus status, Class<?> clazz, String exception, Integer itemId) {

        try {
            AdminActivity a = createAdminActivity(method, status, clazz.getSimpleName(), exception, itemId);
            dao.create(a);
        }
        catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    private AdminActivity createAdminActivity(HttpMethod method, AdminActivityStatus status, String className, String exception, Integer itemId) {

        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if(token != null) {
            UserInfo info = (UserInfo) token.getPrincipal();

            int userId = Integer.parseInt(info.getId());
            return new AdminActivity(userId, method, status, className, exception, itemId);
        }
        else return new AdminActivity(0, method, status, className, exception, itemId);
    }
}
