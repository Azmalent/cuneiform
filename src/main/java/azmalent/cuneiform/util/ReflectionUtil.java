package azmalent.cuneiform.util;

import com.google.common.collect.Maps;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.forgespi.language.ModFileScanData.AnnotationData;
import org.objectweb.asm.Type;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.RecordComponent;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class ReflectionUtil {
    private static final Map<Class<?>, RecordComponent[]> recordCache = Maps.newHashMap();

    public static Class<?> getClassOrNull(@Nonnull String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Field getFieldOrNull(@Nonnull Class<?> clazz, @Nonnull String name) {
        try {
            return clazz.getField(name);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getSingletonInstanceOrNull(@Nonnull Class<T> clazz) {
        Field instanceField = getFieldOrNull(clazz, "INSTANCE");
        if (instanceField != null && instanceField.getType().equals(clazz)) {
            try {
                return (T) instanceField.get(null);
            } catch (IllegalAccessException e) {
                return null;
            }
        }

        return null;
    }

    public static boolean isSubclass(Class<?> clazz, Class<?> parent) {
        return clazz != null && parent != null && parent.isAssignableFrom(clazz);
    }

    public static <T extends Record> RecordComponent[] getRecordComponents(Class<T> clazz) {
        assert clazz.isRecord();
        return recordCache.computeIfAbsent(clazz, Class::getRecordComponents);
    }

    public static <TAnnotation extends Annotation> List<AnnotationData> getAnnotationDataFromMod(String modid, Class<TAnnotation> annotationClass) {
        Type requiredType = Type.getType(annotationClass);

        ModFileScanData scanData = ModList.get().getModFileById(modid).getFile().getScanResult();
        return scanData.getAnnotations().stream()
                .filter(annotationData -> annotationData.annotationType().equals(requiredType))
                .collect(Collectors.toList());
    }
}
