package azmalent.cuneiform.lib.util;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.forgespi.language.ModFileScanData.AnnotationData;
import org.objectweb.asm.Type;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public final class ReflectionUtil {
    public static Class<?> tryGetClass(@Nonnull String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Field tryGetField(@Nonnull String name, @Nonnull Class<?> clazz) {
        try {
            return clazz.getField(name);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getSingletonInstance(@Nonnull Class<T> clazz) {
        Field instanceField = tryGetField("INSTANCE", clazz);
        if (instanceField != null && instanceField.getType().equals(clazz)) {
            try {
                return (T) instanceField.get(null);
            } catch (IllegalAccessException e) {
                return null;
            }
        }

        return null;
    }

    @Nonnull
    public static <TAnnotation extends Annotation> TAnnotation getAnnotation(@Nonnull Class<?> clazz, @Nonnull Class<TAnnotation> annotationClass) {
        TAnnotation annotation = (TAnnotation) clazz.getAnnotation(annotationClass);
        if (annotation != null) return annotation;

        String className = clazz.getSimpleName();
        String annotationName = annotationClass.getSimpleName();
        throw new IllegalStateException(String.format("The class %s must be annotated with @%s.", className, annotationName));
    }

    public static <TAnnotation extends Annotation> List<AnnotationData> getAnnotationDataFromMod(String modid, Class<TAnnotation> annotationClass) {
        Type requiredType = Type.getType(annotationClass);

        ModFileScanData scanData = ModList.get().getModFileById(modid).getFile().getScanResult();
        return scanData.getAnnotations().stream()
                .filter(annotationData -> annotationData.annotationType().equals(requiredType))
                .collect(Collectors.toList());
    }
}
