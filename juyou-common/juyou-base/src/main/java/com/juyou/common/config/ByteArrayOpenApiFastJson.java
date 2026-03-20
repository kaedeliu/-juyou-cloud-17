package com.juyou.common.config;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * Fastjson serializes byte[] as base64 by default.
 * OpenAPI endpoint may return JSON bytes, which should be written as raw JSON text.
 */
public class ByteArrayOpenApiFastJson implements ObjectSerializer {

    public static final ByteArrayOpenApiFastJson instance = new ByteArrayOpenApiFastJson();

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
            throws IOException {
        SerializeWriter out = serializer.getWriter();
        if (!(object instanceof byte[])) {
            out.writeNull();
            return;
        }
        byte[] bytes = (byte[]) object;
        if (bytes.length == 0) {
            out.write("[]");
            return;
        }
        String text = new String(bytes, StandardCharsets.UTF_8).trim();
        if (text.startsWith("{") || text.startsWith("[")) {
            out.write(text);
            return;
        }
        // Fallback to default base64 behavior for non-JSON binary bytes.
        out.writeByteArray(bytes);
    }
}
