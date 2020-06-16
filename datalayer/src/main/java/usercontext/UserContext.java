package usercontext;

import com.j256.ormlite.stmt.SelectArg;
import context.Context;
import objects.user.User;
import util.HashSaltAuthentication;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

public class UserContext extends Context<User> implements IUserContext {

    public UserContext(String connectionString) {
        super(User.class, connectionString);
        this.priorityContext = new PriorityContext(connectionString);
    }

    private PriorityContext priorityContext;

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
                 .eq("username", new SelectArg(user.getUsername()))
                 .countOf() != 0;

            if(!existing) {
                user.setPassword(HashSaltAuthentication.getSaltedHash(user.getPassword()));
                dao.create(user);
            }
            else throw new IllegalArgumentException("A user with that username already exists");
        }
        //only catch sqlExceptions
        catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    @Override
    public User loginUser(User user) {

        try {
            //can only check on name as password is hashed with salt
            User storedUser = dao.queryBuilder().where()
                    .eq("username", user.getUsername())
                    .queryForFirst();

            //select first record and return given password matches with stored password
            if(storedUser != null && HashSaltAuthentication.check(user.getPassword(), storedUser.getPassword())) {
                storedUser.setPassword(PW_SECURE);
                storedUser.setEmailAddress(EMAIL_SECURE);
                storedUser.setRole(priorityContext.getByUserId(storedUser.getId()).getRole());
                return storedUser;
            }

        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public String getEmail(long id) {
        try {

            return dao.queryBuilder()
                    .where()
                    .eq("id", new SelectArg(id))
                    .queryForFirst().getEmailAddress();
        }
        catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public User getByName(String name) {
        try {
            User user =
                    dao.queryBuilder()
                    .where()
                    .eq("username", new SelectArg(name))
                    .queryForFirst();

            user.setRole(priorityContext.getByUserId(user.getId()).getRole());
            return user;
        }
        catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
            return null;
        }
    }
}
