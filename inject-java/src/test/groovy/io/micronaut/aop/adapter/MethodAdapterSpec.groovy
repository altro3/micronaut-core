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
package io.micronaut.aop.adapter

import io.micronaut.annotation.processing.test.AbstractTypeElementSpec
import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Requires
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.context.event.StartupEvent
import io.micronaut.core.reflect.ReflectionUtils
import io.micronaut.core.type.TypeInformation
import io.micronaut.inject.AdvisedBeanType
import io.micronaut.inject.BeanDefinition
import org.atinject.jakartatck.auto.events.EventHandlerMultipleArguments
import org.atinject.jakartatck.auto.events.Metadata
import org.atinject.jakartatck.auto.events.SomeEvent

import java.nio.charset.StandardCharsets

class MethodAdapterSpec extends AbstractTypeElementSpec {

    void 'test method adapter with failing requirements is not present'() {
        given:
        def context = buildContext('''
package issue5640;

import io.micronaut.aop.Adapter;
import java.lang.annotation.*;
import io.micronaut.context.annotation.Requires;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import jakarta.inject.Singleton;
import static java.nio.charset.StandardCharsets.US_ASCII;

@Singleton
@Requires(property="not.present")
class AsciiParser {
    @Parse
    public String parseAsAscii(byte[] value) {
        return new String(value, US_ASCII);
    }
}

@Retention(RUNTIME)
@Target({ANNOTATION_TYPE, METHOD})
@Adapter(Parser.class)
@interface Parse {}

interface Parser {
    String parse(byte[] value);
}
''')
        def adaptedType = context.classLoader.loadClass('issue5640.Parser')

        expect:
        !context.containsBean(adaptedType)
        context.getBeansOfType(adaptedType).isEmpty()
    }

    void 'test method adapter with byte[] argument'() {
        given:
        def context = buildContext('issue5054.AsciiParser', '''
package issue5054;

import io.micronaut.aop.Adapter;
import java.lang.annotation.*;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import jakarta.inject.Singleton;
import static java.nio.charset.StandardCharsets.US_ASCII;

@Singleton
class AsciiParser {
    @Parse
    public String parseAsAscii(byte[] value) {
        return new String(value, US_ASCII);
    }
}

@Retention(RUNTIME)
@Target({ANNOTATION_TYPE, METHOD})
@Adapter(Parser.class)
@interface Parse {}

interface Parser {
    String parse(byte[] value);
}
''')
        def adaptedType = context.classLoader.loadClass('issue5054.Parser')
        def parser = context.getBean(adaptedType)
        def beanDef = context.getBeanDefinition(adaptedType)
        def result = parser.parse("test".getBytes(StandardCharsets.US_ASCII))

        expect:
        beanDef.getBeanDescription(TypeInformation.TypeFormat.SHORTENED) == '@j.i.Singleton i.AsciiParser.parseAsAscii()'
        result == 'test'
    }

    void "test method adapter inherits metadata"() {
        when:"An adapter method is parsed that has requirements"
        BeanDefinition definition = buildBeanDefinition('test.Test$ApplicationEventListener$onStartup1$Intercepted','''\
package test;

import io.micronaut.aop.*;
import io.micronaut.inject.annotation.*;
import io.micronaut.context.annotation.*;
import io.micronaut.context.event.*;

@jakarta.inject.Singleton
@io.micronaut.context.annotation.Requires(property="foo.bar")
class Test {

    @Adapter(ApplicationEventListener.class)
    void onStartup(StartupEvent event) {

    }
}

''')
        then:"Then a bean is produced that is valid"
        definition != null
        definition.annotationMetadata.hasAnnotation(Requires)
        definition.annotationMetadata.stringValue(Requires, "property").get() == 'foo.bar'
    }

    void "test method adapter with around overloading"() {
        given:
        def context = buildContext('adapteroverloading.Test', '''
package adapteroverloading;

import io.micronaut.context.event.*;
import io.micronaut.scheduling.annotation.Async;
import jakarta.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import io.micronaut.runtime.event.annotation.*;

@Singleton
public class Test {
    boolean invoked = false;
    boolean shutdown = false;

    public boolean getInvoked() {
        return invoked;
    }
    public boolean isShutdown() {
        return shutdown;
    }

    @EventListener
    void receive(StartupEvent event) {
        invoked = true;
    }

    @EventListener
    void receive(ShutdownEvent event) {
        shutdown = true;
    }
}

''')

        when:
        def bean = context.getBean(context.classLoader.loadClass('adapteroverloading.Test'))

        then:
        bean.invoked

        when:
        context.close()

        then:
        bean.shutdown
    }

    void "test method adapter with around advice"() {
        given:
        def context = buildContext('adapteraround.Test', '''
package adapteraround;

import io.micronaut.context.event.StartupEvent;
import io.micronaut.scheduling.annotation.Async;
import jakarta.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import io.micronaut.runtime.event.annotation.*;

@Singleton
public class Test {

    boolean invoked = false;
    @EventListener
    @Async
    CompletableFuture<Boolean> onStartup(StartupEvent event) {
        invoked = true;
        return CompletableFuture.completedFuture(invoked);
    }

    public boolean getInvoked() {
        return invoked;
    }
}

''')

        def bean = context.getBean(context.classLoader.loadClass('adapteraround.Test'))

        expect:
        bean.invoked

        cleanup:
        context.close()
    }

