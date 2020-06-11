package util;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class DbConnections {

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
        try {
            //in-memory db
            if(dbProperties.size() == 1) {
                return new JdbcPooledConnectionSource(dbString);
            }
            //production db
            else {
                return new JdbcPooledConnectionSource(dbString,
                        dbProperties.get(1), dbProperties.get(2));
            }
        }
        catch (SQLException e) {
//            logger.warn(e.toString());
            return null;
        }
    }

    private enum dbType {
        SQLITE,
        MYSQL;

        String getType() {
            return this.name().toLowerCase();
        }
    }
}
