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
package org.atinject.jakartatck.auto

import jakarta.inject.Inject
import jakarta.inject.Named
import jakarta.inject.Provider
import junit.framework.TestCase

class Convertible implements Car {

    @Inject
    @org.atinject.jakartatck.auto.Drivers
    Seat driversSeatA
    @Inject
    @org.atinject.jakartatck.auto.Drivers
    Seat driversSeatB
    @Inject
    org.atinject.jakartatck.auto.accessories.SpareTire spareTire
    @Inject
    org.atinject.jakartatck.auto.accessories.Cupholder cupholder
    @Inject
    Provider<Engine> engineProvider

    private boolean methodWithZeroParamsInjected
    private boolean methodWithMultipleParamsInjected
    private boolean methodWithNonVoidReturnInjected

    private Seat constructorPlainSeat
    private Seat constructorDriversSeat
    private Tire constructorPlainTire
    private Tire constructorSpareTire
    private Provider<Seat> constructorPlainSeatProvider = nullProvider()
    private Provider<Seat> constructorDriversSeatProvider = nullProvider()
    private Provider<Tire> constructorPlainTireProvider = nullProvider()
    private Provider<Tire> constructorSpareTireProvider = nullProvider()

    @Inject
    Seat fieldPlainSeat
    @Inject
    @org.atinject.jakartatck.auto.Drivers
    Seat fieldDriversSeat
    @Inject
    Tire fieldPlainTire
    @Inject
    @Named('spare')
    Tire fieldSpareTire
    @Inject
    Provider<Seat> fieldPlainSeatProvider = nullProvider()
    @Inject
    @org.atinject.jakartatck.auto.Drivers
    Provider<Seat> fieldDriversSeatProvider = nullProvider()
    @Inject
    Provider<Tire> fieldPlainTireProvider = nullProvider()
    @Inject
    @Named('spare')
    Provider<Tire> fieldSpareTireProvider = nullProvider()

    private Seat methodPlainSeat
    private Seat methodDriversSeat
    private Tire methodPlainTire
    private Tire methodSpareTire
    private Provider<Seat> methodPlainSeatProvider = nullProvider()
    private Provider<Seat> methodDriversSeatProvider = nullProvider()
    private Provider<Tire> methodPlainTireProvider = nullProvider()
    private Provider<Tire> methodSpareTireProvider = nullProvider()

    @Inject
    static Seat staticFieldPlainSeat
    @Inject
    @org.atinject.jakartatck.auto.Drivers
    static Seat staticFieldDriversSeat
    @Inject
    static Tire staticFieldPlainTire
    @Inject
    @Named('spare')
    static Tire staticFieldSpareTire
    @Inject
    static Provider<Seat> staticFieldPlainSeatProvider = nullProvider()
    @Inject
    @org.atinject.jakartatck.auto.Drivers
    static Provider<Seat> staticFieldDriversSeatProvider = nullProvider()
    @Inject
    static Provider<Tire> staticFieldPlainTireProvider = nullProvider()
    @Inject
    @Named('spare')
    static Provider<Tire> staticFieldSpareTireProvider = nullProvider()

    private static Seat staticMethodPlainSeat
    private static Seat staticMethodDriversSeat
    private static Tire staticMethodPlainTire
    private static Tire staticMethodSpareTire
    private static Provider<Seat> staticMethodPlainSeatProvider = nullProvider()
    private static Provider<Seat> staticMethodDriversSeatProvider = nullProvider()
    private static Provider<Tire> staticMethodPlainTireProvider = nullProvider()
    private static Provider<Tire> staticMethodSpareTireProvider = nullProvider()

