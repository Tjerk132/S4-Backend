package repository;

import context.Context;
import util.DbConnections;

import java.util.List;

public abstract class GlobalRepository<T> implements IRepository<T> {

    protected GlobalRepository(Context<T> context) {
        this.context = context;
    }

    protected static final String DB_STRING = DbConnections.inMemoryDB();

    protected Context<T> context;

    @Override
    public T getById(long id) {
        return context.getById(id);
    }

    @Override
    public List<T> getAll() {
        return context.getAll();
    }

    @Override
    public void add(T object) {
        context.add(object);
    }

    @Override
    public void delete(T object) {
        context.delete(object);
    }
}
