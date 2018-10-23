package com.comphenix.rema1000.beans;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class BeanProperty {
    private final String name;
    private final String displayName;

    private final Class<?> propertyType;
    private final Method readMethod;
    private final Method writeMethod;

    private final BeanMetadata.CustomType customType;
    private final int sortOrder;

    public BeanProperty(String name, String displayName, Class<?> propertyType,
                        Method readMethod, Method writeMethod, BeanMetadata.CustomType customType, int sortOrder) {
        this.name = name;
        this.displayName = displayName;
        this.propertyType = propertyType;
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
        this.customType = customType;
        this.sortOrder = sortOrder;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Class<?> getPropertyType() {
        return propertyType;
    }

    public Method getReadMethod() {
        return readMethod;
    }

    public Method getWriteMethod() {
        return writeMethod;
    }

    public BeanMetadata.CustomType getCustomType() {
        return customType;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public Object getPropertyValue(Object bean) {
        try {
            return readMethod.invoke(bean);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeanProperty that = (BeanProperty) o;
        return sortOrder == that.sortOrder &&
                Objects.equals(name, that.name) &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(propertyType, that.propertyType) &&
                Objects.equals(readMethod, that.readMethod) &&
                Objects.equals(writeMethod, that.writeMethod) &&
                customType == that.customType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, displayName, propertyType, readMethod, writeMethod, customType, sortOrder);
    }

    @Override
    public String toString() {
        return "BeanProperty{" +
                "name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", propertyType=" + propertyType +
                ", readMethod=" + readMethod +
                ", writeMethod=" + writeMethod +
                ", customType=" + customType +
                ", sortOrder=" + sortOrder +
                '}';
    }
}
