/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rnkrsoft.com.google.gson;

import com.rnkrsoft.com.google.gson.internal.$Gson$Preconditions;
import com.rnkrsoft.com.google.gson.internal.Excluder;
import com.rnkrsoft.com.google.gson.internal.bind.TreeTypeAdapter;
import com.rnkrsoft.com.google.gson.internal.bind.TypeAdapters;
import com.rnkrsoft.com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.*;

import static com.rnkrsoft.com.google.gson.Gson.*;

public final class GsonBuilder {
    private Excluder excluder = Excluder.DEFAULT;
    private LongSerializationPolicy longSerializationPolicy = LongSerializationPolicy.DEFAULT;
    private FieldNamingStrategy fieldNamingPolicy = FieldNamingPolicy.IDENTITY;
    private final Map<Type, InstanceCreator<?>> instanceCreators
            = new HashMap<Type, InstanceCreator<?>>();
    private final List<TypeAdapterFactory> factories = new ArrayList<TypeAdapterFactory>();
    /**
     * tree-style hierarchy factories. These come after factories for backwards compatibility.
     */
    private final List<TypeAdapterFactory> hierarchyFactories = new ArrayList<TypeAdapterFactory>();
    private boolean serializeNulls = DEFAULT_SERIALIZE_NULLS;
    private String datePattern;
    private int dateStyle = DateFormat.DEFAULT;
    private int timeStyle = DateFormat.DEFAULT;
    private boolean complexMapKeySerialization = DEFAULT_COMPLEX_MAP_KEYS;
    private boolean serializeSpecialFloatingPointValues = DEFAULT_SPECIALIZE_FLOAT_VALUES;
    private boolean escapeHtmlChars = DEFAULT_ESCAPE_HTML;
    private boolean prettyPrinting = DEFAULT_PRETTY_PRINT;
    private boolean generateNonExecutableJson = DEFAULT_JSON_NON_EXECUTABLE;
    private boolean lenient = DEFAULT_LENIENT;

    public GsonBuilder() {
    }

    GsonBuilder(Gson gson) {
        this.excluder = gson.excluder;
        this.fieldNamingPolicy = gson.fieldNamingStrategy;
        this.instanceCreators.putAll(gson.instanceCreators);
        this.serializeNulls = gson.serializeNulls;
        this.complexMapKeySerialization = gson.complexMapKeySerialization;
        this.generateNonExecutableJson = gson.generateNonExecutableJson;
        this.escapeHtmlChars = gson.htmlSafe;
        this.prettyPrinting = gson.prettyPrinting;
        this.lenient = gson.lenient;
        this.serializeSpecialFloatingPointValues = gson.serializeSpecialFloatingPointValues;
        this.longSerializationPolicy = gson.longSerializationPolicy;
        this.datePattern = gson.datePattern;
        this.dateStyle = gson.dateStyle;
        this.timeStyle = gson.timeStyle;
        this.factories.addAll(gson.builderFactories);
        this.hierarchyFactories.addAll(gson.builderHierarchyFactories);
    }

    public GsonBuilder setVersion(double ignoreVersionsAfter) {
        excluder = excluder.withVersion(ignoreVersionsAfter);
        return this;
    }

    public GsonBuilder excludeFieldsWithModifiers(int... modifiers) {
        excluder = excluder.withModifiers(modifiers);
        return this;
    }

    public GsonBuilder generateNonExecutableJson() {
        this.generateNonExecutableJson = true;
        return this;
    }

    public GsonBuilder excludeFieldsWithoutExposeAnnotation() {
        excluder = excluder.excludeFieldsWithoutExposeAnnotation();
        return this;
    }

    public GsonBuilder serializeNulls() {
        this.serializeNulls = true;
        return this;
    }

    public GsonBuilder enableComplexMapKeySerialization() {
        complexMapKeySerialization = true;
        return this;
    }

