package usercontext;

import objects.user.User;

public interface IUserContext {

    User loginUser(User user);

    String getEmail(long id);

    User getByName(String name);
}
