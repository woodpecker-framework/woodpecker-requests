package me.gv7.woodpecker.requests.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflects {
    public static synchronized Object getFV(final Object o, final String s) throws IllegalAccessException, NoSuchFieldException {
        Field declaredField = null;
        Class<?> clazz = o.getClass();
        while (clazz != Object.class) {
            try {
                declaredField = clazz.getDeclaredField(s);
                break;
            }
            catch (NoSuchFieldException ex) {
                clazz = clazz.getSuperclass();
            }
        }
        if (declaredField == null) {
            throw new NoSuchFieldException(s);
        }
        declaredField.setAccessible(true);
        return declaredField.get(o);
    }


    public static synchronized Method getMethod(final Class clazz,final String methodName,Class[] paramClazz,Object[] param) throws NoSuchMethodException {
        Method method = null;
        Class<?> clazzTmp = clazz;
        while (clazzTmp != Object.class){
            try {
                method = clazzTmp.getDeclaredMethod(methodName,paramClazz);
                break;
            } catch (NoSuchMethodException e) {
                clazzTmp = clazzTmp.getSuperclass();
            }
        }

        if (method == null) {
            throw new NoSuchMethodException(methodName);
        }
        method.setAccessible(true);
        return method;
    }

    public static synchronized Object invokeMethod(final Object obj,final String methodName,Class[] paramClazz,Object[] param) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> clazz = obj.getClass();
        Method method = getMethod(clazz,methodName,paramClazz,param);
        return method.invoke(obj,param);
    }

    public static synchronized Object invokeStaticMethod(final Class clazz,final String methodName,Class[] paramClazz,Object[] param) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = getMethod(clazz,methodName,paramClazz,param);
        return method.invoke(clazz,param);
    }

    public static Class loadClass(String clazzName) throws ClassNotFoundException {
        try {
            return Class.forName(clazzName);
        } catch (ClassNotFoundException exception) {
            return Thread.currentThread().getContextClassLoader().loadClass(clazzName);
        }
    }
}
