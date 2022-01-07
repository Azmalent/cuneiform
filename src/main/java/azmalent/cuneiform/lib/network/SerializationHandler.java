package azmalent.cuneiform.lib.network;

import azmalent.cuneiform.Cuneiform;
import azmalent.cuneiform.lib.util.ReflectionUtil;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.SerializationException;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.RecordComponent;
import java.util.Map;
import java.util.UUID;

/**
 * Automagically serializes basic network messages using reflection. <p>
 * The message class must be a record implementing IMessage. <p>
 * Loosely based on <a href="https://github.com/VazkiiMods/AutoRegLib/blob/master/src/main/java/vazkii/arl/network/MessageSerializer.java">MessageSerializer.java</a>
 * from AutoRegLib.
 */
@SuppressWarnings({"unchecked", "unused"})
public final class SerializationHandler {
    private static final Map<Class<?>, INetworkSerializer<?>> serializers = Maps.newHashMap();

    public static <T> void registerSerializer(Class<T> clazz, INetworkSerializer<T> serializer) {
        serializers.put(clazz, serializer);
    }

    public static <T> void registerSerializer(Class<T> clazz, NetworkReader<T> reader, NetworkWriter<T> writer) {
        serializers.put(clazz, new NetworkSerializer<T>(reader, writer));
    }

    //TODO: list & map support
    static {
        SerializationHandler.<Byte>registerSerializer(byte.class, FriendlyByteBuf::readByte, FriendlyByteBuf::writeByte);
        SerializationHandler.<Short>registerSerializer(short.class, FriendlyByteBuf::readShort, FriendlyByteBuf::writeShort);
        SerializationHandler.<Integer>registerSerializer(int.class, FriendlyByteBuf::readInt, FriendlyByteBuf::writeInt);
        SerializationHandler.<Long>registerSerializer(long.class, FriendlyByteBuf::readLong, FriendlyByteBuf::writeLong);
        SerializationHandler.<Character>registerSerializer(char.class, FriendlyByteBuf::readChar, FriendlyByteBuf::writeChar);
        registerSerializer(float.class, FriendlyByteBuf::readFloat, FriendlyByteBuf::writeFloat);
        registerSerializer(double.class, FriendlyByteBuf::readDouble, FriendlyByteBuf::writeDouble);
        registerSerializer(boolean.class, FriendlyByteBuf::readBoolean, FriendlyByteBuf::writeBoolean);
        registerSerializer(String.class, buf -> buf.readUtf(32767), FriendlyByteBuf::writeUtf);
        registerSerializer(ResourceLocation.class, FriendlyByteBuf::readResourceLocation, FriendlyByteBuf::writeResourceLocation);
        registerSerializer(ItemStack.class, FriendlyByteBuf::readItem, (buf, stack) -> buf.writeItemStack(stack, false));
        registerSerializer(CompoundTag.class, FriendlyByteBuf::readNbt, FriendlyByteBuf::writeNbt);
        registerSerializer(BlockPos.class, FriendlyByteBuf::readBlockPos, FriendlyByteBuf::writeBlockPos);
        registerSerializer(UUID.class, FriendlyByteBuf::readUUID, FriendlyByteBuf::writeUUID);
    }

    public static <T extends Record & IMessage> void encodeMessage(FriendlyByteBuf buffer, T message) {
        Class<T> clazz = (Class<T>) message.getClass();
        assert clazz.isRecord();

        try {
            for (RecordComponent recordComponent : ReflectionUtil.getRecordComponents(clazz)) {
                Class<?> fieldType = recordComponent.getType();
                Object value = recordComponent.getAccessor().invoke(message);
                writeToBuffer(buffer, value, fieldType);
            }
        } catch (Exception e) {
            Cuneiform.LOGGER.error(String.format("Failed to encode message %s!", message.getClass().getCanonicalName()));
            e.printStackTrace();
        }
    }

    public static <T extends Record & IMessage> T decodeMessage(Class<T> clazz, FriendlyByteBuf buffer) {
        assert clazz.isRecord();

        try {
            RecordComponent[] recordComponents = ReflectionUtil.getRecordComponents(clazz);
            Class<?>[] fieldTypes = new Class<?>[recordComponents.length];
            Object[] fieldValues = new Object[recordComponents.length];

            for (int i = 0; i < recordComponents.length; i++) {
                Class<?> fieldType = recordComponents[i].getType();
                fieldTypes[i] = fieldType;
                fieldValues[i] = readFromBuffer(buffer, fieldType);
            }

            Constructor<?> constructor = clazz.getConstructor(fieldTypes);
            return (T) constructor.newInstance(fieldValues);
        } catch (Exception e) {
            Cuneiform.LOGGER.error(String.format("Failed to decode message %s!", clazz.getCanonicalName()));
            e.printStackTrace();
            return null;
        }
    }

    private static Object readFromBuffer(FriendlyByteBuf buffer, Class<?> clazz) {
        if (serializers.containsKey(clazz)) {
            return serializers.get(clazz).read(buffer);
        } else if (clazz.isEnum()) {
            return buffer.readEnum(clazz.asSubclass(Enum.class));
        } else if (clazz.isArray()) {
            Class<?> componentClass = clazz.getComponentType();

            int length = buffer.readInt();
            Object array = Array.newInstance(componentClass, length);
            for (int i = 0; i < length; i++) {
                Array.set(array, i, readFromBuffer(buffer, componentClass));
            }

            return array;
        }

        throw new SerializationException(String.format("Type %s does not support automatic serialization.", clazz.getSimpleName()));
    }

    private static void writeToBuffer(FriendlyByteBuf buffer, Object value, Class<?> clazz) {
        if (serializers.containsKey(clazz)) {
            serializers.get(clazz).write(buffer, value);
        } else if (clazz.isEnum()) {
            buffer.writeEnum((Enum<?>) value);
        } else if (clazz.isArray()) {
            Class<?> componentClass = clazz.getComponentType();

            int length = Array.getLength(value);
            buffer.writeInt(length);
            for (int i = 0; i < length; i++) {
                writeToBuffer(buffer, Array.get(value, i), componentClass);
            }
        } else {
            throw new SerializationException(String.format("Type %s does not support automatic serialization.", clazz.getSimpleName()));
        }
    }

    private record NetworkSerializer<T>(NetworkReader<T> reader, NetworkWriter<T> writer) implements INetworkSerializer<T> {
        @Override
        public T read(FriendlyByteBuf buffer) {
            return reader.read(buffer);
        }

        @Override
        public void write(FriendlyByteBuf buffer, Object value) {
            writer.write(buffer, (T) value);
        }
    }
}
