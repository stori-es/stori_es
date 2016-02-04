/**
 * Copyright (C) 2009-2010 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.fusesource.restygwt.client;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.fusesource.restygwt.client.Json.Style;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;

/**
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 * @author <a href="http://www.acuedo.com">Dave Finch</a>
 */
abstract public class AbstractJsonEncoderDecoder<T> implements JsonEncoderDecoder<T> {

    // /////////////////////////////////////////////////////////////////
    // Built in encoders for the native types.
    // /////////////////////////////////////////////////////////////////
    public static final AbstractJsonEncoderDecoder<Boolean> BOOLEAN =
            new AbstractJsonEncoderDecoder<Boolean>() {

                public Boolean decode(JSONValue value) throws DecodingException {
                    if (value == null || value.isNull() != null) {
                        return null;
                    }
                    JSONBoolean bool = value.isBoolean();
                    if (bool == null) {
                        throw new DecodingException("Expected a json boolean, but was given: "
                                + value);
                    }
                    return bool.booleanValue();
                }

                public JSONValue encode(Boolean value) throws EncodingException {
                    return (value == null) ? getNullType() : JSONBoolean.getInstance(value);
                }
            };

    public static final AbstractJsonEncoderDecoder<Character> CHAR =
            new AbstractJsonEncoderDecoder<Character>() {

                public Character decode(JSONValue value) throws DecodingException {
                    if (value == null || value.isNull() != null) {
                        return null;
                    }
                    return (char) toDouble(value);
                }

                public JSONValue encode(Character value) throws EncodingException {
                    return (value == null) ? getNullType() : new JSONNumber(value);
                }
            };

    public static final AbstractJsonEncoderDecoder<Byte> BYTE =
            new AbstractJsonEncoderDecoder<Byte>() {

                public Byte decode(JSONValue value) throws DecodingException {
                    if (value == null || value.isNull() != null) {
                        return null;
                    }
                    return (byte) toDouble(value);
                }

                public JSONValue encode(Byte value) throws EncodingException {
                    if (value == null) {
                        return null;
                    }
                    return new JSONNumber(value);
                }
            };

    public static final AbstractJsonEncoderDecoder<Short> SHORT =
            new AbstractJsonEncoderDecoder<Short>() {

                public Short decode(JSONValue value) throws DecodingException {
                    if (value == null || value.isNull() != null) {
                        return null;
                    }
                    return (short) toDouble(value);
                }

                public JSONValue encode(Short value) throws EncodingException {
                    return (value == null) ? getNullType() : new JSONNumber(value);
                }
            };

    public static final AbstractJsonEncoderDecoder<Integer> INT =
            new AbstractJsonEncoderDecoder<Integer>() {

                public Integer decode(JSONValue value) throws DecodingException {
                    if (value == null || value.isNull() != null) {
                        return null;
                    }
                    return (int) toDouble(value);
                }

                public JSONValue encode(Integer value) throws EncodingException {
                    return (value == null) ? getNullType() : new JSONNumber(value);
                }
            };

    public static final AbstractJsonEncoderDecoder<Long> LONG =
            new AbstractJsonEncoderDecoder<Long>() {

                public Long decode(JSONValue value) throws DecodingException {
                    if (value == null || value.isNull() != null) {
                        return null;
                    }
                    return (long) toDouble(value);
                }

                public JSONValue encode(Long value) throws EncodingException {
                    return (value == null) ? getNullType() : new JSONNumber(value);
                }
            };

    public static final AbstractJsonEncoderDecoder<Float> FLOAT =
            new AbstractJsonEncoderDecoder<Float>() {

                public Float decode(JSONValue value) throws DecodingException {
                    if (value == null || value.isNull() != null) {
                        return null;
                    }
                    return (float) toDouble(value);
                }

                public JSONValue encode(Float value) throws EncodingException {
                    return (value == null) ? getNullType() : new JSONNumber(value);
                }
            };

