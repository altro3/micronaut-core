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
package io.micronaut.http.bind.binders;

import io.micronaut.core.annotation.AnnotationMetadata;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.bind.annotation.AbstractArgumentBinder;
import io.micronaut.core.convert.ArgumentConversionContext;
import io.micronaut.core.convert.ConversionService;
import io.micronaut.core.convert.format.Format;
import io.micronaut.core.convert.value.ConvertibleMultiValues;
import io.micronaut.core.naming.NameUtils;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Header;

import java.util.Optional;

/**
 * An {@link io.micronaut.core.bind.annotation.AnnotatedArgumentBinder} implementation that uses the {@link Header}
 * annotation to trigger binding from an HTTP header.
 *
 * @param <T> A type
 * @author Graeme Rocher
 * @see io.micronaut.http.HttpHeaders
 * @since 1.0
 */
public class HeaderAnnotationBinder<T> extends AbstractArgumentBinder<T> implements AnnotatedRequestArgumentBinder<Header, T> {

    /**
     * @param conversionService The conversion service
     */
    public HeaderAnnotationBinder(ConversionService conversionService) {
        super(conversionService);
    }

    /**
     * @param conversionService The conversion service
     * @param argument The argument
     */
    public HeaderAnnotationBinder(ConversionService conversionService, Argument<T> argument) {
        super(conversionService, argument);
    }

    @NonNull
    @Override
    public RequestArgumentBinder<T> createSpecific(@NonNull Argument<T> argument) {
        return new HeaderAnnotationBinder<>(conversionService, argument);
    }

    @Override
    public BindingResult<T> bind(ArgumentConversionContext<T> context, HttpRequest<?> source) {

        ConvertibleMultiValues<String> headers = source.getHeaders();
        Argument<T> argument = context.getArgument();
        AnnotationMetadata annotationMetadata = argument.getAnnotationMetadata();

        Optional<T> multiValueConversion;
        if (annotationMetadata.hasAnnotation(Format.class)) {
            multiValueConversion = conversionService.convert(headers, context);
        } else {
            multiValueConversion = Optional.empty();
        }
        if (multiValueConversion.isPresent()) {
            return () -> multiValueConversion;
        }

        return doBind(context, headers);
    }

    @Override
    public Class<Header> getAnnotationType() {
        return Header.class;
    }

    @NonNull
    @Override
    protected String getParameterName(Argument<T> argument) {
        AnnotationMetadata annotationMetadata = argument.getAnnotationMetadata();
        return annotationMetadata.stringValue(Header.class)
            .orElseGet(() -> annotationMetadata.stringValue(Header.class, "name").orElse(argument.getName()));
    }

    @Override
    protected String getFallbackFormat(Argument<?> argument) {
        return NameUtils.hyphenate(NameUtils.capitalize(argument.getName()), false);
    }
}
