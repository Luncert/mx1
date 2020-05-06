package org.luncert.mx1.probe.commons.util;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;
import org.luncert.mx1.probe.commons.exception.InvalidInvokableMethodError;
import org.luncert.mx1.probe.commons.exception.InvocationError;
import org.luncert.mx1.probe.commons.exception.ProxyInvokableError;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

// ref: https://stackoverflow.com/questions/14440270/how-does-mockito-when-invocation-work
public class Invokable {
  
  private Object instance;
  private Method method;
  
  public Invokable() {}
  
  public Invokable(Object instance, Method method) {
    validateInvokableMethod(method);
    this.instance = instance;
    this.method = method;
  }
  
  @SuppressWarnings("unchecked")
  public <T> T bind(T instance) {
    this.instance = instance;
    this.method = null;
    
    Class<T> type = (Class<T>) instance.getClass();
    Class<? extends T> proxyType = new ByteBuddy()
        .subclass(type)
        .method(ElementMatchers.any())
        .intercept(InvocationHandlerAdapter.of(new ProxyInvocationHandler()))
        .make()
        .load(type.getClassLoader())
        .getLoaded();
    
    Constructor constructor = proxyType.getConstructors()[0];
    int constructorParamCount = constructor.getParameterCount();
    Object[] emptyParams = new Object[constructorParamCount];
    
    try {
      return (T) constructor.newInstance(emptyParams);
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new ProxyInvokableError(e);
    }
  }
  
  public Object apply(Object... arguments) throws InvocationError {
    try {
      return method.invoke(instance, arguments);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new InvocationError(e);
    }
  }
  
  @Override
  public String toString() {
    return instance.getClass().getName() + "#" + method.getName();
  }
  
  private class ProxyInvocationHandler implements InvocationHandler {
  
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
      if (Invokable.this.method != null) {
        throw new ProxyInvokableError("another method has bound to this invokable");
      }
      
      validateInvokableMethod(method);
      
      Invokable.this.method = method;
      return null;
    }
  }
  
  private void validateInvokableMethod(Method method) {
    int modifiers = method.getModifiers();
    if (!Modifier.isPublic(modifiers)) {
      throw new InvalidInvokableMethodError("invokable method must be public");
    }
  }
}
