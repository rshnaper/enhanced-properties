package com.shnaper.properties;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A property resolver implementation that recursively
 * tries to resolve a value. If a cycle is detected
 * then a runtime exception is thrown
 * @author Rostislav Shnaper
 * 2014-08-14
 */
class PropertyResolver implements IPropertyResolver{
    private final static String VAR_REGEX = "\\$\\{[a-zA-Z0-9\\.\\-]+\\}";
    private final static Pattern PATTERN = Pattern.compile(VAR_REGEX);

    private String key, unresolvedValue, resolvedValue;
    private IPropertyResolver parent;

    PropertyResolver(String key, String unresolvedValue, IPropertyResolver parent) {
        this.key = key;
        this.unresolvedValue = unresolvedValue;
        this.parent = parent;
    }

    /**
     * Returns the value of this property resolver, resolving it
     * if necessary. <strong>Do not use this method directly.</strong>
     * @param stack stack of current property resolvers
     * @return resolved property value
     * @throws  java.lang.RuntimeException if a cycle has been detected during resolve stage
     */
    String getValue(Stack<IPropertyResolver> stack) {
        if(stack.contains(this)) {
            throw new RuntimeException(String.format("Cycle detected for key '%s'",key));
        }

        try {
            stack.push(this);

            if (resolvedValue == null && needsResolving(unresolvedValue)) {
                StringBuffer compiledValue = new StringBuffer();
                Matcher matcher = PATTERN.matcher(unresolvedValue);

                String resolvedString;
                while (matcher.find()) {
                    resolvedString = resolve(unresolvedValue.substring(matcher.start() + 2, matcher.end() - 1), stack);
                    if (resolvedString != null) {
                        matcher.appendReplacement(compiledValue, resolvedString);
                    }
                }
                matcher.appendTail(compiledValue);

                resolvedValue = compiledValue.toString();
            } else {
                resolvedValue = unresolvedValue;
            }
        }
        finally {
            stack.pop();
        }

        return resolvedValue;
    }

    @Override
    public String resolve(String key, Stack<IPropertyResolver> stack) {
        return parent != null ? parent.resolve(key, stack) : null;
    }

    /**
     * Checks whether the specified string requires resolving.
     * A string requires resolving only if it contains values
     * matching the #VAR_REGEX pattern
     * @param value a String value
     * @return {@code} true if value contains #VAR_REGEX pattern
     */
    public static boolean needsResolving(String value) {
        return value != null && PATTERN.matcher(value).find();
    }
}
