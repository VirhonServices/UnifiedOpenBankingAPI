/*
 * Copyright 2026 Viktor Honcharov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.virhon.uobg;

import co.virhon.uobg.domain.rules.ApiRulesConfig;
import co.virhon.uobg.domain.rules.Endpoint;
import co.virhon.uobg.domain.rules.NodeRule;
import co.virhon.uobg.domain.rules.ParameterRule;
import co.virhon.uobg.domain.rules.RequestConfig;
import co.virhon.uobg.domain.rules.RulesLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Component
public class GatewayMapper<UnifiedDTO, BankSpecificDTO> {
    private static final String RULES_PATH_TEMPLATE = "rules/%s/%s/%s.json";

    private final Class<UnifiedDTO> unifiedClass;
    private final Class<BankSpecificDTO> bankSpecificClass;
    private final String countryCode;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GatewayMapper(Class<UnifiedDTO> unifiedClass,
                         Class<BankSpecificDTO> bankSpecificClass,
                         String countryCode) {
        this.unifiedClass = Objects.requireNonNull(unifiedClass, "unifiedClass must not be null");
        this.bankSpecificClass = Objects.requireNonNull(bankSpecificClass, "bankSpecificClass must not be null");
        this.countryCode = requireCode(countryCode, "countryCode");
    }

    public BankSpecificDTO map(UnifiedDTO source, String bankCode) {
        Objects.requireNonNull(source, "source must not be null");

        String rulesPath = RULES_PATH_TEMPLATE.formatted(
                countryCode,
                requireCode(bankCode, "bankCode"),
                resolveRulesName()
        );
        ApiRulesConfig rules = RulesLoader.loadRulesFromClasspath(rulesPath);

        return map(source, rules);
    }

    private BankSpecificDTO map(UnifiedDTO source, ApiRulesConfig rules) {
        Endpoint endpoint = findEndpoint(rules);
        RequestConfig request = endpoint.getRequest();

        try {
            BankSpecificDTO result = bankSpecificClass.getDeclaredConstructor().newInstance();
            setProperty(result, "url", endpoint.getUrl());
            setProperty(result, "method", endpoint.getMethod());

            if (request != null) {
                setProperty(result, "headers", mapParameters(source, request.getHeaders()));
                setProperty(result, "pathParameters", mapParameters(source, request.getPath()));
                if (request.getPayload() != null) {
                    setProperty(result, "payloadJson", objectMapper.writeValueAsString(
                            mapPayload(source, request.getPayload().getNodes())
                    ));
                }
            }
            return result;
        } catch (ReflectiveOperationException | IntrospectionException | JsonProcessingException e) {
            throw new IllegalStateException("Failed to map " + unifiedClass.getName()
                    + " to " + bankSpecificClass.getName(), e);
        }
    }

    private Endpoint findEndpoint(ApiRulesConfig rules) {
        String name = unifiedClass.getSimpleName()
                .replaceFirst("^Unified", "")
                .replaceFirst("(Request|Response)?DTO$", "");
        name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
        Endpoint endpoint = rules.getEndpoints().get(name);
        if (endpoint == null) {
            throw new IllegalArgumentException("Endpoint rules not found: " + name);
        }
        return endpoint;
    }

    private Map<String, String> mapParameters(UnifiedDTO source, List<ParameterRule> rules)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Map<String, String> result = new LinkedHashMap<>();
        if (rules == null) {
            return result;
        }
        for (ParameterRule rule : rules) {
            Object value = "constant".equals(rule.getMethod())
                    ? rule.getValue()
                    : readParameter(source, rule);
            if (value != null) {
                result.put(rule.getCode(), String.valueOf(value));
            }
        }
        return result;
    }

    private Object readParameter(Object source, ParameterRule rule)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        if (rule.getSourceValue() != null) {
            return readPath(source, rule.getSourceValue());
        }
        String property = rule.getCode().replaceAll("[^A-Za-z0-9]", "").toLowerCase(Locale.ROOT);
        for (PropertyDescriptor descriptor : Introspector.getBeanInfo(source.getClass()).getPropertyDescriptors()) {
            if (descriptor.getName().toLowerCase(Locale.ROOT).equals(property)
                    && descriptor.getReadMethod() != null) {
                return descriptor.getReadMethod().invoke(source);
            }
        }
        return null;
    }

    private Map<String, Object> mapPayload(UnifiedDTO source, List<NodeRule> rules)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Map<String, Object> root = new LinkedHashMap<>();
        Map<String, Object> containers = new LinkedHashMap<>();
        Map<String, String> paths = new LinkedHashMap<>();
        containers.put("root", root);
        paths.put("root", "");

        for (NodeRule rule : rules) {
            Object parent = containers.get(rule.getParent());
            String parentPath = paths.get(rule.getParent());
            if (parent == null || parentPath == null) {
                throw new IllegalArgumentException("Unknown payload parent: " + rule.getParent());
            }
            String targetPath = parentPath.isEmpty() ? rule.getCode() : parentPath + "." + rule.getCode();
            if ("node".equals(rule.getType())) {
                Object resolved = readPath(source, targetPath);
                Object child = resolved instanceof Collection<?> collection
                        ? createMaps(collection.size()) : new LinkedHashMap<String, Object>();
                putValue(parent, rule.getCode(), child);
                containers.put(rule.getCode(), child);
                paths.put(rule.getCode(), targetPath);
            } else if ("value".equals(rule.getType())) {
                Object value = readPath(source, rule.getSourceValue());
                putValue(parent, rule.getCode(), normalizeValue(value));
            }
        }
        return root;
    }

    private static List<Map<String, Object>> createMaps(int size) {
        List<Map<String, Object>> maps = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            maps.add(new LinkedHashMap<>());
        }
        return maps;
    }

    @SuppressWarnings("unchecked")
    private static void putValue(Object parent, String key, Object value) {
        if (parent instanceof Map<?, ?> map) {
            ((Map<String, Object>) map).put(key, value);
            return;
        }
        List<Map<String, Object>> parents = (List<Map<String, Object>>) parent;
        if (value instanceof List<?> values && values.size() == parents.size()) {
            for (int i = 0; i < parents.size(); i++) {
                parents.get(i).put(key, values.get(i));
            }
        } else {
            parents.forEach(item -> item.put(key, value));
        }
    }

    private Object readPath(Object source, String path)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        if (source == null || path == null || path.isBlank()) {
            return source;
        }
        Object current = source;
        for (String part : path.split("\\.")) {
            if (current instanceof Collection<?> collection) {
                List<Object> values = new ArrayList<>(collection.size());
                for (Object item : collection) {
                    values.add(readPath(item, part));
                }
                current = values;
            } else {
                current = readProperty(current, part);
            }
            if (current == null) {
                return null;
            }
        }
        return current;
    }

    private static Object readProperty(Object bean, String property)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        String normalizedProperty = property.replace("reccurring", "recurring");
        for (PropertyDescriptor descriptor : Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors()) {
            if (descriptor.getName().equals(normalizedProperty) && descriptor.getReadMethod() != null) {
                return descriptor.getReadMethod().invoke(bean);
            }
        }
        return null;
    }

    private static Object normalizeValue(Object value) {
        if (value instanceof TemporalAccessor || value instanceof Enum<?>) {
            return value.toString();
        }
        if (value instanceof Collection<?> collection) {
            return collection.stream().map(GatewayMapper::normalizeValue).toList();
        }
        return value;
    }

    private static void setProperty(Object bean, String property, Object value)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        for (PropertyDescriptor descriptor : Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors()) {
            if (descriptor.getName().equals(property) && descriptor.getWriteMethod() != null) {
                descriptor.getWriteMethod().invoke(bean, value);
                return;
            }
        }
        throw new IllegalArgumentException("Property '" + property + "' not found in " + bean.getClass().getName());
    }

    private String resolveRulesName() {
        String dtoName = unifiedClass.getSimpleName()
                .replaceFirst("^Unified", "")
                .replaceFirst("^(Create|Update|Delete|Get)", "")
                .replaceFirst("(Request|Response)?DTO$", "");

        if (dtoName.isBlank()) {
            throw new IllegalArgumentException(
                    "Cannot derive rules file name from unified DTO: " + unifiedClass.getName()
            );
        }
        return dtoName.toLowerCase(Locale.ROOT);
    }

    private static String requireCode(String code, String parameterName) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException(parameterName + " must not be blank");
        }
        return code.trim().toLowerCase(Locale.ROOT);
    }
}