    public GsonBuilder disableInnerClassSerialization() {
        excluder = excluder.disableInnerClassSerialization();
        return this;
    }

    public GsonBuilder setLongSerializationPolicy(LongSerializationPolicy serializationPolicy) {
        this.longSerializationPolicy = serializationPolicy;
        return this;
    }

    public GsonBuilder setFieldNamingPolicy(FieldNamingPolicy namingConvention) {
        this.fieldNamingPolicy = namingConvention;
        return this;
    }

    public GsonBuilder setFieldNamingStrategy(FieldNamingStrategy fieldNamingStrategy) {
        this.fieldNamingPolicy = fieldNamingStrategy;
        return this;
    }

    public GsonBuilder setExclusionStrategies(ExclusionStrategy... strategies) {
        for (ExclusionStrategy strategy : strategies) {
            excluder = excluder.withExclusionStrategy(strategy, true, true);
        }
        return this;
    }

    public GsonBuilder addSerializationExclusionStrategy(ExclusionStrategy strategy) {
        excluder = excluder.withExclusionStrategy(strategy, true, false);
        return this;
    }

    public GsonBuilder addDeserializationExclusionStrategy(ExclusionStrategy strategy) {
        excluder = excluder.withExclusionStrategy(strategy, false, true);
        return this;
    }

    public GsonBuilder setPrettyPrinting() {
        prettyPrinting = true;
        return this;
    }

    public GsonBuilder setLenient() {
        lenient = true;
        return this;
    }

    public GsonBuilder disableHtmlEscaping() {
        this.escapeHtmlChars = false;
        return this;
    }

    public GsonBuilder setDateFormat(String pattern) {
        // TODO(Joel): Make this fail fast if it is an invalid date format
        this.datePattern = pattern;
        return this;
    }

    /**
     * Configures Gson to to serialize {@code Date} objects according to the style value provided.
     * You can call this method or {@link #setDateFormat(String)} multiple times, but only the last
     * invocation will be used to decide the serialization format.
     * <p>
     * <p>Note that this style value should be one of the predefined constants in the
     * {@code DateFormat} class. See the documentation in {@link java.text.DateFormat} for more
     * information on the valid style constants.</p>
     *
     * @param style the predefined date style that date objects will be serialized/deserialized
     *              to/from
     * @return a reference to this {@code GsonBuilder} object to fulfill the "Builder" pattern
     * @since 1.2
     */
    public GsonBuilder setDateFormat(int style) {
        this.dateStyle = style;
        this.datePattern = null;
        return this;
    }

    /**
     * Configures Gson to to serialize {@code Date} objects according to the style value provided.
     * You can call this method or {@link #setDateFormat(String)} multiple times, but only the last
     * invocation will be used to decide the serialization format.
     * <p>
     * <p>Note that this style value should be one of the predefined constants in the
     * {@code DateFormat} class. See the documentation in {@link java.text.DateFormat} for more
     * information on the valid style constants.</p>
     *
     * @param dateStyle the predefined date style that date objects will be serialized/deserialized
     *                  to/from
     * @param timeStyle the predefined style for the time portion of the date objects
     * @return a reference to this {@code GsonBuilder} object to fulfill the "Builder" pattern
     * @since 1.2
     */
    public GsonBuilder setDateFormat(int dateStyle, int timeStyle) {
        this.dateStyle = dateStyle;
        this.timeStyle = timeStyle;
        this.datePattern = null;
        return this;
    }

