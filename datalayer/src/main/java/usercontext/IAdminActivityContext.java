package usercontext;

import enums.AdminActivityStatus;
import org.springframework.http.HttpMethod;

/**
 * Interface provided for the AdminActivityContext with
 * documentation for the interface itself.
 *
 * @author Tjerk Sevenich
 */
public interface IAdminActivityContext {

    /**
     * add a new admin activity to the database.
     * @param method the method of the request
     * @param status the status after the method was invoked
     * @param clazz the class type the request was ment for
     * @param exception possible exception that was thrown while invoking the method
     * @param itemId the id of the item that the request was ment for
     */
    void add(HttpMethod method, AdminActivityStatus status, Class<?> clazz, String exception, Integer itemId);
}
