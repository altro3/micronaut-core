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
package org.atinject.jakartatck.auto.accessories

import jakarta.inject.Inject

class SpareTire extends org.atinject.jakartatck.auto.Tire {

    org.atinject.jakartatck.auto.FuelTank constructorInjection = NEVER_INJECTED
    @Inject
    org.atinject.jakartatck.auto.FuelTank fieldInjection = NEVER_INJECTED
    org.atinject.jakartatck.auto.FuelTank methodInjection = NEVER_INJECTED
    @Inject
    static org.atinject.jakartatck.auto.FuelTank staticFieldInjection = NEVER_INJECTED
    static org.atinject.jakartatck.auto.FuelTank staticMethodInjection = NEVER_INJECTED

    @Inject
    SpareTire(org.atinject.jakartatck.auto.FuelTank forSupertype, org.atinject.jakartatck.auto.FuelTank forSubtype) {
        super(forSupertype)
        this.constructorInjection = forSubtype
    }

    @Inject
    void subtypeMethodInjection(org.atinject.jakartatck.auto.FuelTank methodInjection) {
        if (!hasSpareTireBeenFieldInjected()) {
            methodInjectedBeforeFields = true
        }
        this.methodInjection = methodInjection
    }

    @Inject
    static void subtypeStaticMethodInjection(org.atinject.jakartatck.auto.FuelTank methodInjection) {
        if (!hasBeenStaticFieldInjected()) {
            staticMethodInjectedBeforeStaticFields = true
        }
        staticMethodInjection = methodInjection
    }

    @Inject
    private void injectPrivateMethod() {
        if (subPrivateMethodInjected) {
            similarPrivateMethodInjectedTwice = true
        }
        subPrivateMethodInjected = true
    }

    @Inject
    void injectPackagePrivateMethod() {
        if (subPackagePrivateMethodInjected) {
            similarPackagePrivateMethodInjectedTwice = true
        }
        subPackagePrivateMethodInjected = true
    }

    @Override
    @Inject
    protected void injectProtectedMethod() {
        if (subProtectedMethodInjected) {
            overriddenProtectedMethodInjectedTwice = true
        }
        subProtectedMethodInjected = true
    }

    @Override
    @Inject
    void injectPublicMethod() {
        if (subPublicMethodInjected) {
            overriddenPublicMethodInjectedTwice = true
        }
        subPublicMethodInjected = true
    }

    private void injectPrivateMethodForOverride() {
        superPrivateMethodForOverrideInjected = true
    }

    void injectPackagePrivateMethodForOverride() {
        superPackagePrivateMethodForOverrideInjected = true
    }

    @Override
    protected void injectProtectedMethodForOverride() {
        protectedMethodForOverrideInjected = true
    }

    @Override
    void injectPublicMethodForOverride() {
        publicMethodForOverrideInjected = true
    }

    @Override
    boolean hasSpareTireBeenFieldInjected() {
        return fieldInjection != NEVER_INJECTED
    }

    @Override
    boolean hasSpareTireBeenMethodInjected() {
        return methodInjection != NEVER_INJECTED
    }

    static boolean hasBeenStaticFieldInjected() {
        return staticFieldInjection != NEVER_INJECTED
    }

    static boolean hasBeenStaticMethodInjected() {
        return staticMethodInjection != NEVER_INJECTED
    }

    public boolean packagePrivateMethod2Injected

    @Inject
    void injectPackagePrivateMethod2() {
        packagePrivateMethod2Injected = true
    }

    public boolean packagePrivateMethod3Injected

    void injectPackagePrivateMethod3() {
        packagePrivateMethod3Injected = true
    }
}