    public static final AbstractJsonEncoderDecoder<Double> DOUBLE =
            new AbstractJsonEncoderDecoder<Double>() {

                public Double decode(JSONValue value) throws DecodingException {
                    if (value == null || value.isNull() != null) {
                        return null;
                    }
                    return toDouble(value);
                }

                public JSONValue encode(Double value) throws EncodingException {
                    return (value == null) ? getNullType() : new JSONNumber(value);
                }
            };

    public static final AbstractJsonEncoderDecoder<String> STRING =
            new AbstractJsonEncoderDecoder<String>() {

                public String decode(JSONValue value) throws DecodingException {
                    if (value == null || value.isNull() != null) {
                        return null;
                    }
                    JSONString str = value.isString();

                    if (str == null) {
                        throw new DecodingException("Expected a json string, but was given: "
                                + value);
                    }

                    return str.stringValue();
                }

                public JSONValue encode(String value) throws EncodingException {
                    return (value == null) ? getNullType() : new JSONString(value);
                }
            };

    public static final AbstractJsonEncoderDecoder<SafeHtml> SAFE_HTML =
            new AbstractJsonEncoderDecoder<SafeHtml>() {

                public SafeHtml decode(JSONValue value) throws DecodingException {
                    if (value == null || value.isNull() != null) {
                        SafeHtmlBuilder sh = new SafeHtmlBuilder();
                        sh.appendEscaped("");
                        return sh.toSafeHtml();
                    }
                    JSONString str = value.isString();

                    if (str == null) {
                        throw new DecodingException("Expected a json string, but was given: "
                                + value);
                    }

                    SafeHtmlBuilder sh = new SafeHtmlBuilder();
                    sh.appendEscaped(str.stringValue());
                    return sh.toSafeHtml();
                }

                @Override
                public JSONValue encode(SafeHtml value)
                        throws org.fusesource.restygwt.client.JsonEncoderDecoder.EncodingException {
                    return (value == null) ? getNullType() : new JSONString(value.asString());
                }
            };

    public static final AbstractJsonEncoderDecoder<BigDecimal> BIG_DECIMAL =
            new AbstractJsonEncoderDecoder<BigDecimal>() {

                public BigDecimal decode(JSONValue value) throws DecodingException {
                    if (value == null || value.isNull() != null) {
                        return null;
                    }
                    return toBigDecimal(value);
                }

                public JSONValue encode(BigDecimal value) throws EncodingException {
                    return (value == null) ? getNullType() : new JSONString(value.toString());
                }
            };

    public static final AbstractJsonEncoderDecoder<BigInteger> BIG_INTEGER =
            new AbstractJsonEncoderDecoder<BigInteger>() {

                public BigInteger decode(JSONValue value) throws DecodingException {
                    if (value == null || value.isNull() != null) {
                        return null;
                    }
                    JSONNumber number = value.isNumber();
                    if (number == null) {
                        throw new DecodingException("Expected a json number, but was given: "
                                + value);
                    }

                    // Doing a straight conversion from string to BigInteger will not work for large
                    // values
                    // So we convert to BigDecimal first and then convert it to BigInteger.
                    return new BigDecimal(value.toString()).toBigInteger();
                }

                public JSONValue encode(BigInteger value) throws EncodingException {
                    return (value == null) ? getNullType() : new JSONString(value.toString());
                }
            };

    public static final AbstractJsonEncoderDecoder<Document> DOCUMENT =
            new AbstractJsonEncoderDecoder<Document>() {

                public Document decode(JSONValue value) throws DecodingException {
                    if (value == null || value.isNull() != null) {
                        return null;
                    }
                    JSONString str = value.isString();
                    if (str == null) {
                        throw new DecodingException("Expected a json string, but was given: "
                                + value);
                    }
                    return XMLParser.parse(str.stringValue());
                }

                public JSONValue encode(Document value) throws EncodingException {
                    return (value == null) ? getNullType() : new JSONString(value.toString());
                }
            };