    @Inject
    Convertible(
            Seat plainSeat,
            @org.atinject.jakartatck.auto.Drivers Seat driversSeat,
            Tire plainTire,
            @Named('spare') Tire spareTire,
            Provider<Seat> plainSeatProvider,
            @org.atinject.jakartatck.auto.Drivers Provider<Seat> driversSeatProvider,
            Provider<Tire> plainTireProvider,
            @Named('spare') Provider<Tire> spareTireProvider) {
        constructorPlainSeat = plainSeat
        constructorDriversSeat = driversSeat
        constructorPlainTire = plainTire
        constructorSpareTire = spareTire
        constructorPlainSeatProvider = plainSeatProvider
        constructorDriversSeatProvider = driversSeatProvider
        constructorPlainTireProvider = plainTireProvider
        constructorSpareTireProvider = spareTireProvider
    }

    Convertible() {
        throw new AssertionError('Unexpected call to non-injectable constructor')
    }

    void setSeat(Seat unused) {
        throw new AssertionError('Unexpected call to non-injectable method')
    }

    @Inject
    void injectMethodWithZeroArgs() {
        methodWithZeroParamsInjected = true
    }

    @Inject
    String injectMethodWithNonVoidReturn() {
        methodWithNonVoidReturnInjected = true
        return 'unused'
    }

    @Inject
    void injectInstanceMethodWithManyArgs(
            Seat plainSeat,
            @org.atinject.jakartatck.auto.Drivers Seat driversSeat,
            Tire plainTire,
            @Named('spare') Tire spareTire,
            Provider<Seat> plainSeatProvider,
            @org.atinject.jakartatck.auto.Drivers Provider<Seat> driversSeatProvider,
            Provider<Tire> plainTireProvider,
            @Named('spare') Provider<Tire> spareTireProvider) {
        methodWithMultipleParamsInjected = true

        methodPlainSeat = plainSeat
        methodDriversSeat = driversSeat
        methodPlainTire = plainTire
        methodSpareTire = spareTire
        methodPlainSeatProvider = plainSeatProvider
        methodDriversSeatProvider = driversSeatProvider
        methodPlainTireProvider = plainTireProvider
        methodSpareTireProvider = spareTireProvider
    }

    @Inject
    static void injectStaticMethodWithManyArgs(
            Seat plainSeat,
            @org.atinject.jakartatck.auto.Drivers Seat driversSeat,
            Tire plainTire,
            @Named('spare') Tire spareTire,
            Provider<Seat> plainSeatProvider,
            @org.atinject.jakartatck.auto.Drivers Provider<Seat> driversSeatProvider,
            Provider<Tire> plainTireProvider,
            @Named('spare') Provider<Tire> spareTireProvider) {
        staticMethodPlainSeat = plainSeat
        staticMethodDriversSeat = driversSeat
        staticMethodPlainTire = plainTire
        staticMethodSpareTire = spareTire
        staticMethodPlainSeatProvider = plainSeatProvider
        staticMethodDriversSeatProvider = driversSeatProvider
        staticMethodPlainTireProvider = plainTireProvider
        staticMethodSpareTireProvider = spareTireProvider
    }

    /**
     * Returns a provider that always returns null. This is used as a default
     * value to avoid null checks for omitted provider injections.
     */
    private static <T> Provider<T> nullProvider() {
        return () -> null
    }

    public static ThreadLocal<Convertible> localConvertible
            = new ThreadLocal<>()

    static class Tests extends TestCase {

        private final Convertible car = localConvertible.get()
        private final org.atinject.jakartatck.auto.accessories.Cupholder cupholder = car.cupholder
        private final org.atinject.jakartatck.auto.accessories.SpareTire spareTire = car.spareTire
        private final Tire plainTire = car.fieldPlainTire
        private final Engine engine = car.engineProvider.get()

        // smoke tests: if these fail all bets are off

        void testFieldsInjected() {
            assertTrue(cupholder != null && spareTire != null)
        }

        void testProviderReturnedValues() {
            assertTrue(engine != null)
        }

        // injecting different kinds of members

        void testMethodWithZeroParametersInjected() {
            assertTrue(car.methodWithZeroParamsInjected)
        }

        void testMethodWithMultipleParametersInjected() {
            assertTrue(car.methodWithMultipleParamsInjected)
        }

        void testNonVoidMethodInjected() {
            assertTrue(car.methodWithNonVoidReturnInjected)
        }

