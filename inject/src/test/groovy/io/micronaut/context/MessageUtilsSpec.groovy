package io.micronaut.context

import spock.lang.Specification

class MessageUtilsSpec extends Specification {

    void "test bean class name normalization"() {
        expect:
        MessageUtils.normalizeBeanClassName(null) == null
        MessageUtils.normalizeBeanClassName("") == ""
        MessageUtils.normalizeBeanClassName("ClassNameWithoutDollar") == "ClassNameWithoutDollar"
        MessageUtils.normalizeBeanClassName("FooImpl\$InternalClass") == "FooImpl\$InternalClass"
        MessageUtils.normalizeBeanClassName("\$FooImpl\$Definition\$Intercepted") == "FooImpl"
        MessageUtils.normalizeBeanClassName("\$FooImpl\$Definition\$Intercepted\$Definition") == "FooImpl"
        MessageUtils.normalizeBeanClassName("\$FooImpl\$Definition\$Intercepted\$Definition\$Reference") == "FooImpl"
    }
}