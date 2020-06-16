package util;

import enums.AdminActivityStatus;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import usercontext.AdminActivityContext;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LoggingInvocationHandler implements InvocationHandler {

    private final Object delegate;

    private AdminActivityContext context;

    private Class clazz;

    public LoggingInvocationHandler(final Object delegate, final Class<?> clazz, final String dbString) {
        this.delegate = delegate;
        this.context = new AdminActivityContext(dbString);
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        RequestMethod requestMethod = getRequestMethod(method);

        try {

            Object ret = method.invoke(delegate, args);
            Integer id = getObjectId(ret);

            context.add(requestMethod, AdminActivityStatus.SUCCESS, this.clazz, null, id);

            return ret;

        } catch (Throwable t) {

            context.add(requestMethod, AdminActivityStatus.FAILED, this.clazz, t.getMessage(), 0);

            throw t;
        }
    }

    private RequestMethod getRequestMethod(Method method) {
        for(RequestMethod m : RequestMethod.values()) {
            if(method.getName().contains(m.name().toLowerCase())) {
                return m;
            }
        }
        throw new IllegalArgumentException("No requestMethod found for " + method);
    }

    private Integer getObjectId(Object ret) throws IllegalAccessException {

        Field field = ReflectionUtils.findField(clazz, "id");
        ReflectionUtils.makeAccessible(field);

        return (Integer) field.get(ret);
    }
}