    /**
     * Configures Gson for custom serialization or deserialization. This method combines the
     * registration of an {@link TypeAdapter}, {@link InstanceCreator}, {@link JsonSerializer}, and a
     * {@link JsonDeserializer}. It is best used when a single object {@code typeAdapter} implements
     * all the required interfaces for custom serialization with Gson. If a type adapter was
     * previously registered for the specified {@code type}, it is overwritten.
     * <p/>
     * <p>This registers the type specified and no other types: you must manually register related
     * types! For example, applications registering {@code boolean.class} should also register {@code
     * Boolean.class}.
     *
     * @param type        the type definition for the type adapter being registered
     * @param typeAdapter This object must implement at least one of the {@link TypeAdapter},
     *                    {@link InstanceCreator}, {@link JsonSerializer}, and a {@link JsonDeserializer} interfaces.
     * @return a reference to this {@code GsonBuilder} object to fulfill the "Builder" pattern
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public GsonBuilder registerTypeAdapter(Type type, Object typeAdapter) {
        $Gson$Preconditions.checkArgument(typeAdapter instanceof JsonSerializer<?>
                || typeAdapter instanceof JsonDeserializer<?>
                || typeAdapter instanceof InstanceCreator<?>
                || typeAdapter instanceof TypeAdapter<?>);
        if (typeAdapter instanceof InstanceCreator<?>) {
            instanceCreators.put(type, (InstanceCreator) typeAdapter);
        }
        if (typeAdapter instanceof JsonSerializer<?> || typeAdapter instanceof JsonDeserializer<?>) {
            TypeToken<?> typeToken = TypeToken.get(type);
            factories.add(TreeTypeAdapter.newFactoryWithMatchRawType(typeToken, typeAdapter));
        }
        if (typeAdapter instanceof TypeAdapter<?>) {
            factories.add(TypeAdapters.newFactory(TypeToken.get(type), (TypeAdapter) typeAdapter));
        }
        return this;
    }

    /**
     * Register a factory for type adapters. Registering a factory is useful when the type
     * adapter needs to be configured based on the type of the field being processed. Gson
     * is designed to handle a large number of factories, so you should consider registering
     * them to be at par with registering an individual type adapter.
     *
     * @since 2.1
     */
    public GsonBuilder registerTypeAdapterFactory(TypeAdapterFactory factory) {
        factories.add(factory);
        return this;
    }

    /**
     * Configures Gson for custom serialization or deserialization for an inheritance type hierarchy.
     * This method combines the registration of a {@link TypeAdapter}, {@link JsonSerializer} and
     * a {@link JsonDeserializer}. If a type adapter was previously registered for the specified
     * type hierarchy, it is overridden. If a type adapter is registered for a specific type in
     * the type hierarchy, it will be invoked instead of the one registered for the type hierarchy.
     *
     * @param baseType    the class definition for the type adapter being registered for the base class
     *                    or interface
     * @param typeAdapter This object must implement at least one of {@link TypeAdapter},
     *                    {@link JsonSerializer} or {@link JsonDeserializer} interfaces.
     * @return a reference to this {@code GsonBuilder} object to fulfill the "Builder" pattern
     * @since 1.7
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public GsonBuilder registerTypeHierarchyAdapter(Class<?> baseType, Object typeAdapter) {
        $Gson$Preconditions.checkArgument(typeAdapter instanceof JsonSerializer<?>
                || typeAdapter instanceof JsonDeserializer<?>
                || typeAdapter instanceof TypeAdapter<?>);
        if (typeAdapter instanceof JsonDeserializer || typeAdapter instanceof JsonSerializer) {
            hierarchyFactories.add(TreeTypeAdapter.newTypeHierarchyFactory(baseType, typeAdapter));
        }
        if (typeAdapter instanceof TypeAdapter<?>) {
            factories.add(TypeAdapters.newTypeHierarchyFactory(baseType, (TypeAdapter) typeAdapter));
        }
        return this;
    }

    /**
     * Section 2.4 of <a href="http://www.ietf.org/rfc/rfc4627.txt">JSON specification</a> disallows
     * special double values (NaN, Infinity, -Infinity). However,
     * <a href="http://www.ecma-international.org/publications/files/ECMA-ST/Ecma-262.pdf">Javascript
     * specification</a> (see section 4.3.20, 4.3.22, 4.3.23) allows these values as valid Javascript
     * values. Moreover, most JavaScript engines will accept these special values in JSON without
     * problem. So, at a practical level, it makes sense to accept these values as valid JSON even
     * though JSON specification disallows them.
     * <p/>
     * <p>Gson always accepts these special values during deserialization. However, it outputs
     * strictly compliant JSON. Hence, if it encounters a float value {@link Float#NaN},
     * {@link Float#POSITIVE_INFINITY}, {@link Float#NEGATIVE_INFINITY}, or a double value
     * {@link Double#NaN}, {@link Double#POSITIVE_INFINITY}, {@link Double#NEGATIVE_INFINITY}, it
     * will throw an {@link IllegalArgumentException}. This method provides a way to override the
     * default behavior when you know that the JSON receiver will be able to handle these special
     * values.
     *
     * @return a reference to this {@code GsonBuilder} object to fulfill the "Builder" pattern
     * @since 1.3
     */
    public GsonBuilder serializeSpecialFloatingPointValues() {
        this.serializeSpecialFloatingPointValues = true;
        return this;
    }

