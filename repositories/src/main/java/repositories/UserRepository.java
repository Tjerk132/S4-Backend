package repositories;

import objects.user.User;
import org.springframework.stereotype.Repository;
import repository.GlobalRepository;
import usercontext.UserContext;

@Repository
public class UserRepository extends GlobalRepository<User> {

    public UserRepository() {
        super(new UserContext(GlobalRepository.DB_STRING));
        this.userContext = (UserContext) context;
    }

    private UserContext userContext;

    public User loginUser(User user) {
        return userContext.loginUser(user);
    }

    public String getEmail(long id) { return userContext.getEmail(id); }

    public User getByName(String name) { return userContext.getByName(name); }
}
