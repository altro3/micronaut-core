package io.micronaut.context;

/**
 * @since 4.8.0
 */
public final class MessageUtils {

    private static final String POSTFIX_BEAN_DEFINITION_REFERENCE = "$Definition$Intercepted$Definition$Reference";
    private static final String POSTFIX_BEAN_DEFINITION = "$Definition$Intercepted$Definition";
    private static final String POSTFIX_BEAN_METHOD_DEFINITION = "$Definition$Intercepted";

    private MessageUtils() {
    }

    /**
     * Normalization bean class names for logs.
     *
     * @param typeString bean class name
     *
     * @return normalized bean class name
     */
    public static String normalizeBeanClassName(String typeString) {
        if (typeString.startsWith("$")) {
            typeString = typeString.substring(1);
        }
        if (typeString.endsWith(POSTFIX_BEAN_DEFINITION_REFERENCE)) {
            typeString = typeString.substring(0, typeString.indexOf(POSTFIX_BEAN_DEFINITION_REFERENCE));
        } else if (typeString.endsWith(POSTFIX_BEAN_DEFINITION)) {
            typeString = typeString.substring(0, typeString.indexOf(POSTFIX_BEAN_DEFINITION));
        } else if (typeString.endsWith(POSTFIX_BEAN_METHOD_DEFINITION)) {
            typeString = typeString.substring(0, typeString.indexOf(POSTFIX_BEAN_METHOD_DEFINITION));
        }
        return typeString;
    }
}
