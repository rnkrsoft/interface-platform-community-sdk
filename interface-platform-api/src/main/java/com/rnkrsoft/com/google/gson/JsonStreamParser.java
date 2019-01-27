/*
 * Copyright (C) 2009 Google Inc.
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

import com.rnkrsoft.com.google.gson.internal.Streams;
import com.rnkrsoft.com.google.gson.stream.JsonReader;
import com.rnkrsoft.com.google.gson.stream.JsonToken;
import com.rnkrsoft.com.google.gson.stream.MalformedJsonException;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class JsonStreamParser implements Iterator<JsonElement> {
    private final JsonReader parser;
    private final Object lock;


    public JsonStreamParser(String json) {
        this(new StringReader(json));
    }


    public JsonStreamParser(Reader reader) {
        parser = new JsonReader(reader);
        parser.setLenient(true);
        lock = new Object();
    }


    public JsonElement next() throws JsonParseException {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        try {
            return Streams.parse(parser);
        } catch (StackOverflowError e) {
            throw new JsonParseException("Failed parsing JSON source to Json", e);
        } catch (OutOfMemoryError e) {
            throw new JsonParseException("Failed parsing JSON source to Json", e);
        } catch (JsonParseException e) {
            throw e.getCause() instanceof EOFException ? new NoSuchElementException() : e;
        }
    }


    public boolean hasNext() {
        synchronized (lock) {
            try {
                return parser.peek() != JsonToken.END_DOCUMENT;
            } catch (MalformedJsonException e) {
                throw new JsonSyntaxException(e);
            } catch (IOException e) {
                throw new JsonIOException(e);
            }
        }
    }


    public void remove() {
        throw new UnsupportedOperationException();
    }
}
