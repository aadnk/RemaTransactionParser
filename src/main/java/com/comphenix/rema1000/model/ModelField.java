package com.comphenix.rema1000.model;

import com.comphenix.rema1000.beans.BeanMetadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModelField {
    /**
     * Retrieve the custom output type of this model field.
     * @return The custom type.
     */
    public BeanMetadata.CustomType customType() default BeanMetadata.CustomType.NONE;

    /**
     * Specify the order of the model field.
     * @return The order, or -1 for no specified order.
     */
    public int order() default  -1;
}