    /**
     * Creates a {@link Gson} instance based on the current configuration. This method is free of
     * side-effects to this {@code GsonBuilder} instance and hence can be called multiple times.
     *
     * @return an instance of Gson configured with the options currently set in this builder
     */
    public Gson create() {
        List<TypeAdapterFactory> factories = new ArrayList<TypeAdapterFactory>(this.factories.size() + this.hierarchyFactories.size() + 3);
        factories.addAll(this.factories);
        Collections.reverse(factories);

        List<TypeAdapterFactory> hierarchyFactories = new ArrayList<TypeAdapterFactory>(this.hierarchyFactories);
        Collections.reverse(hierarchyFactories);
        factories.addAll(hierarchyFactories);

        addTypeAdaptersForDate(datePattern, dateStyle, timeStyle, factories);

        return new Gson(excluder, fieldNamingPolicy, instanceCreators,
                serializeNulls, complexMapKeySerialization,
                generateNonExecutableJson, escapeHtmlChars, prettyPrinting, lenient,
                serializeSpecialFloatingPointValues, longSerializationPolicy,
                datePattern, dateStyle, timeStyle,
                this.factories, this.hierarchyFactories, factories);
    }

    @SuppressWarnings("unchecked")
    private void addTypeAdaptersForDate(String datePattern, int dateStyle, int timeStyle,
                                        List<TypeAdapterFactory> factories) {
        DefaultDateTypeAdapter dateTypeAdapter;
        TypeAdapter<Timestamp> timestampTypeAdapter;
        TypeAdapter<java.sql.Date> javaSqlDateTypeAdapter;
        if (datePattern != null && !"".equals(datePattern.trim())) {
            dateTypeAdapter = new DefaultDateTypeAdapter(Date.class, datePattern);
            timestampTypeAdapter = (TypeAdapter) new DefaultDateTypeAdapter(Timestamp.class, datePattern);
            javaSqlDateTypeAdapter = (TypeAdapter) new DefaultDateTypeAdapter(java.sql.Date.class, datePattern);
        } else if (dateStyle != DateFormat.DEFAULT && timeStyle != DateFormat.DEFAULT) {
            dateTypeAdapter = new DefaultDateTypeAdapter(Date.class, dateStyle, timeStyle);
            timestampTypeAdapter = (TypeAdapter) new DefaultDateTypeAdapter(Timestamp.class, dateStyle, timeStyle);
            javaSqlDateTypeAdapter = (TypeAdapter) new DefaultDateTypeAdapter(java.sql.Date.class, dateStyle, timeStyle);
        } else {
            return;
        }

        factories.add(TypeAdapters.newFactory(Date.class, dateTypeAdapter));
        factories.add(TypeAdapters.newFactory(Timestamp.class, timestampTypeAdapter));
        factories.add(TypeAdapters.newFactory(java.sql.Date.class, javaSqlDateTypeAdapter));
    }
}
