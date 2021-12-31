package azmalent.cuneiform.lib.network;

import azmalent.cuneiform.Cuneiform;
import azmalent.cuneiform.lib.util.ReflectionUtil;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.RecordComponent;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Automagically serializes basic network messages using reflection.
 * The message class must be a record implementing IMessage.
 * Based on https://github.com/VazkiiMods/AutoRegLib/blob/master/src/main/java/vazkii/arl/network/MessageSerializer.java
 */
@SuppressWarnings("unchecked")
public final class SerializationHandler {
    private static final Map<Class<?>, TypeHandler<?>> typeHandlers = Maps.newHashMap();

    private static <T> void setTypeHandlers(Class<T> clazz, Function<FriendlyByteBuf, T> reader, BiConsumer<FriendlyByteBuf, T> writer) {
        typeHandlers.put(clazz, new TypeHandler<T>(reader, writer));
    }

    static {
        SerializationHandler.<Byte>setTypeHandlers(byte.class, FriendlyByteBuf::readByte, FriendlyByteBuf::writeByte);
        SerializationHandler.<Short>setTypeHandlers(short.class, FriendlyByteBuf::readShort, FriendlyByteBuf::writeShort);
        SerializationHandler.<Integer>setTypeHandlers(int.class, FriendlyByteBuf::readInt, FriendlyByteBuf::writeInt);
        SerializationHandler.<Long>setTypeHandlers(long.class, FriendlyByteBuf::readLong, FriendlyByteBuf::writeLong);
        SerializationHandler.<Character>setTypeHandlers(char.class, FriendlyByteBuf::readChar, FriendlyByteBuf::writeChar);

        setTypeHandlers(float.class, FriendlyByteBuf::readFloat, FriendlyByteBuf::writeFloat);
        setTypeHandlers(double.class, FriendlyByteBuf::readDouble, FriendlyByteBuf::writeDouble);
        setTypeHandlers(boolean.class, FriendlyByteBuf::readBoolean, FriendlyByteBuf::writeBoolean);

        setTypeHandlers(String.class, buf -> buf.readUtf(32767), FriendlyByteBuf::writeUtf);
        setTypeHandlers(ResourceLocation.class, FriendlyByteBuf::readResourceLocation, FriendlyByteBuf::writeResourceLocation);
        setTypeHandlers(ItemStack.class, FriendlyByteBuf::readItem, (buf, stack) -> buf.writeItemStack(stack, false));
        setTypeHandlers(CompoundTag.class, FriendlyByteBuf::readNbt, FriendlyByteBuf::writeNbt);
        setTypeHandlers(BlockPos.class, FriendlyByteBuf::readBlockPos, FriendlyByteBuf::writeBlockPos);
        setTypeHandlers(UUID.class, FriendlyByteBuf::readUUID, FriendlyByteBuf::writeUUID);
    }

    public static <T extends Record & IMessage> void encodeMessage(T message, FriendlyByteBuf buffer) {
        try {
            Class<T> clazz = (Class<T>) message.getClass();
            assert clazz.isRecord();

            for (RecordComponent recordComponent : ReflectionUtil.getRecordComponents(clazz)) {
                Class<?> fieldType = recordComponent.getType();
                Object value = recordComponent.getAccessor().invoke(message);
                typeHandlers.get(fieldType).write(buffer, value);
            }
        } catch (Exception e) {
            Cuneiform.LOGGER.error(String.format("Failed to encode message %s!", message.getClass().getCanonicalName()));
            e.getCause().printStackTrace();
        }
    }

    public static <T extends Record & IMessage> T decodeMessage(Class<T> clazz, FriendlyByteBuf buffer) {
        try {
            RecordComponent[] recordComponents = ReflectionUtil.getRecordComponents(clazz);
            Class<?>[] fieldTypes = new Class<?>[recordComponents.length];
            Object[] fieldValues = new Object[recordComponents.length];

            for (int i = 0; i < recordComponents.length; i++) {
                Class<?> fieldType = recordComponents[i].getType();
                fieldTypes[i] = fieldType;

                Object value = typeHandlers.get(fieldType).read(buffer);
                fieldValues[i] = value;
            }

            Constructor<?> constructor = clazz.getConstructor(fieldTypes);
            return (T) constructor.newInstance(fieldValues);
        } catch (Exception e) {
            Cuneiform.LOGGER.error(String.format("Failed to decode message %s!", clazz.getCanonicalName()));
            e.getCause().printStackTrace();
            return null;
        }
    }

    private record TypeHandler<T>(Function<FriendlyByteBuf, T> reader, BiConsumer<FriendlyByteBuf, T> writer) {
        T read(FriendlyByteBuf buffer) {
            return reader.apply(buffer);
        }

        void write(FriendlyByteBuf buffer, Object value) {
            writer.accept(buffer, (T) value);
        }
    }
}
