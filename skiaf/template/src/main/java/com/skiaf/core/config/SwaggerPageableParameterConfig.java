/**
 * Copyright (c) 2018 SK INNOVATION Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK INNOVATION.
 * You shall not disclose such confidential information and shall use it only in accordance
 * with the terms of the license agreement you entered into with SK INNOVATION.
 */
package com.skiaf.core.config;

import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.spi.schema.contexts.ModelContext.inputParam;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Function;

import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.schema.ResolvedTypes;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.ParameterContext;

/**
 * <pre>
 * Pageable 파라메터가 Swagger 에서 올바르게 표시 및 입력 되도록 하는 설정
 * </pre>
 */
@ConditionalOnExpression("${bcm.swagger.enabled}")
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class SwaggerPageableParameterConfig implements OperationBuilderPlugin {

    private static final String QUERY = "query";
    private static final String PAGE = "page";
    private static final String SIZE = "size";
    private static final String SORT = "sort";

    private final TypeNameExtractor nameExtractor;
    private final TypeResolver resolver;
    private final ResolvedType pageableType;

    @Autowired
    public SwaggerPageableParameterConfig(TypeNameExtractor nameExtractor, TypeResolver resolver) {
        this.nameExtractor = nameExtractor;
        this.resolver = resolver;
        this.pageableType = resolver.resolve(Pageable.class);
    }

    @Override
    public void apply(OperationContext context) {
        List<ResolvedMethodParameter> methodParameters = context.getParameters();
        List<Parameter> parameters = newArrayList();

        ResolvedType resolvedType = null;
        ParameterContext parameterContext = null;
        ModelReference intModel = null;
        ModelReference stringModel = null;

        for (ResolvedMethodParameter methodParameter : methodParameters) {
            resolvedType = methodParameter.getParameterType();

            if (pageableType.equals(resolvedType)) {
                parameterContext = new ParameterContext(
                                        methodParameter, 
                                        new ParameterBuilder(),
                                        context.getDocumentationContext(), 
                                        context.getGenericsNamingStrategy(), 
                                        context);
                Function<ResolvedType, ? extends ModelReference> factory = createModelRefFactory(parameterContext);

                intModel = factory.apply(resolver.resolve(Integer.TYPE));
                stringModel = factory.apply(resolver.resolve(List.class, String.class));

                parameters.add(new ParameterBuilder()
                                    .parameterType(QUERY)
                                    .name(PAGE)
                                    .modelRef(intModel)
                                    .description("Results page you want to retrieve (0..N)")
                                    .build());

                parameters.add(new ParameterBuilder()
                                    .parameterType(QUERY)
                                    .name(SIZE)
                                    .modelRef(intModel)
                                    .description("Number of records per page")
                                    .build());

                parameters.add(new ParameterBuilder()
                                    .parameterType(QUERY)
                                    .name(SORT)
                                    .modelRef(stringModel)
                                    .allowMultiple(true)
                                    .description("Sorting criteria in the format: property(,asc|desc). "
                                                    + "Default sort order is ascending. " 
                                                    + "Multiple sort criteria are supported.")
                                    .build());

                context.operationBuilder().parameters(parameters);
            }
        }
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    private Function<ResolvedType, ? extends ModelReference> createModelRefFactory(ParameterContext context) {
        ModelContext modelContext = inputParam(context.getGroupName(),
                                                context.resolvedMethodParameter().getParameterType(), 
                                                context.getDocumentationType(),
                                                context.getAlternateTypeProvider(), 
                                                context.getGenericNamingStrategy(),
                                                context.getIgnorableParameterTypes());

        return ResolvedTypes.modelRefFactory(modelContext, nameExtractor);
    }
}