    void  "test method adapter produces additional bean"() {
        when:"An adapter method is parsed"
        BeanDefinition definition = buildBeanDefinition('test.Test$ApplicationEventListener$onStartup1$Intercepted','''\
package test;

import io.micronaut.aop.*;
import io.micronaut.inject.annotation.*;
import io.micronaut.context.annotation.*;
import io.micronaut.context.event.*;

@jakarta.inject.Singleton
class Test {

    @Adapter(ApplicationEventListener.class)
    void onStartup(StartupEvent event) {

    }
}

''')
        then:"Then a bean is produced that is valid"
        definition != null
        !(definition instanceof AdvisedBeanType)
        ApplicationEventListener.isAssignableFrom(definition.getBeanType())
        !definition.getTypeArguments(ApplicationEventListener).isEmpty()
        definition.getTypeArguments(ApplicationEventListener).get(0).type == StartupEvent
    }

    void "test method adapter inherited from an interface produces additional bean"() {
        when:"An adapter method is parsed"
        BeanDefinition definition = buildBeanDefinition('test.Test$ApplicationEventListener$onStartup1$Intercepted','''\
package test;

import io.micronaut.aop.*;
import io.micronaut.inject.annotation.*;
import io.micronaut.context.annotation.*;
import io.micronaut.context.event.*;

@jakarta.inject.Singleton
class Test implements TestContract {

    @Override
    public void onStartup(StartupEvent event) {

    }
}

interface TestContract {

    @Adapter(ApplicationEventListener.class)
    void onStartup(StartupEvent event);
}

''')
        then:"Then a bean is produced that is valid"
        definition != null
        ApplicationEventListener.isAssignableFrom(definition.getBeanType())
        !definition.getTypeArguments(ApplicationEventListener).isEmpty()
        definition.getTypeArguments(ApplicationEventListener).get(0).type == StartupEvent
    }

    void  "test method adapter honours type restraints - correct path"() {
        when:"An adapter method is parsed"
        BeanDefinition definition = buildBeanDefinition('test.Test$Foo$myMethod1$Intercepted','''\
package test;

import io.micronaut.aop.*;
import io.micronaut.inject.annotation.*;
import io.micronaut.context.annotation.*;
import io.micronaut.context.event.*;

@jakarta.inject.Singleton
class Test {

    @Adapter(Foo.class)
    void myMethod(String blah) {

    }
}

interface Foo<T extends CharSequence> extends java.util.function.Consumer<T> {}
''')
        then:"Then a bean is produced that is valid"
        definition != null
        ReflectionUtils.getAllInterfaces(definition.getBeanType()).find { it.name == 'test.Foo'}
        !definition.getTypeArguments("test.Foo").isEmpty()
        definition.getTypeArguments("test.Foo").get(0).type == String

    }

    void  "test method adapter honours type restraints - compilation error"() {
        when:"An adapter method is parsed"
        BeanDefinition definition = buildBeanDefinition('test.Test$Foo$myMethod$Intercepted','''\
package test;

import io.micronaut.aop.*;
import io.micronaut.inject.annotation.*;
import io.micronaut.context.annotation.*;
import io.micronaut.context.event.*;

@jakarta.inject.Singleton
class Test {

    @Adapter(Foo.class)
    void myMethod(Integer blah) {

    }
}

interface Foo<T extends CharSequence> extends java.util.function.Consumer<T> {}
''')
        then:"An error occurs"
        def e = thrown(RuntimeException)
        e.message.contains 'Cannot adapt method [myMethod(java.lang.Integer)] to target method [accept(T)]. Type [java.lang.Integer] is not a subtype of type [java.lang.CharSequence] for argument at position 0'
    }

    void  "test method adapter wrong argument count"() {
        when:"An adapter method is parsed"
        buildBeanDefinition('test.Test$ApplicationEventListener$onStartup$Intercepted','''\
package test;

import io.micronaut.aop.*;
import io.micronaut.inject.annotation.*;
import io.micronaut.context.annotation.*;
import io.micronaut.context.event.*;

@jakarta.inject.Singleton
class Test {

    @Adapter(ApplicationEventListener.class)
    void onStartup(StartupEvent event, boolean stuff) {

    }
}

''')
        then:"Then a bean is produced that is valid"
        def e = thrown(RuntimeException)
        e.message.contains("Cannot adapt method [onStartup(io.micronaut.context.event.StartupEvent,boolean)] to target method [onApplicationEvent(E)]. Argument lengths don't match.")

    }

    void  "test method adapter argument order"() {
        when:"An adapter method is parsed"
        BeanDefinition definition = buildBeanDefinition('org.atinject.jakartatck.auto.events.EventListener$EventHandlerMultipleArguments$onEvent1$Intercepted','''\
package org.atinject.jakartatck.auto.events;

@jakarta.inject.Singleton
class EventListener {

    @EventHandler
    public void onEvent(Metadata metadata, SomeEvent event) {
    }

}

''')
        then:"Then a bean is produced that is valid"
        definition != null
        EventHandlerMultipleArguments.isAssignableFrom(definition.getBeanType())
        definition.getTypeArguments(EventHandlerMultipleArguments).size() == 2
        definition.getTypeArguments(EventHandlerMultipleArguments).get(0).type == Metadata
        definition.getTypeArguments(EventHandlerMultipleArguments).get(1).type == SomeEvent

    }


    void "test adapter is invoked"() {
        given:
        ApplicationContext ctx = ApplicationContext.run(["spec" : getClass().getSimpleName()])

        when:
        Test t = ctx.getBean(Test)

        then:
        t.invoked

        cleanup:
        ctx.close()
    }
}
