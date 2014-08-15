package com.shnaper.properties;

import java.util.Hashtable;
import java.util.Properties;
import java.util.Stack;

/**
 * An enhanced version of {@link java.util.Properties} that is capable of
 * resolving variables within property values using the {@code ${value}} format
 * @author Rostislav Shnaper
 * 2014-08-14
 */
public class EnhancedProperties extends Properties implements IPropertyResolver {
    private Hashtable<String,PropertyResolver> resolvers = new Hashtable<String, PropertyResolver>();

    @Override
    public String getProperty(String key) {
        return resolve(key, new Stack<IPropertyResolver>());
    }

    @Override
    public synchronized Object setProperty(String key, String value) {
        if(key != null && resolvers.containsKey(key)) {
            if(value == null) {
                resolvers.remove(key);
            }
            else {
                //resolve the values again
                if(PropertyResolver.needsResolving(value)) {
                    resolvers.put(key, new PropertyResolver(key, value, this));
                }
            }
        }
        return super.setProperty(key, value);
    }

    @Override
    public synchronized void clear() {
        resolvers.clear();
        super.clear();
    }

    @Override
    public String resolve(String key, Stack<IPropertyResolver> stack) {
        String value;
        PropertyResolver resolver = resolvers.get(key);
        if(resolver != null) {
            value = resolver.getValue(stack);
        }
        else {
            value = super.getProperty(key);
            if(PropertyResolver.needsResolving(value)) {
                resolver = new PropertyResolver(key,value,this);
                resolvers.put(key, resolver);
                value = resolver.getValue(stack);
            }
        }
        return value;
    }
}
