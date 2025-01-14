/*
 * Copyright 2017-2022 original authors
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
package io.micronaut.expressions.parser.ast.types;

import io.micronaut.core.annotation.Internal;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.expressions.parser.ast.ExpressionNode;
import io.micronaut.expressions.parser.ast.util.TypeDescriptors;
import io.micronaut.expressions.parser.compilation.ExpressionCompilationContext;
import io.micronaut.expressions.parser.compilation.ExpressionVisitorContext;
import io.micronaut.expressions.parser.exception.ExpressionCompilationException;
import io.micronaut.inject.ast.ClassElement;
import io.micronaut.inject.ast.PrimitiveElement;
import io.micronaut.sourcegen.model.ExpressionDef;
import io.micronaut.sourcegen.model.TypeDef;

import java.util.Map;
import java.util.Optional;

/**
 * Expression node for type identifier. Bytecode for identifier is not generated
 * directly - it is generated by nodes using the identifier.
 *
 * @author Sergey Gavrilov
 * @since 4.0.0
 */
@Internal
public final class TypeIdentifier extends ExpressionNode {
    private static final Map<String, TypeDef> PRIMITIVES = Map.of(
        "int", TypeDescriptors.INT,
        "long", TypeDescriptors.LONG,
        "byte", TypeDescriptors.BYTE,
        "short", TypeDescriptors.SHORT,
        "char", TypeDescriptors.CHAR,
        "boolean", TypeDescriptors.BOOLEAN,
        "double", TypeDescriptors.DOUBLE,
        "float", TypeDescriptors.FLOAT);

    private final String name;

    public TypeIdentifier(String name) {
        this.name = name;
    }

    public boolean isPrimitive() {
        return PRIMITIVES.containsKey(this.toString());
    }

    @Override
    public ExpressionDef generateExpression(ExpressionCompilationContext ctx) {
        return ExpressionDef.constant(resolveType(ctx));
    }

    @Override
    protected ClassElement doResolveClassElement(ExpressionVisitorContext ctx) {
        String name = this.toString();
        if (PRIMITIVES.containsKey(name)) {
            return PrimitiveElement.valueOf(name);
        }
        Optional<ClassElement> resolvedType = ctx.visitorContext().getClassElement(name);
        if (resolvedType.isEmpty() && !name.contains(".")) {
            resolvedType = ctx.visitorContext().getClassElement("java.lang." + name);
        }
        return resolvedType
            .orElseThrow(() -> new ExpressionCompilationException("Unknown type identifier: " + name));
    }

    @Override
    public TypeDef doResolveType(@NonNull ExpressionVisitorContext ctx) {
        String name = this.toString();
        if (PRIMITIVES.containsKey(name)) {
            return PRIMITIVES.get(name);
        }

        TypeDef resolvedType = resolveObjectType(ctx, name);

        // may be java.lang type
        if (resolvedType == null && !name.contains(".")) {
            resolvedType = resolveObjectType(ctx, "java.lang." + name);
        }

        if (resolvedType == null) {
            throw new ExpressionCompilationException("Unknown type identifier: " + name);
        }

        return resolvedType;
    }

    private TypeDef resolveObjectType(ExpressionVisitorContext ctx, String name) {
        return ctx.visitorContext().getClassElement(name)
                   .map(TypeDef::erasure)
                   .orElse(null);
    }

    @Override
    public String toString() {
        return name;
    }
}
