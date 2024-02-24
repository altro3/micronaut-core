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

abstract class Engine {

    protected boolean publicNoArgsConstructorInjected
    protected boolean subPackagePrivateMethodInjected
    protected boolean superPackagePrivateMethodInjected
    protected boolean subPackagePrivateMethodForOverrideInjected
    protected boolean superPackagePrivateMethodForOverrideInjected

    protected boolean overriddenTwiceWithOmissionInMiddleInjected
    protected boolean overriddenTwiceWithOmissionInSubclassInjected

    protected Seat seatA
    protected Seat seatB
    protected org.atinject.jakartatck.auto.Tire tireA
    protected org.atinject.jakartatck.auto.Tire tireB

    public boolean overriddenPackagePrivateMethodInjectedTwice
    public boolean qualifiersInheritedFromOverriddenMethod

    @Inject
    void injectPackagePrivateMethod() {
        superPackagePrivateMethodInjected = true
    }

    @Inject
    void injectPackagePrivateMethodForOverride() {
        superPackagePrivateMethodForOverrideInjected = true
    }

    @Inject
    void injectQualifiers(@org.atinject.jakartatck.auto.Drivers Seat seatA, Seat seatB,
                          @Named('spare') org.atinject.jakartatck.auto.Tire tireA, org.atinject.jakartatck.auto.Tire tireB) {
        if (!(seatA instanceof org.atinject.jakartatck.auto.DriversSeat)
                || (seatB instanceof org.atinject.jakartatck.auto.DriversSeat)
                || !(tireA instanceof org.atinject.jakartatck.auto.accessories.SpareTire)
                || (tireB instanceof org.atinject.jakartatck.auto.accessories.SpareTire)) {
            qualifiersInheritedFromOverriddenMethod = true
        }
    }

    @Inject
    void injectTwiceOverriddenWithOmissionInMiddle() {
        overriddenTwiceWithOmissionInMiddleInjected = true
    }

    @Inject
    void injectTwiceOverriddenWithOmissionInSubclass() {
        overriddenTwiceWithOmissionInSubclassInjected = true
    }
}
