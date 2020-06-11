package context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import objects.user.AdminActivity;
import usercontext.AdminActivityContext;
import util.DbConnections;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public abstract class Context<T> implements IContext<T> {

    public Context(Class<T> clazz, String connectionString, boolean logActivity) {

        //create log activity context for context if requested
        if (logActivity) {
            setConnectionSource(connectionString, clazz);
            createLogActivityDao(connectionString);
        }
        // if class is not activity log dao itself
        else if(!clazz.isInstance(AdminActivity.class)) {
            setConnectionSource(connectionString, clazz);
        }
    }

    @SuppressWarnings("unchecked")
    private void createLogActivityDao(String connectionString) {
        this.adminActivityContext = new AdminActivityContext(connectionString);
        adminActivityContext.setAdminActivityDao(createDao(AdminActivity.class));
    }
    @SuppressWarnings("unchecked")
    private void setConnectionSource(String connectionString, Class<?> clazz) {
        this.connectionSource = DbConnections.createOrmConnection(connectionString);
        this.dao = createDao(clazz);
    }

    private Dao createDao(Class<?> clazz) {
        try {
            return DaoManager.createDao(connectionSource, clazz);
        }
        catch (SQLException e) {
//            logger.warning(e.toString());
            return null;
        }
    }

    private JdbcPooledConnectionSource connectionSource;

    protected Dao<T, Long> dao;

    protected AdminActivityContext adminActivityContext;

    protected static final Logger logger = Logger.getLogger(Context.class.getName());

    @Override
    public T getById(long id) {

        T item = null;

        try {
            item = dao.queryForId(id);
        }
        catch (SQLException e) {
            logger.warning(e.toString());
        }
        finally {
            this.close();
        }
        return item;
    }

    @Override
    public List<T> getAll() {

        List<T> items = new LinkedList<>();

        dao.forEach(items::add);

        return items;
    }

    @Override
    public void add(T item) {

        try {
            dao.create(item);
        }
        catch (Exception e) {
            logger.warning(e.toString());
        }
        finally {
            this.close();
        }
    }

    @Override
    public void delete(T item) {

        try {
            dao.delete(item);
        } catch (SQLException e) {
            logger.warning(e.toString());
        }
        finally {
            this.close();
        }
    }

    protected void close() {
        try {
            if (connectionSource != null) {
                connectionSource.close();
            }
        }
        catch (IOException e) {
            logger.warning(e.toString());
        }
    }
}
