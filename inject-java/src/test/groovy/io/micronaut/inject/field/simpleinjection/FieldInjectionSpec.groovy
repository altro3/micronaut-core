/*
 * Copyright 2017-2019 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.inject.field.simpleinjection

import io.micronaut.annotation.processing.test.AbstractTypeElementSpec
import io.micronaut.context.ApplicationContext
import io.micronaut.context.BeanContext
import io.micronaut.context.BeanResolutionTraceMode

class FieldInjectionSpec extends AbstractTypeElementSpec {

    void 'test field injection generics'() {
        given:
        def definition = buildBeanDefinition('fieldinjection.Test', '''
package fieldinjection;

import jakarta.inject.*;

@Singleton
class Test {
    @Inject
    java.util.List<Bar> bars;
}

class Bar {

}
''')
        expect:
        definition.injectedFields.first().asArgument().firstTypeVariable.get().type.name == 'fieldinjection.Bar'
    }

    void "test injection via field with interface"() {
        given:
        ApplicationContext context = ApplicationContext.builder()
                                            .beanResolutionTrace(BeanResolutionTraceMode.STANDARD_OUT)
                                            .start()

        when:"Alpha bean is obtained that has a field with @Inject"
        B b =  context.getBean(B)

        then:"The implementation is injected"
        b.a != null

        cleanup:
        context.close()
    }

    void "test values injection with private fields"() {
        ApplicationContext context = ApplicationContext.run()

        when:
            E e = context.getBean(E)

        then:
            e.value == null
            e.property == null

        cleanup:
            context.close()
    }

    void "test values injection with protected fields"() {
        BeanContext context = ApplicationContext.run()

        when:
            D e = context.getBean(D)

        then:
            e.value == null
            e.property == null

        cleanup:
            context.close()
    }
}

