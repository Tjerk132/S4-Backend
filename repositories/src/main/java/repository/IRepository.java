package repository;

import java.util.List;

public interface IRepository<T> {

    /**
     * get a Object from the database with a given id.
     * @param id identifier for object to be searched
     * @return object that has been found with given id
     */
    T getById(final long id);

    /**
     * retrieve all objects existing in the database
     * @return all objects that has been found from
     * the database.
     */
    List<T> getAll();

    /**
     * add the object to the database
     * @param entity the object to be added
     */
    void add(final T entity);

    /**
     * update the object in the database
     * @param entity the object to be updated
     */
    void update(final T entity);

    /**
     * remove the object from the database
     * @param entity the object to be deleted
     */
    void delete(final T entity);
}
