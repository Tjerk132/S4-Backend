package util;

import enums.AdminActivityStatus;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import usercontext.AdminActivityContext;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingInvocationHandler implements InvocationHandler {

    private final Object delegate;

    private AdminActivityContext context;

    private Class clazz;

    public LoggingInvocationHandler(final Object delegate, final Class<?> clazz, final String dbString) {
        this.delegate = delegate;
        this.context = new AdminActivityContext(dbString);
        this.clazz = clazz;
    }

    protected static final Logger logger = Logger.getLogger(LoggingInvocationHandler.class.getName());

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        RequestMethod requestMethod = getRequestMethod(method);

        try {

            Object ret = method.invoke(delegate, args);
            Integer id = getObjectId(ret);

            context.add(requestMethod, AdminActivityStatus.SUCCESS, this.clazz, null, id);

            return ret;

        } catch (Exception e) {

            context.add(requestMethod, AdminActivityStatus.FAILED, this.clazz, e.getMessage(), 0);

            throw e;
        }
    }

    private RequestMethod getRequestMethod(Method method) {

        String methodName = method.getName();
        if(methodName.contains("login") || methodName.contains("register")) {
            return RequestMethod.POST;
        }
        for(RequestMethod m : RequestMethod.values()) {
            if(methodName.contains(m.name().toLowerCase())) {
                return m;
            }
        }
        throw new IllegalArgumentException("No requestMethod found for " + method);
    }

    private Integer getObjectId(Object ret) throws IllegalAccessException {

        Integer id = null;
        try {
            ret = validateReturnSingleObject(ret);

            Field field = ReflectionUtils.findField(clazz, "id");

            assert field != null: "id field cannot be null";
            ReflectionUtils.makeAccessible(field);

            id = (Integer) field.get(ret);
        }
        catch (IllegalArgumentException | AssertionError e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return id;
    }

    private Object validateReturnSingleObject(Object ret) {
        //return only one value if multiple exist (to log)
        if (ret instanceof AbstractList) {
            return ((AbstractList) ret).subList(0,1);
        }
        else return ret;
    }
}