        void testPublicNoArgsConstructorInjected() {
            assertTrue(engine.publicNoArgsConstructorInjected)
        }

        void testSubtypeFieldsInjected() {
            assertTrue(spareTire.hasSpareTireBeenFieldInjected())
        }

        void testSubtypeMethodsInjected() {
            assertTrue(spareTire.hasSpareTireBeenMethodInjected())
        }

        void testSupertypeFieldsInjected() {
            assertTrue(spareTire.hasTireBeenFieldInjected())
        }

        void testSupertypeMethodsInjected() {
            assertTrue(spareTire.hasTireBeenMethodInjected())
        }

        void testTwiceOverriddenMethodInjectedWhenMiddleLacksAnnotation() {
            assertTrue(engine.overriddenTwiceWithOmissionInMiddleInjected)
        }

        // injected values

        void testQualifiersNotInheritedFromOverriddenMethod() {
            assertFalse(engine.qualifiersInheritedFromOverriddenMethod)
        }

        void testConstructorInjectionWithValues() {
            assertFalse('Expected unqualified value',
                    car.constructorPlainSeat instanceof DriversSeat)
            assertFalse('Expected unqualified value',
                    car.constructorPlainTire instanceof org.atinject.jakartatck.auto.accessories.SpareTire)
            assertTrue('Expected qualified value',
                    car.constructorDriversSeat instanceof DriversSeat)
            assertTrue('Expected qualified value',
                    car.constructorSpareTire instanceof org.atinject.jakartatck.auto.accessories.SpareTire)
        }

        void testFieldInjectionWithValues() {
            assertFalse('Expected unqualified value',
                    car.fieldPlainSeat instanceof DriversSeat)
            assertFalse('Expected unqualified value',
                    car.fieldPlainTire instanceof org.atinject.jakartatck.auto.accessories.SpareTire)
            assertTrue('Expected qualified value',
                    car.fieldDriversSeat instanceof DriversSeat)
            assertTrue('Expected qualified value',
                    car.fieldSpareTire instanceof org.atinject.jakartatck.auto.accessories.SpareTire)
        }

        void testMethodInjectionWithValues() {
            assertFalse('Expected unqualified value',
                    car.methodPlainSeat instanceof DriversSeat)
            assertFalse('Expected unqualified value',
                    car.methodPlainTire instanceof org.atinject.jakartatck.auto.accessories.SpareTire)
            assertTrue('Expected qualified value',
                    car.methodDriversSeat instanceof DriversSeat)
            assertTrue('Expected qualified value',
                    car.methodSpareTire instanceof org.atinject.jakartatck.auto.accessories.SpareTire)
        }

        // injected providers

        void testConstructorInjectionWithProviders() {
            assertFalse('Expected unqualified value',
                    car.constructorPlainSeatProvider.get() instanceof DriversSeat)
            assertFalse('Expected unqualified value',
                    car.constructorPlainTireProvider.get() instanceof org.atinject.jakartatck.auto.accessories.SpareTire)
            assertTrue('Expected qualified value',
                    car.constructorDriversSeatProvider.get() instanceof DriversSeat)
            assertTrue('Expected qualified value',
                    car.constructorSpareTireProvider.get() instanceof org.atinject.jakartatck.auto.accessories.SpareTire)
        }

        void testFieldInjectionWithProviders() {
            assertFalse('Expected unqualified value',
                    car.fieldPlainSeatProvider.get() instanceof DriversSeat)
            assertFalse('Expected unqualified value',
                    car.fieldPlainTireProvider.get() instanceof org.atinject.jakartatck.auto.accessories.SpareTire)
            assertTrue('Expected qualified value',
                    car.fieldDriversSeatProvider.get() instanceof DriversSeat)
            assertTrue('Expected qualified value',
                    car.fieldSpareTireProvider.get() instanceof org.atinject.jakartatck.auto.accessories.SpareTire)
        }

