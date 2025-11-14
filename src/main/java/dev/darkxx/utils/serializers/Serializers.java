package dev.darkxx.utils.serializers;

import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/serializers/Serializers.class */
public class Serializers {
    @NotNull
    public static Base64Serializer base64() {
        return new Base64Serializer();
    }

    @NotNull
    public static ByteSerializer bytes() {
        return new ByteSerializer();
    }
}
