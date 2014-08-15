package com.shnaper.properties;

import java.util.Stack;

/**
 * Property resolver interface for dynamically resolving
 * property values that contain variables in them
 * @author Rostislav Shnaper
 * 2014-08-14
 */
public interface IPropertyResolver {

    /**
     * Performs resolving of the property using the specified property key
     * @param key property key
     * @param stack a stack of current property resolvers
     * @return resolved property value
     */
    public String resolve(String key, Stack<IPropertyResolver> stack);
}
