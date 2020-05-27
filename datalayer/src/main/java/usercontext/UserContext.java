package usercontext;

import context.Context;
import objects.User;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UserContext extends Context<User> {

    public UserContext(String connectionString) {
        super(User.class, connectionString);
    }

    private static final String PW_SECURE = "$-()-$";

    private static final String EMAIL_SECURE = "email@host.com";

    @Override
    public List<User> getAll() {

        List<User> users = new LinkedList<>();

        dao.forEach(user -> {
            user.setPassword(PW_SECURE);
            user.setEmailAddress(EMAIL_SECURE);
            users.add(user);
        });

        return users;
    }

    @Override
    public User getById(long id) {

        User user = null;

        try {
            user = dao.queryForId(id);
            if(user != null) {
                user.setPassword(PW_SECURE);
                user.setEmailAddress(EMAIL_SECURE);
            }
        }
        catch (SQLException e) {
            logger.warning(e.toString());
        }
        return user;
    }

    @Override
    public void add(User user) {

        try {
            boolean existing = dao.queryBuilder()
                .where()
                 .eq("username", user.getUsername())
                 .countOf() != 0;

            if(!existing) {
                user.setPassword(PasswordAuthentication.getSaltedHash(user.getPassword()));
                dao.create(user);
            }
            else throw new IllegalArgumentException();
        }
        //only catch sqlExceptions
        catch (SQLException e) {
            logger.warning(e.toString());
        }
        finally {
            this.close();
        }
    }

    public User loginUser(User user) {

        try {
            //can only check on name as password is hashed with salt
            User storedUser = dao.queryBuilder().where()
                    .eq("username", user.getUsername())
                    .queryForFirst();

            //select first record and return given password matches with stored password
            if(storedUser != null && PasswordAuthentication.check(user.getPassword(), storedUser.getPassword())) {
                storedUser.setPassword(PW_SECURE);
                storedUser.setEmailAddress(EMAIL_SECURE);
                return storedUser;
            }

        } catch (SQLException e) {
            logger.warning(e.toString());
        }
        return null;
    }

    public String getEmail(long id) {
        try {

            return dao.queryBuilder()
                    .where()
                    .eq("id", id)
                    .queryForFirst().getEmailAddress();
        }
        catch (SQLException e) {
            logger.warning(e.toString());
            return null;
        }
    }
}
