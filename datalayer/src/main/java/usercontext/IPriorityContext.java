package usercontext;

import objects.user.Priority;

/**
 * Interface provided for the PriorityContext with
 * documentation for the interface itself.
 *
 * @author Tjerk Sevenich
 */
public interface IPriorityContext {

    /**
     * get a priority by the given user id.
     * @param userId the user id to retrieve the priority with
     * @return the role that belongs to the given user id
     */
    Priority getByUserId(long userId);

}
