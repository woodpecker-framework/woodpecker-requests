package me.gv7.woodpecker.requests.json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Simple generic type infer class
 *
 * @param <T>
 */
public abstract class TypeInfer<T> {
    final Type type;

    protected TypeInfer() {
        this.type = getSuperclassTypeParameter(getClass());
    }

    private static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return parameterized.getActualTypeArguments()[0];
    }

    /**
     * Gets underlying {@code Type} instance.
     */
    public final Type getType() {
        return type;
    }
}
