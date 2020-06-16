package usercontext;

import com.j256.ormlite.stmt.SelectArg;
import context.Context;
import objects.user.Priority;

import java.sql.SQLException;

class PriorityContext extends Context<Priority> {

    PriorityContext(String connectionString) {
        super(Priority.class, connectionString);
    }

    Priority getByUserId(long userId) {

        try {
            return dao.queryBuilder()
                    .where()
                    .eq("userId", new SelectArg(userId))
                    .queryForFirst();
        }
        catch (SQLException e) {
            logger.warning(e.toString());
        }
        return null;
    }

}
