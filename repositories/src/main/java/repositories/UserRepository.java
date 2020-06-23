package repositories;

import objects.user.User;
import org.springframework.stereotype.Repository;
import repository.GlobalRepository;
import usercontext.IUserContext;
import usercontext.UserContext;

@Repository
public class UserRepository extends GlobalRepository<User> {

    public UserRepository() {
        this.userContext = (IUserContext) getContext(new UserContext(GlobalRepository.db_String), IUserContext.class, User.class);
    }

    private IUserContext userContext;

    public User loginUser(User user) {
        return userContext.loginUser(user);
    }

    public String getEmail(long id) { return userContext.getEmail(id); }

    public User getByName(String name) { return userContext.getByName(name); }
}
