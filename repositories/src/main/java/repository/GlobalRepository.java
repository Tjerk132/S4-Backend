package repository;

import context.IContext;
import util.DbConnections;
import util.LoggingInvocationHandler;

import java.lang.reflect.Proxy;
import java.util.List;

public abstract class GlobalRepository<T> implements IRepository<T> {

    protected Object getContext(IContext<T> context, Class contextImpl, Class<?> objectClass) {

        this.context = context;
        return Proxy.newProxyInstance(LoggingInvocationHandler.class.getClassLoader(),
            new Class[] {contextImpl},
            new LoggingInvocationHandler (context, objectClass, db_String));
    }

    protected static final String db_String = DbConnections.productionDB();

    private IContext<T> context;

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
    public void update(T object) { context.update(object); }

    @Override
    public void delete(T object) {
        context.delete(object);
    }

}
