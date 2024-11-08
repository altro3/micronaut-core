/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.context;

import io.micronaut.core.util.StringUtils;

/**
 * Some helper method for log / exception messages.
 *
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
        if (StringUtils.isEmpty(typeString) || !typeString.contains("$")) {
            return typeString;
        }
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
