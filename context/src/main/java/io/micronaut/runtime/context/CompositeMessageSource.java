/*
 * Copyright 2017-2020 original authors
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
package io.micronaut.runtime.context;

import io.micronaut.context.AbstractMessageSource;
import io.micronaut.context.MessageSource;
import io.micronaut.context.annotation.Primary;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.ArgumentUtils;
import jakarta.inject.Inject;

import java.util.*;

/**
 * Composite message source that is the primary message source.
 *
 * @author graemerocher
 * @since 1.2
 */
@Primary
public final class CompositeMessageSource extends AbstractMessageSource {

    private final List<MessageSource> messageSources;

    /**
     * The other messages sources.
     *
     * @param messageSources The message sources.
     * @deprecated Use {@link CompositeMessageSource(List)} instead.
     */
    @Deprecated
    public CompositeMessageSource(@Nullable Collection<MessageSource> messageSources) {
        this(messageSources == null ? Collections.emptyList() : new ArrayList<>(messageSources));
    }

    /**
     * The other messages sources.
     *
     * @param messageSources The message sources.
     */
    @Inject
    public CompositeMessageSource(@Nullable List<MessageSource> messageSources) {
        this.messageSources = messageSources != null ? messageSources : Collections.emptyList();
    }

    @NonNull
    @Override
    public Optional<String> getRawMessage(@NonNull String code, @NonNull MessageContext context) {
        ArgumentUtils.requireNonNull("code", code);
        ArgumentUtils.requireNonNull("context", context);
        for (MessageSource messageSource : messageSources) {
            final Optional<String> message = messageSource.getRawMessage(code, context);
            if (message.isPresent()) {
                return message;
            }
        }
        return Optional.empty();
    }
}
