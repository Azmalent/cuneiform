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

/**
 * Utility methods for reflection operations used throughout the mod.
 *
 * <p>All methods are null-safe where documented, returning {@code null} instead
 * of throwing exceptions for missing classes, fields, etc.</p>
 */
public final class ReflectionUtil {
    private static final Map<Class<?>, RecordComponent[]> recordCache = Maps.newHashMap();

    /**
     * Attempts to load a class by name, returning {@code null} if not found.
     *
     * @param name the fully qualified class name
     * @return the loaded class, or {@code null} if not found
     */
    public static Class<?> getClassOrNull(@Nonnull String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * Gets a public field from a class, returning {@code null} if not found.
     *
     * @return the field, or {@code null} if not found
     */
    public static Field getFieldOrNull(@Nonnull Class<?> clazz, @Nonnull String name) {
        try {
            return clazz.getField(name);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    /**
     * Gets the singleton instance of a class by reading its public static
     * {@code INSTANCE} field.
     *
     * <p>The field must be of the same type as the class itself.</p>
     *
     * This supports Kotlin objects, since they compile to classes following this pattern.
     *
     * @return the singleton instance, or {@code null} if the field doesn't exist or can't be read
     */
    @SuppressWarnings("unchecked")
    public static <T> T getSingletonInstanceOrNull(@Nonnull Class<T> clazz) {
        var instanceField = getFieldOrNull(clazz, "INSTANCE");
        if (instanceField != null && instanceField.getType().equals(clazz)) {
            try {
                return (T) instanceField.get(null);
            } catch (IllegalAccessException e) {
                return null;
            }
        }

        return null;
    }

    /**
     * Checks whether {@code clazz} is a subclass of (or the same as) {@code parent}.
     *
     * @return {@code true} if {@code clazz} extends or implements {@code parent}
     */
    public static boolean isSubclass(Class<?> clazz, Class<?> parent) {
        return clazz != null && parent != null && parent.isAssignableFrom(clazz);
    }

    /**
     * Returns the record components of a record class, caching the result.
     *
     * @return the array of record components
     */
    public static <T extends Record> RecordComponent[] getRecordComponents(Class<T> clazz) {
        assert clazz.isRecord();
        return recordCache.computeIfAbsent(clazz, Class::getRecordComponents);
    }

    /**
     * Scans a mod's annotation data for all annotations of the given type.
     *
     * <p>Uses Forge's {@link ModFileScanData} to avoid loading classes prematurely.</p>
     *
     * @param modid the mod ID to scan
     * @param annotationClass the annotation type to search for
     * @param <TAnnotation> the annotation type
     * @return a list of matching annotation data entries
     */
    public static <TAnnotation extends Annotation> List<AnnotationData> getAnnotationDataFromMod(String modid, Class<TAnnotation> annotationClass) {
        var requiredType = Type.getType(annotationClass);

        var scanData = ModList.get().getModFileById(modid).getFile().getScanResult();
        return scanData.getAnnotations().stream()
            .filter(annotationData -> annotationData.annotationType().equals(requiredType))
            .collect(Collectors.toList());
    }
}
