package com.comphenix.rema1000.beans;

import com.comphenix.rema1000.model.ModelField;
import com.google.common.base.CaseFormat;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.stream.Collectors;

public class BeanMetadata {
    public enum CustomType {
        NONE,
        UNIX_TIME
    }

    private ImmutableList<BeanProperty> properties;

    protected BeanMetadata(ImmutableList<BeanProperty> properties) {
        this.properties = properties != null ? properties : ImmutableList.of();
    }

    public static BeanMetadata of(Class<?> clazz, Class<?> exclude) {
        try {
            // First - extract the bean info
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz, exclude);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            @SuppressWarnings("UnstableApiUsage")
            ImmutableList<BeanProperty> beanProperties = Arrays.stream(propertyDescriptors).
                    map(BeanMetadata::getBeanProperty).
                    sorted(Comparator.comparing(BeanProperty::getSortOrder)).
                    collect(ImmutableList.toImmutableList());

            return new BeanMetadata(beanProperties);

        } catch (IntrospectionException e) {
            throw new RuntimeException("Unable to extract metadata from " + clazz);
        }
    }

    private static BeanProperty getBeanProperty(PropertyDescriptor x) {
        ModelField modelField = x.getReadMethod().getAnnotation(ModelField.class);

        return new BeanProperty(
            x.getName(), formatJavaName(x.getName()), x.getPropertyType(),
            x.getReadMethod(), x.getWriteMethod(),
            modelField != null ? modelField.customType() : CustomType.NONE,
            modelField != null ? modelField.order() : -1
        );
    }

    private static String formatJavaName(String propertyName) {
        @SuppressWarnings("UnstableApiUsage")
        List<String> segments = Splitter.on("_").splitToList(
                CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, propertyName));

        return segments.stream().map(x -> x.length() > 0 ? Character.toUpperCase(x.charAt(0)) + x.substring(1).toLowerCase() : "").
                collect(Collectors.joining(" "));
    }

    public ImmutableList<BeanProperty> getProperties() {
        return properties;
    }

    @SuppressWarnings("UnstableApiUsage")
    public ImmutableList<BeanProperty> getFilteredProperties(Class<?> excludedPropertyType) {
        return properties.stream().
                filter(x -> !excludedPropertyType.isAssignableFrom(x.getPropertyType())).
                collect(ImmutableList.toImmutableList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeanMetadata that = (BeanMetadata) o;
        return Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(properties);
    }

    @Override
    public String toString() {
        return "BeanMetadata{" +
                "properties=" + properties +
                '}';
    }
}