        void testMethodInjectionWithProviders() {
            assertFalse('Expected unqualified value',
                    car.methodPlainSeatProvider.get() instanceof DriversSeat)
            assertFalse('Expected unqualified value',
                    car.methodPlainTireProvider.get() instanceof org.atinject.jakartatck.auto.accessories.SpareTire)
            assertTrue('Expected qualified value',
                    car.methodDriversSeatProvider.get() instanceof DriversSeat)
            assertTrue('Expected qualified value',
                    car.methodSpareTireProvider.get() instanceof org.atinject.jakartatck.auto.accessories.SpareTire)
        }


        // singletons

        void testConstructorInjectedProviderYieldsSingleton() {
            assertSame('Expected same value',
                    car.constructorPlainSeatProvider.get(), car.constructorPlainSeatProvider.get())
        }

        void testFieldInjectedProviderYieldsSingleton() {
            assertSame('Expected same value',
                    car.fieldPlainSeatProvider.get(), car.fieldPlainSeatProvider.get())
        }

        void testMethodInjectedProviderYieldsSingleton() {
            assertSame('Expected same value',
                    car.methodPlainSeatProvider.get(), car.methodPlainSeatProvider.get())
        }

        void testCircularlyDependentSingletons() {
            // uses provider.get() to get around circular deps
            assertSame(cupholder.seatProvider.get().cupholder, cupholder)
        }


        // non singletons

        void testSingletonAnnotationNotInheritedFromSupertype() {
            assertNotSame(car.driversSeatA, car.driversSeatB)
        }

        void testConstructorInjectedProviderYieldsDistinctValues() {
            assertNotSame('Expected distinct values',
                    car.constructorDriversSeatProvider.get(), car.constructorDriversSeatProvider.get())
            assertNotSame('Expected distinct values',
                    car.constructorPlainTireProvider.get(), car.constructorPlainTireProvider.get())
            assertNotSame('Expected distinct values',
                    car.constructorSpareTireProvider.get(), car.constructorSpareTireProvider.get())
        }

        void testFieldInjectedProviderYieldsDistinctValues() {
            assertNotSame('Expected distinct values',
                    car.fieldDriversSeatProvider.get(), car.fieldDriversSeatProvider.get())
            assertNotSame('Expected distinct values',
                    car.fieldPlainTireProvider.get(), car.fieldPlainTireProvider.get())
            assertNotSame('Expected distinct values',
                    car.fieldSpareTireProvider.get(), car.fieldSpareTireProvider.get())
        }

        void testMethodInjectedProviderYieldsDistinctValues() {
            assertNotSame('Expected distinct values',
                    car.methodDriversSeatProvider.get(), car.methodDriversSeatProvider.get())
            assertNotSame('Expected distinct values',
                    car.methodPlainTireProvider.get(), car.methodPlainTireProvider.get())
            assertNotSame('Expected distinct values',
                    car.methodSpareTireProvider.get(), car.methodSpareTireProvider.get())
        }


        // mix inheritance + visibility

        void testPackagePrivateMethodInjectedDifferentPackages() {
            assertTrue(spareTire.subPackagePrivateMethodInjected)
            assertTrue(spareTire.superPackagePrivateMethodInjected)
        }

        void testOverriddenProtectedMethodInjection() {
            assertTrue(spareTire.subProtectedMethodInjected)
            assertFalse(spareTire.superProtectedMethodInjected)
        }

        void testOverriddenPublicMethodNotInjected() {
            assertTrue(spareTire.subPublicMethodInjected)
            assertFalse(spareTire.superPublicMethodInjected)
        }


        // inject in order

        void testFieldsInjectedBeforeMethods() {
            assertFalse(spareTire.methodInjectedBeforeFields)
        }

        void testSupertypeMethodsInjectedBeforeSubtypeFields() {
            assertFalse(spareTire.subtypeFieldInjectedBeforeSupertypeMethods)
        }

        void testSupertypeMethodInjectedBeforeSubtypeMethods() {
            assertFalse(spareTire.subtypeMethodInjectedBeforeSupertypeMethods)
        }


        // necessary injections occur

        void testPackagePrivateMethodInjectedEvenWhenSimilarMethodLacksAnnotation() {
            assertTrue(spareTire.subPackagePrivateMethodForOverrideInjected)
        }


