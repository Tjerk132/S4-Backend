package objects.user;

import javax.security.auth.Subject;
import java.security.Principal;

public class UserInfo implements Principal {

    private String id;

    private String name;

    public UserInfo(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean implies(Subject subject) {
        return true;
    }
}
