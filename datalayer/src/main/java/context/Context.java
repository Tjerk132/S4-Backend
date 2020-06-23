package context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import util.DbConnections;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Context<T> implements IContext<T> {

    public Context(Class<T> clazz, String connectionString) {

        this.connectionSource = DbConnections.getConnectionSource(connectionString);
        try {
            this.dao = DaoManager.createDao(connectionSource, clazz);
        }
        catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    private JdbcPooledConnectionSource connectionSource;

    protected Dao<T, Long> dao;

    protected static final Logger logger = Logger.getLogger(Context.class.getName());

    @Override
    public T getById(long id) {

        T item = null;

        try {
            item = dao.queryForId(id);
        }
        catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
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
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    @Override
    public void update(T item) {
        try {
            dao.update(item);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    @Override
    public void delete(T item) {

        try {
            dao.delete(item);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
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
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }
}
