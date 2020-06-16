package util;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbConnections {

    protected static final Logger logger = Logger.getLogger(DbConnections.class.getName());

    private static final String JDBC = "jdbc";

    public static String inMemoryDB() {
        return buildDbString(JDBC, dbType.SQLITE.getType(), "Database.db");
    }

    public static String productionDB() {
        return buildDbString(JDBC, dbType.MYSQL.getType(),
                "//studmysql01.fhict.local/dbi386599,dbi386599,12345");
    }

    private static String buildDbString(String... dbConfig) {
        StringBuilder builder = new StringBuilder();
        for (String config : dbConfig) {
            builder.append(config);
            //don't append semicolon if last property
            if (!dbConfig[dbConfig.length - 1].equals(config))
                builder.append(":");
        }
        return builder.toString().replace(",","%");
    }

    public static JdbcPooledConnectionSource createOrmConnection(String connectionString) {
        List<String> dbProperties = Arrays.asList(connectionString.split("%"));
        final String dbString = dbProperties.get(0);

        JdbcPooledConnectionSource source = null;
        try {
            //in-memory db
            if(dbProperties.size() == 1) {
                source = new JdbcPooledConnectionSource(dbString);
            }
            //production db
            else {
                source = new JdbcPooledConnectionSource(dbString,
                        dbProperties.get(1), dbProperties.get(2));
            }
        }
        catch (SQLException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return source;
    }

    private enum dbType {
        SQLITE,
        MYSQL;

        String getType() {
            return this.name().toLowerCase();
        }
    }
}
