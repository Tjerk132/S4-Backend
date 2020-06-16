package usercontext;

import objects.user.User;

/**
 * Interface provided for the UserContext with
 * documentation for the interface itself.
 *
 * @author Tjerk Sevenich
 */
public interface IUserContext {

    /**
     * login a user with the given user credentials.
     * @param user the user object that contains the credentials
     * @return the user if the credentials are correct
     */
    User loginUser(User user);

    /**
     * get a user's email by his/her id.
     * @param id the id of the user of who's email needs to be retrieved
     * @return the email of the user
     */
    String getEmail(long id);

    /**
     * get a user by the given name.
     * @param name the name of the user that needs to be retrieved
     * @return the user found by the name
     */
    User getByName(String name);
}
