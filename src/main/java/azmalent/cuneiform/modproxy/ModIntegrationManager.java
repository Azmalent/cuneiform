package azmalent.cuneiform.modproxy;

import azmalent.cuneiform.Cuneiform;
import azmalent.cuneiform.util.ReflectionUtil;
import com.google.common.collect.Maps;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData.AnnotationData;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class ModIntegrationManager {
    @SuppressWarnings("unchecked")
    public static void initModProxies(Class containerClass, String modid) {
        Object instance = ReflectionUtil.getSingletonInstanceOrNull(containerClass);

        Map<String, Field> proxyFields = findProxyFields(containerClass);
        Map<String, Class<?>> dummies = getDummies(modid);

        for (Map.Entry<String, Field> proxyField : proxyFields.entrySet()) {
            String targetModid = proxyField.getKey();
            Field field = proxyField.getValue();
            try {
                Object proxy = createProxy(modid, targetModid, dummies.get(targetModid), field.getType());
                field.set(instance, proxy);
            } catch (Exception e) {
                Cuneiform.LOGGER.error(String.format("Failed to instantiate proxy for mod %s!", targetModid), e);
            }
        }
    }

    private static <TMod> Map<String, Field> findProxyFields(Class<TMod> clazz) {
        Map<String, Field> proxies = Maps.newHashMap();

        for (Field field : clazz.getFields()) {
            ModProxy proxyAnnotation = field.getAnnotation(ModProxy.class);
            if (proxyAnnotation != null) {
                String modid = proxyAnnotation.value();
                proxies.put(modid, field);
            }
        }

        return proxies;
    }

    private static <TAnnotation extends Annotation> Map<String, Class<?>> getDummies(String modid) {
        List<AnnotationData> annotationData = ReflectionUtil.getAnnotationDataFromMod(modid, IntegrationDummy.class);
        Map<String, Class<?>> result = Maps.newHashMap();
        for (AnnotationData data : annotationData) {
            String targetModid = (String) data.annotationData().get("value");
            Class<?> clazz = ReflectionUtil.getClassOrNull(data.clazz().getClassName());
            result.put(targetModid, clazz);
        }

        return result;
    }

    private static Class getImpl(String thisModid, String targetModid) {
        List<AnnotationData> annotationData = ReflectionUtil.getAnnotationDataFromMod(thisModid, IntegrationImpl.class);
        for (AnnotationData data : annotationData) {
            String modid = (String) data.annotationData().get("value");
            if (modid.equals(targetModid)) {
                return ReflectionUtil.getClassOrNull(data.clazz().getClassName());
            }
        }

        return null;
    }

    private static Object createProxy(String thisModid, String targetModid, Class<?> dummyClass, Class<?> fieldType) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        if (dummyClass == null) {
            if (fieldType != IModProxy.class) {
                throw new ClassNotFoundException("Missing dummy proxy for " + targetModid);
            }

            dummyClass = IModProxy.Dummy.class;
        }

        try {
            if (ModList.get().isLoaded(targetModid)) {
                Class<?> implClass = getImpl(thisModid, targetModid);
                if (implClass == null) {
                    throw new ClassNotFoundException("Failed to instantiate  " + targetModid);
                }

                var constructor = implClass.asSubclass(fieldType).getConstructor();
                return constructor.newInstance();
            } else {
                var constructor = dummyClass.asSubclass(fieldType).getConstructor();
                return constructor.newInstance();
            }
        } catch (ReflectiveOperationException e) {
            Cuneiform.LOGGER.error(String.format("Failed to instantiate proxy for mod %s!", targetModid));
            return null;
        }
    }
}
