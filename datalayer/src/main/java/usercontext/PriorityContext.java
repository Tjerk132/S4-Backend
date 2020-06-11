package usercontext;

import context.Context;
import objects.user.Priority;

import java.sql.SQLException;

class PriorityContext extends Context<Priority> {

    PriorityContext(String connectionString) {
        super(Priority.class, connectionString, false);
    }

    Priority getByUserId(long userId) {

        try {
            return dao.queryBuilder()
                    .where()
                    .eq("userId", userId)
                    .queryForFirst();
        }
        catch (SQLException e) {
//            logger.warning(e.toString());
        }
        return null;
    }

}
