//package org.luncert.mx1.probe.commons.util;
//
//import net.bytebuddy.ByteBuddy;
//import net.bytebuddy.implementation.InvocationHandlerAdapter;
//import net.bytebuddy.matcher.ElementMatchers;
//import org.luncert.mx1.probe.commons.exception.ProxyInvokableError;
//
//import java.lang.reflect.Constructor;
//import java.lang.reflect.InvocationHandler;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.Collections;
//import java.util.List;
//
//public final class ReflectUtils {
//
//  private ReflectUtils() {}
//
//  @SuppressWarnings("unchecked")
//  public static <T> T recordInvocation(T instance) {
//
//    Class<T> type = (Class<T>) instance.getClass();
//    Class<? extends T> proxyType = new ByteBuddy()
//        .subclass(type)
//        .method(ElementMatchers.any())
//        .defaultValue()
//        .intercept(InvocationHandlerAdapter.of(new ProxyInvocationHandler()))
//        .make()
//        .load(type.getClassLoader())
//        .getLoaded();
//
//    Constructor constructor = proxyType.getConstructors()[0];
//    int constructorParamCount = constructor.getParameterCount();
//    Object[] emptyParams = new Object[constructorParamCount];
//
//    try {
//      return (T) constructor.newInstance(emptyParams);
//    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
//      throw new ProxyInvokableError(e);
//    }
//  }
//
//  public interface InvocationRecorder {
//
//    default List<Method> getRecord() {
//      return Collections.emptyList();
//    }
//  }
//
//  private static class ProxyInvocationHandler implements InvocationHandler {
//
//    @Override
//    public Object invoke(Object proxy, Method method, Object[] args) {
//      if (Invokable.this.method != null) {
//        throw new ProxyInvokableError("another method has bound to this invokable");
//      }
//
//      validateInvokableMethod(method);
//
//      Invokable.this.method = method;
//      return null;
//    }
//  }
//}