    public static final AbstractJsonEncoderDecoder<JSONValue> JSON_VALUE =
            new AbstractJsonEncoderDecoder<JSONValue>() {

                public JSONValue decode(JSONValue value) throws DecodingException {
                    return value;
                }

                public JSONValue encode(JSONValue value) throws EncodingException {
                    return value;
                }
            };

    public static final AbstractJsonEncoderDecoder<List<Integer>> LIST_INT = new
            AbstractJsonEncoderDecoder<List<Integer>>() {
                @Override
                public JSONValue encode(List<Integer> value) throws EncodingException {
                    return toJSON(value, INT);
                }

                @Override
                public List<Integer> decode(JSONValue value) throws DecodingException {
                    return toList(value, INT);
                }
            };

    public static final AbstractJsonEncoderDecoder<Set<Integer>> SET_INT = new
            AbstractJsonEncoderDecoder<Set<Integer>>() {
                @Override
                public JSONValue encode(Set<Integer> value) throws EncodingException {
                    return toJSON(value, INT);
                }

                @Override
                public Set<Integer> decode(JSONValue value) throws DecodingException {
                    return toSet(value, INT);
                }
            };

    public static final AbstractJsonEncoderDecoder<Date> DATE =
            new AbstractJsonEncoderDecoder<Date>() {

                public Date decode(JSONValue value) throws DecodingException {
                    if (value == null || value.isNull() != null) {
                        return null;
                    }
                    String format = Defaults.getDateFormat();
                    if (format == null) {
                        JSONNumber num = value.isNumber();
                        if (num == null) {
                            throw new DecodingException("Expected a json number, but was given: "
                                    + value);
                        }
                        return new Date((long) num.doubleValue());
                    } else {
                        JSONString str = value.isString();
                        if (str == null) {
                            throw new DecodingException("Expected a json string, but was given: "
                                    + value);
                        }
                        return DateTimeFormat.getFormat(format).parse(str.stringValue());
                    }
                }

                public JSONValue encode(Date value) throws EncodingException {
                    if (value == null) {
                        return getNullType();
                    }
                    String format = Defaults.getDateFormat();
                    if (format == null) {
                        return new JSONNumber(value.getTime());
                    } else {
                        return new JSONString(DateTimeFormat.getFormat(format).format(value));
                    }
                }
            };

    // /////////////////////////////////////////////////////////////////
    // Helper Methods.
    // /////////////////////////////////////////////////////////////////

    static public BigDecimal toBigDecimal(JSONValue value) {
        JSONString stringNumber = value.isString();
        if (stringNumber != null) {
            return new BigDecimal(stringNumber.stringValue());
        }
        JSONNumber number = value.isNumber();
        if (number != null) {
            return new BigDecimal(number.doubleValue());
        }

        throw new DecodingException("Expected a json number, but was given: " + value);
    }

    static public double toDouble(JSONValue value) {
        JSONNumber number = value.isNumber();
        if (number == null) {
            throw new DecodingException("Expected a json number, but was given: " + value);
        }
        return number.doubleValue();
    }

    static public JSONObject toObject(JSONValue value) {
        JSONObject object = value.isObject();
        if (object == null) {
            throw new DecodingException("Expected a json obejct, but was given: " + object);
        }
        return object;
    }

    static public <Type> List<Type> toList(JSONValue value, AbstractJsonEncoderDecoder<Type> encoder) {
        if (value == null || value.isNull() != null) {
            return null;
        }
        JSONArray array = value.isArray();
        if (array == null) {
            throw new DecodingException("Expected a json array, but was given: " + value);
        }

        ArrayList<Type> rc = new ArrayList<Type>(array.size());
        int size = array.size();
        for (int i = 0; i < size; i++) {
            rc.add(encoder.decode(array.get(i)));
        }
        return rc;
    }

