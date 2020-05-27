package context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public abstract class Context<T> implements IContext<T> {

    public Context(Class<T> clazz, String connectionString) {

        List<String> dbProperties = Arrays.asList(connectionString.split("%"));
        final String dbString = dbProperties.get(0);
        try {
            if(dbProperties.size() == 1) {
                logger.info("No database properties submitted");
                this.connectionSource =
                        new JdbcPooledConnectionSource(dbString);
            }
            else {
                //user submitted credentials
                this.connectionSource =
                        new JdbcPooledConnectionSource(dbString,
                                dbProperties.get(1), dbProperties.get(2));
            }

            this.dao = DaoManager.createDao(connectionSource, clazz);
        }
        catch (SQLException e) {
            logger.warning(e.toString());
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
