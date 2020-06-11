package usercontext;

import com.j256.ormlite.dao.Dao;
import context.Context;
import enums.AdminActivityStatus;
import objects.user.AdminActivity;
import objects.user.UserInfo;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMethod;

public class AdminActivityContext extends Context<AdminActivity> {

    public AdminActivityContext(String connectionString) {
        super(AdminActivity.class, connectionString, false);
    }

    private Dao<AdminActivity, Long> adminActivityDao;

    public void add(RequestMethod method, AdminActivityStatus status, Class<?> clazz, Integer itemId) {

        AdminActivity a = createAdminActivity(method, status, clazz.getSimpleName(), itemId);
        try {
            adminActivityDao.create(a);
        }
        catch (Exception e) {
//            logger.warning(e.toString());
        }
    }

    private AdminActivity createAdminActivity(RequestMethod method, AdminActivityStatus status, String className, Integer itemId) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserInfo info = (UserInfo) token.getPrincipal();

        int userId = Integer.parseInt(info.getId());

        return new AdminActivity(userId, method, status, className, itemId);
    }

    public void setAdminActivityDao(Dao<AdminActivity, Long> adminActivityDao) {
        this.adminActivityDao = adminActivityDao;
    }


}
