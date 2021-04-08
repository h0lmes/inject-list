package com.example.InjectList.config;

import org.reflections.ReflectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class InjectComponentListBPP implements BeanPostProcessor {

    private final ExpressionParser parser = new SpelExpressionParser();

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Set<Field> fields = ReflectionUtils.getAllFields(bean.getClass(), field -> field.isAnnotationPresent(InjectComponentList.class));

        fields.forEach(field -> {
            InjectComponentList annotation = field.getAnnotation(InjectComponentList.class);
            if (!annotation.components().isEmpty()) {
                List<Object> objectListToInject = parseSpel(parseProperties(annotation.components()))
                        .stream()
                        .map(name -> applicationContext.getBean(name.trim()))
                        .collect(Collectors.toList());
                field.setAccessible(true);
                try {
                    field.set(bean, objectListToInject);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return bean;
    }

    private List<String> parseSpel(String componentNames) {
        Expression expression;
        if (componentNames.startsWith("#")) {
            expression = parser.parseExpression(componentNames, new TemplateParserContext());
        } else {
            expression = parser.parseExpression(componentNames);
        }
        Object obj = expression.getValue();

        if (obj instanceof String[]) {
            return Arrays.asList((String[]) obj);
        }
        if (obj instanceof List) {
            return (List<String>) obj;
        }
        if (obj instanceof String) {
            List<String> list = new ArrayList<>();
            list.add((String) obj);
            return list;
        }
        return new ArrayList<>();
    }

    private String parseProperties(String value) {
        Environment env = applicationContext.getEnvironment();
        int start = value.indexOf("${");
        int end = value.indexOf("}", start);
        while (start > 0 && end > 0) {
            String propName = value.substring(start + 2, end);
            if (env.containsProperty(propName)) {
                value = value.substring(0, start) + env.getProperty(propName) + value.substring(end + 1);
                start = value.indexOf("${");
                end = value.indexOf("}", start);
            } else {
                start = value.indexOf("${", end);
                end = value.indexOf("}", start);
            }
        }
        return value;
    }
}
