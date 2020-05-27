package util;

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

    private enum dbType {
        SQLITE,
        MYSQL;

        String getType() {
            return this.name().toLowerCase();
        }
    }
}
