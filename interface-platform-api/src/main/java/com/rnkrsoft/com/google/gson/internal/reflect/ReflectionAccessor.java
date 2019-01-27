/*
 * Copyright (C) 2017 The Gson authors
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
package com.rnkrsoft.com.google.gson.internal.reflect;

import com.rnkrsoft.com.google.gson.internal.JavaVersion;

import java.lang.reflect.AccessibleObject;

public abstract class ReflectionAccessor {

    // the singleton instance, use getInstance() to obtain
    private static final ReflectionAccessor instance = JavaVersion.getMajorJavaVersion() < 9 ? new PreJava9ReflectionAccessor() : new UnsafeReflectionAccessor();


    public abstract void makeAccessible(AccessibleObject ao);


    public static ReflectionAccessor getInstance() {
        return instance;
    }
}
