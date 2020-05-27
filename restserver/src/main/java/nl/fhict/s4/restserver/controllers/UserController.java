package nl.fhict.s4.restserver.controllers;

import nl.fhict.s4.restserver.config.exceptions.CustomInvalidException;
import nl.fhict.s4.restserver.config.exceptions.CustomNotFoundException;
import org.springframework.web.bind.annotation.*;
import repositories.UserRepository;
import objects.User;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    public UserController() {
        repository = new UserRepository();
    }

//    @Autowired
    private UserRepository repository;

    @GetMapping(produces = MediaType.APPLICATION_JSON)
    public List<User> getAllUsers(){
        return repository.getAll();
    }

    @PostMapping(path = "/login",
    consumes = MediaType.APPLICATION_JSON,
    produces = MediaType.APPLICATION_JSON)
    public User loginUser(@RequestBody User user) {

        if(repository.loginUser(user) == null) {
            throw new CustomInvalidException(User.class.getSimpleName(), "Unable to login");
        }
        else return user;
    }

    @PostMapping(path = "/register",
    consumes = MediaType.APPLICATION_JSON,
    produces = MediaType.APPLICATION_JSON)
    public User registerUser(@RequestBody User user) {

        try {
            repository.add(user);
            //remove password after registering
            user.setPassword("$-()-$");
            user.setEmailAddress("email@host.com");
            return user;
        }
        catch (IllegalArgumentException e) {
            throw new CustomInvalidException(User.class.getSimpleName(), "A user with the name already exists");
        }
    }
    @GetMapping(path = "{id}",
    produces = MediaType.APPLICATION_JSON)
    public User getUser(@PathVariable Integer id) {
        User u = repository.getById(id);
        if(u != null) {
            return u;
        }
        else throw new CustomNotFoundException(User.class.getSimpleName(), id);
    }

    @DeleteMapping(path = "/delete",
    consumes = MediaType.APPLICATION_JSON,
    produces = MediaType.APPLICATION_JSON)
    public User deleteUser(@RequestBody User user) {
        repository.delete(user);
        return user;
    }

    @PostMapping(path = "/email",
    consumes = MediaType.APPLICATION_JSON,
    produces = MediaType.APPLICATION_JSON)
    public String getUserEmail(@RequestBody Integer id) {
        return repository.getEmail(id);
    }
}
