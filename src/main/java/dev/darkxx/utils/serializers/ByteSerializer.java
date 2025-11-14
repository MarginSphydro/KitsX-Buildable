package dev.darkxx.utils.serializers;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/* loaded from: KitsX-1.0.3-all.jar:dev/darkxx/utils/serializers/ByteSerializer.class */
public class ByteSerializer {
    public byte[] serialize(ItemStack item) {
        return item.serializeAsBytes();
    }

    public byte[] serialize(List<ItemStack> items) {
        return serialize((ItemStack[]) items.toArray(new ItemStack[0]));
    }

    public byte[] serialize(ItemStack[] items) {
        ByteArrayDataOutput stream = ByteStreams.newDataOutput();
        int length = items.length;
        for (int i = 0; i < length; i++) {
            ItemStack item = items[i];
            byte[] data = (item == null || item.getType() == Material.AIR) ? new byte[0] : item.serializeAsBytes();
            stream.writeInt(data.length);
            stream.write(data);
        }
        stream.writeInt(-1);
        return stream.toByteArray();
    }

    public ItemStack deserialize(byte[] bytes) {
        return ItemStack.deserializeBytes(bytes);
    }

    public ItemStack[] deserializeArray(byte[] bytes) {
        List<ItemStack> items = new ArrayList<>();
        ByteArrayDataInput inputStream = ByteStreams.newDataInput(bytes);
        int i = inputStream.readInt();
        while (true) {
            int length = i;
            if (length != -1) {
                if (length == 0) {
                    items.add(null);
                } else {
                    byte[] data = new byte[length];
                    inputStream.readFully(data, 0, data.length);
                    items.add(ItemStack.deserializeBytes(data));
                }
                i = inputStream.readInt();
            } else {
                return (ItemStack[]) items.toArray(new ItemStack[0]);
            }
        }
    }

    public List<ItemStack> deserializeList(byte[] bytes) {
        return Arrays.asList(deserializeArray(bytes));
    }
}
