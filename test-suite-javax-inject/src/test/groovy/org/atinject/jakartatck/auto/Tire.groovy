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

class Tire extends org.atinject.jakartatck.auto.accessories.RoundThing {

    protected static final org.atinject.jakartatck.auto.FuelTank NEVER_INJECTED = new org.atinject.jakartatck.auto.FuelTank()

    protected static final Set<String> moreProblems = new LinkedHashSet<>()

    org.atinject.jakartatck.auto.FuelTank constructorInjection = NEVER_INJECTED
    @Inject
    org.atinject.jakartatck.auto.FuelTank fieldInjection = NEVER_INJECTED
    org.atinject.jakartatck.auto.FuelTank methodInjection = NEVER_INJECTED
    @Inject
    static org.atinject.jakartatck.auto.FuelTank staticFieldInjection = NEVER_INJECTED
    static org.atinject.jakartatck.auto.FuelTank staticMethodInjection = NEVER_INJECTED

    boolean constructorInjected

    protected boolean superPrivateMethodInjected
    protected boolean superPackagePrivateMethodInjected
    protected boolean superProtectedMethodInjected
    protected boolean superPublicMethodInjected
    protected boolean subPrivateMethodInjected
    protected boolean subPackagePrivateMethodInjected
    protected boolean subProtectedMethodInjected
    protected boolean subPublicMethodInjected

    protected boolean superPrivateMethodForOverrideInjected
    protected boolean superPackagePrivateMethodForOverrideInjected
    protected boolean subPrivateMethodForOverrideInjected
    protected boolean subPackagePrivateMethodForOverrideInjected
    protected boolean protectedMethodForOverrideInjected
    protected boolean publicMethodForOverrideInjected

    public boolean methodInjectedBeforeFields
    public boolean subtypeFieldInjectedBeforeSupertypeMethods
    public boolean subtypeMethodInjectedBeforeSupertypeMethods
    public static boolean staticMethodInjectedBeforeStaticFields
    public static boolean subtypeStaticFieldInjectedBeforeSupertypeStaticMethods
    public static boolean subtypeStaticMethodInjectedBeforeSupertypeStaticMethods
    public boolean similarPrivateMethodInjectedTwice
    public boolean similarPackagePrivateMethodInjectedTwice
    public boolean overriddenProtectedMethodInjectedTwice
    public boolean overriddenPublicMethodInjectedTwice

    @Inject
    Tire(org.atinject.jakartatck.auto.FuelTank constructorInjection) {
        this.constructorInjection = constructorInjection
    }

    @Inject
    void supertypeMethodInjection(org.atinject.jakartatck.auto.FuelTank methodInjection) {
        if (!hasTireBeenFieldInjected()) {
            methodInjectedBeforeFields = true
        }
        if (hasSpareTireBeenFieldInjected()) {
            subtypeFieldInjectedBeforeSupertypeMethods = true
        }
        if (hasSpareTireBeenMethodInjected()) {
            subtypeMethodInjectedBeforeSupertypeMethods = true
        }
        this.methodInjection = methodInjection
    }

    @Inject
    static void supertypeStaticMethodInjection(org.atinject.jakartatck.auto.FuelTank methodInjection) {
        if (!Tire.hasBeenStaticFieldInjected()) {
            staticMethodInjectedBeforeStaticFields = true
        }
        if (org.atinject.jakartatck.auto.accessories.SpareTire.hasBeenStaticFieldInjected()) {
            subtypeStaticFieldInjectedBeforeSupertypeStaticMethods = true
        }
        if (org.atinject.jakartatck.auto.accessories.SpareTire.hasBeenStaticMethodInjected()) {
            subtypeStaticMethodInjectedBeforeSupertypeStaticMethods = true
        }
        staticMethodInjection = methodInjection
    }

    @Inject
    private void injectPrivateMethod() {
        if (superPrivateMethodInjected) {
            similarPrivateMethodInjectedTwice = true
        }
        superPrivateMethodInjected = true
    }

    @Inject
    void injectPackagePrivateMethod() {
        if (superPackagePrivateMethodInjected) {
            similarPackagePrivateMethodInjectedTwice = true
        }
        superPackagePrivateMethodInjected = true
    }

    @Inject
    protected void injectProtectedMethod() {
        if (superProtectedMethodInjected) {
            overriddenProtectedMethodInjectedTwice = true
        }
        superProtectedMethodInjected = true
    }

    @Inject
    void injectPublicMethod() {
        if (superPublicMethodInjected) {
            overriddenPublicMethodInjectedTwice = true
        }
        superPublicMethodInjected = true
    }

    @Inject
    private void injectPrivateMethodForOverride() {
        subPrivateMethodForOverrideInjected = true
    }

    @Inject
    void injectPackagePrivateMethodForOverride() {
        subPackagePrivateMethodForOverrideInjected = true
    }

    @Inject
    protected void injectProtectedMethodForOverride() {
        protectedMethodForOverrideInjected = true
    }

    @Inject
    void injectPublicMethodForOverride() {
        publicMethodForOverrideInjected = true
    }

    protected final boolean hasTireBeenFieldInjected() {
        return fieldInjection != NEVER_INJECTED
    }

    protected boolean hasSpareTireBeenFieldInjected() {
        return false
    }

    protected final boolean hasTireBeenMethodInjected() {
        return methodInjection != NEVER_INJECTED
    }

    protected static boolean hasBeenStaticFieldInjected() {
        return staticFieldInjection != NEVER_INJECTED
    }

    protected static boolean hasBeenStaticMethodInjected() {
        return staticMethodInjection != NEVER_INJECTED
    }

    protected boolean hasSpareTireBeenMethodInjected() {
        return false
    }

    boolean packagePrivateMethod2Injected

    @Inject
    void injectPackagePrivateMethod2() {
        packagePrivateMethod2Injected = true
    }

    public boolean packagePrivateMethod3Injected

    @Inject
    void injectPackagePrivateMethod3() {
        packagePrivateMethod3Injected = true
    }

    public boolean packagePrivateMethod4Injected

    void injectPackagePrivateMethod4() {
        packagePrivateMethod4Injected = true
    }
}