        // override or similar method without @Inject

        void testPrivateMethodNotInjectedWhenSupertypeHasAnnotatedSimilarMethod() {
            assertFalse(spareTire.superPrivateMethodForOverrideInjected)
        }

        void testPackagePrivateMethodNotInjectedWhenOverrideLacksAnnotation() {
            assertFalse(engine.subPackagePrivateMethodForOverrideInjected)
            assertFalse(engine.superPackagePrivateMethodForOverrideInjected)
        }

        void testPackagePrivateMethodNotInjectedWhenSupertypeHasAnnotatedSimilarMethod() {
            assertFalse(spareTire.superPackagePrivateMethodForOverrideInjected)
        }

        void testProtectedMethodNotInjectedWhenOverrideNotAnnotated() {
            assertFalse(spareTire.protectedMethodForOverrideInjected)
        }

        void testPublicMethodNotInjectedWhenOverrideNotAnnotated() {
            assertFalse(spareTire.publicMethodForOverrideInjected)
        }

        void testTwiceOverriddenMethodNotInjectedWhenOverrideLacksAnnotation() {
            assertFalse(engine.overriddenTwiceWithOmissionInSubclassInjected)
        }

        void testOverriddingMixedWithPackagePrivate2() {
            assertTrue(spareTire.packagePrivateMethod2Injected)
            assertTrue(((Tire) spareTire).packagePrivateMethod2Injected)
            assertFalse(((org.atinject.jakartatck.auto.accessories.RoundThing) spareTire).packagePrivateMethod2Injected)

            assertTrue(plainTire.packagePrivateMethod2Injected)
            assertTrue(((org.atinject.jakartatck.auto.accessories.RoundThing) plainTire).packagePrivateMethod2Injected)
        }

        void testOverriddingMixedWithPackagePrivate3() {
            assertFalse(spareTire.packagePrivateMethod3Injected)
            assertTrue(((Tire) spareTire).packagePrivateMethod3Injected)
            assertFalse(((org.atinject.jakartatck.auto.accessories.RoundThing) spareTire).packagePrivateMethod3Injected)

            assertTrue(plainTire.packagePrivateMethod3Injected)
            assertTrue(((org.atinject.jakartatck.auto.accessories.RoundThing) plainTire).packagePrivateMethod3Injected)
        }

        void testOverriddingMixedWithPackagePrivate4() {
            assertFalse(plainTire.packagePrivateMethod4Injected)
            assertTrue(((org.atinject.jakartatck.auto.accessories.RoundThing) plainTire).packagePrivateMethod4Injected)
        }

        // inject only once

        void testOverriddenPackagePrivateMethodInjectedOnlyOnce() {
            assertFalse(engine.overriddenPackagePrivateMethodInjectedTwice)
        }

        void testSimilarPackagePrivateMethodInjectedOnlyOnce() {
            assertFalse(spareTire.similarPackagePrivateMethodInjectedTwice)
        }

        void testOverriddenProtectedMethodInjectedOnlyOnce() {
            assertFalse(spareTire.overriddenProtectedMethodInjectedTwice)
        }

        void testOverriddenPublicMethodInjectedOnlyOnce() {
            assertFalse(spareTire.overriddenPublicMethodInjectedTwice)
        }

    }

    static class StaticTests extends TestCase {

        void testSubtypeStaticFieldsInjected() {
            assertTrue(org.atinject.jakartatck.auto.accessories.SpareTire.hasBeenStaticFieldInjected())
        }

        void testSubtypeStaticMethodsInjected() {
            assertTrue(org.atinject.jakartatck.auto.accessories.SpareTire.hasBeenStaticMethodInjected())
        }

        void testSupertypeStaticFieldsInjected() {
            assertTrue(Tire.hasBeenStaticFieldInjected())
        }

        void testSupertypeStaticMethodsInjected() {
            assertTrue(Tire.hasBeenStaticMethodInjected())
        }

