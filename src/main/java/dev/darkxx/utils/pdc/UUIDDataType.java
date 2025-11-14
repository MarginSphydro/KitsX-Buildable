package dev.darkxx.utils.pdc;

import java.nio.ByteBuffer;
import java.util.UUID;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/pdc/UUIDDataType.class */
public class UUIDDataType implements PersistentDataType<byte[], UUID> {
    public static UUIDDataType of() {
        return new UUIDDataType();
    }

    @NotNull
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @NotNull
    public Class<UUID> getComplexType() {
        return UUID.class;
    }

    public byte[] toPrimitive(UUID complex, @NotNull PersistentDataAdapterContext context) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
        byteBuffer.putLong(complex.getMostSignificantBits());
        byteBuffer.putLong(complex.getLeastSignificantBits());
        return byteBuffer.array();
    }

    @NotNull
    public UUID fromPrimitive(byte[] primitive, @NotNull PersistentDataAdapterContext context) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(primitive);
        long firstLong = byteBuffer.getLong();
        long secondLong = byteBuffer.getLong();
        return new UUID(firstLong, secondLong);
    }
}
