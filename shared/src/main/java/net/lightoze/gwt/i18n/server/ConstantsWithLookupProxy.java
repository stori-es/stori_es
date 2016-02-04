package net.lightoze.gwt.i18n.server;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.MissingResourceException;
import java.util.Set;

import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.i18n.client.LocalizableResource;

/**
 * @author Vladimir Kulev
 */
public class ConstantsWithLookupProxy implements InvocationHandler {
    private final Set<Method> methods;
    private final Class<? extends LocalizableResource> cls;
    private final InvocationHandler handler;

    protected ConstantsWithLookupProxy(Class<? extends LocalizableResource> cls, InvocationHandler handler) {
        this.cls = cls;
        this.handler = handler;
        methods = new HashSet<Method>(Arrays.asList(ConstantsWithLookup.class.getDeclaredMethods()));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (methods.contains(method)) {
            try {
                method = cls.getMethod((String) args[0]);
            } catch (NoSuchMethodException e) {
                throw new MissingResourceException(e.getMessage(), cls.getCanonicalName(), method.getName());
            }
            args = null;
        }
        return handler.invoke(proxy, method, args);
    }
}
