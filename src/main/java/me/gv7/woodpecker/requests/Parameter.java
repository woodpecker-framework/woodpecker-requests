package me.gv7.woodpecker.requests;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable parameter entry, the key and value cannot be null
 *
 * @author Liu Dong
 */
public class Parameter<T> implements Map.Entry<String, T>, Serializable {
    private static final long serialVersionUID = -6525353427059094141L;

    protected final String name;
    protected final T value;

    public Parameter(String key, T value) {
        this.name = Objects.requireNonNull(key);
        this.value = Objects.requireNonNull(value);
    }

    public static <V> Parameter<V> of(String key, V value) {
        return new Parameter<>(key, value);
    }

    /**
     * @deprecated using {@link #name()}
     */
    @Deprecated
    public String getKey() {
        return name;
    }

    /**
     * the name of param
     *
     * @deprecated using {@link #name()}
     */
    @Deprecated
    public String getName() {
        return name;
    }

    public String name() {
        return name;
    }

    /**
     * @deprecated using {@link #value()}
     */
    @Deprecated
    public T getValue() {
        return value;
    }

    public T value() {
        return value;
    }

    /**
     * @deprecated not implemented
     */
    @Deprecated
    @Override
    public T setValue(T value) {
        throw new UnsupportedOperationException("Pair is read only");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Parameter<?> parameter = (Parameter<?>) o;

        if (!name.equals(parameter.name)) return false;
        return value.equals(parameter.value);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "(" + name + " = " + value + ")";
    }
}