        void testStaticFieldInjectionWithValues() {
            assertFalse('Expected unqualified value',
                    staticFieldPlainSeat instanceof DriversSeat)
            assertFalse('Expected unqualified value',
                    staticFieldPlainTire instanceof org.atinject.jakartatck.auto.accessories.SpareTire)
            assertTrue('Expected qualified value',
                    staticFieldDriversSeat instanceof DriversSeat)
            assertTrue('Expected qualified value',
                    staticFieldSpareTire instanceof org.atinject.jakartatck.auto.accessories.SpareTire)
        }

        void testStaticMethodInjectionWithValues() {
            assertFalse('Expected unqualified value',
                    staticMethodPlainSeat instanceof DriversSeat)
            assertFalse('Expected unqualified value',
                    staticMethodPlainTire instanceof org.atinject.jakartatck.auto.accessories.SpareTire)
            assertTrue('Expected qualified value',
                    staticMethodDriversSeat instanceof DriversSeat)
            assertTrue('Expected qualified value',
                    staticMethodSpareTire instanceof org.atinject.jakartatck.auto.accessories.SpareTire)
        }

        void testStaticFieldsInjectedBeforeMethods() {
            assertFalse(org.atinject.jakartatck.auto.accessories.SpareTire.staticMethodInjectedBeforeStaticFields)
        }

        void testSupertypeStaticMethodsInjectedBeforeSubtypeStaticFields() {
            assertFalse(org.atinject.jakartatck.auto.accessories.SpareTire.subtypeStaticFieldInjectedBeforeSupertypeStaticMethods)
        }

        void testSupertypeStaticMethodsInjectedBeforeSubtypeStaticMethods() {
            assertFalse(org.atinject.jakartatck.auto.accessories.SpareTire.subtypeStaticMethodInjectedBeforeSupertypeStaticMethods)
        }

        void testStaticFieldInjectionWithProviders() {
            assertFalse('Expected unqualified value',
                    staticFieldPlainSeatProvider.get() instanceof DriversSeat)
            assertFalse('Expected unqualified value',
                    staticFieldPlainTireProvider.get() instanceof org.atinject.jakartatck.auto.accessories.SpareTire)
            assertTrue('Expected qualified value',
                    staticFieldDriversSeatProvider.get() instanceof DriversSeat)
            assertTrue('Expected qualified value',
                    staticFieldSpareTireProvider.get() instanceof org.atinject.jakartatck.auto.accessories.SpareTire)
        }

        void testStaticMethodInjectionWithProviders() {
            assertFalse('Expected unqualified value',
                    staticMethodPlainSeatProvider.get() instanceof DriversSeat)
            assertFalse('Expected unqualified value',
                    staticMethodPlainTireProvider.get() instanceof org.atinject.jakartatck.auto.accessories.SpareTire)
            assertTrue('Expected qualified value',
                    staticMethodDriversSeatProvider.get() instanceof DriversSeat)
            assertTrue('Expected qualified value',
                    staticMethodSpareTireProvider.get() instanceof org.atinject.jakartatck.auto.accessories.SpareTire)
        }
    }

    static class PrivateTests extends TestCase {

        private final Convertible car = localConvertible.get()
        private final Engine engine = car.engineProvider.get()
        private final org.atinject.jakartatck.auto.accessories.SpareTire spareTire = car.spareTire

        void testSupertypePrivateMethodInjected() {
            assertTrue(spareTire.superPrivateMethodInjected)
            assertTrue(spareTire.subPrivateMethodInjected)
        }

        void testPackagePrivateMethodInjectedSamePackage() {
            assertTrue(engine.subPackagePrivateMethodInjected)
            assertFalse(engine.superPackagePrivateMethodInjected)
        }

        void testPrivateMethodInjectedEvenWhenSimilarMethodLacksAnnotation() {
            assertTrue(spareTire.subPrivateMethodForOverrideInjected)
        }

        void testSimilarPrivateMethodInjectedOnlyOnce() {
            assertFalse(spareTire.similarPrivateMethodInjectedTwice)
        }
    }
}