    static public <Type> Set<Type> toSet(JSONValue value, AbstractJsonEncoderDecoder<Type> encoder) {
        if (value == null || value.isNull() != null) {
            return null;
        }
        JSONArray array = value.isArray();
        if (array == null) {
            throw new DecodingException("Expected a json array, but was given: " + value);
        }

        HashSet<Type> rc = new HashSet<Type>(array.size() * 2);
        int size = array.size();
        for (int i = 0; i < size; i++) {
            rc.add(encoder.decode(array.get(i)));
        }
        return rc;
    }

    static public <Type> Map<String, Type> toMap(JSONValue value,
            AbstractJsonEncoderDecoder<Type> encoder, Style style) {
        if (value == null || value.isNull() != null) {
            return null;
        }

        switch (style) {
            case DEFAULT:
            case SIMPLE: {
                JSONObject object = value.isObject();
                if (object == null) {
                    throw new DecodingException("Expected a json object, but was given: " + value);
                }

                HashMap<String, Type> rc = new HashMap<String, Type>(object.size() * 2);
                for (String key : object.keySet()) {
                    rc.put(key, encoder.decode(object.get(key)));
                }
                return rc;
            }
            case JETTISON_NATURAL: {
                JSONObject object = value.isObject();
                if (object == null) {
                    throw new DecodingException("Expected a json object, but was given: " + value);
                }
                value = object.get("entry");
                if (value == null) {
                    throw new DecodingException("Expected an entry array not found");
                }
                JSONArray entries = value.isArray();
                if (entries == null) {
                    throw new DecodingException("Expected an entry array, but was given: " + value);
                }

                HashMap<String, Type> rc = new HashMap<String, Type>(object.size() * 2);
                for (int i = 0; i < entries.size(); i++) {
                    JSONObject entry = entries.get(i).isObject();
                    if (entry == null) {
                        throw new DecodingException("Expected an entry object, but was given: "
                                + value);
                    }
                    JSONValue key = entry.get("key");
                    if (key == null) {
                        throw new DecodingException("Expected an entry key field not found");
                    }
                    JSONString k = key.isString();
                    if (k == null) {
                        throw new DecodingException(
                                "Expected an entry key to be a string, but was given: " + value);
                    }

                    rc.put(k.stringValue(), encoder.decode(entry.get("value")));
                }
                return rc;
            }
            default:
                throw new UnsupportedOperationException("The encoding style is not yet suppored: "
                        + style.name());
        }
    }

    static public <Type> JSONValue toJSON(Map<String, Type> value,
            AbstractJsonEncoderDecoder<Type> encoder, Style style) {
        if (value == null) {
            return JSONNull.getInstance();
        }

        switch (style) {
            case DEFAULT:
            case SIMPLE: {
                JSONObject rc = new JSONObject();
                for (Entry<String, Type> t : value.entrySet()) {
                    rc.put(t.getKey(), encoder.encode(t.getValue()));
                }
                return rc;
            }
            case JETTISON_NATURAL: {
                JSONObject rc = new JSONObject();
                JSONArray entries = new JSONArray();
                int i = 0;
                for (Entry<String, Type> t : value.entrySet()) {
                    JSONObject entry = new JSONObject();
                    entry.put("key", new JSONString(t.getKey()));
                    entry.put("value", encoder.encode(t.getValue()));
                    entries.set(i++, entry);
                }
                rc.put("entry", entries);
                return rc;
            }
            default:
                throw new UnsupportedOperationException("The encoding style is not yet suppored: "
                        + style.name());
        }
    }

    static public <Type> JSONValue toJSON(Collection<Type> value,
            AbstractJsonEncoderDecoder<Type> encoder) {
        if (value == null) {
            return JSONNull.getInstance();
        }
        JSONArray rc = new JSONArray();
        int i = 0;
        for (Type t : value) {
            rc.set(i++, encoder.encode(t));
        }
        return rc;
    }

    static private JSONNull getNullType() {
        return (Defaults.doesIgnoreJsonNulls()) ? null : JSONNull.getInstance();
    }
}
