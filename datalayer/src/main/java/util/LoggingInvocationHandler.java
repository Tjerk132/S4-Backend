package util;

import enums.AdminActivityStatus;
import objects.store.TopRatedSuggestion;
import org.springframework.http.HttpMethod;
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

        HttpMethod requestMethod = getRequestMethod(method);

        try {

            Object ret = method.invoke(delegate, args);
            Integer id = getObjectId(ret);
            //set to 0 if no id's were found
            id = id == null ? 0 : id;

            context.add(requestMethod, AdminActivityStatus.SUCCESS, ret.getClass(), null, id);

            return ret;

        } catch (Exception e) {

            context.add(requestMethod, AdminActivityStatus.FAILED, clazz, e.getMessage(), 0);

            throw e;
        }
    }

    private HttpMethod getRequestMethod(Method method) {

        String methodName = method.getName();
        if(methodName.contains("login") || methodName.contains("register")) {
            return HttpMethod.POST;
        }
        if(methodName.contains("like")) {
            return HttpMethod.GET;
        }
        for(HttpMethod m : HttpMethod.values()) {
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

            if(ret != null && !(ret instanceof AbstractList && ((AbstractList) ret).isEmpty())) {

                Field field = ReflectionUtils.findField(ret.getClass(), "id");

                if(field != null ) {

                    ReflectionUtils.makeAccessible(field);
                    id = (int) field.get(ret);
                }
                else if (ret instanceof TopRatedSuggestion) {
                    id = ((TopRatedSuggestion) ret).getProduct().getId();
                }
            }
            else return 0;
        }
        catch (IllegalArgumentException | AssertionError e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
        return id;
    }

    private Object validateReturnSingleObject(Object ret) {
        //return only one value if multiple exist (to log)
        if (ret instanceof AbstractList && ((AbstractList) ret).size() > 1) {
            return ((AbstractList) ret).get(0);
        }
        else return ret;
    }
}

