package azmalent.cuneiform.modproxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field as a mod integration proxy that should be automatically populated
 * by {@link ModIntegrationManager}.
 *
 * <p>The field type should be {@link IModProxy} or a more specific interface.
 * During mod construction, the manager will:</p>
 * <ol>
 *   <li>Check if the target mod is loaded
 *   <li>If loaded: instantiate the {@link IntegrationImpl @IntegrationImpl}-annotated implementation</li>
 *   <li>If not loaded: instantiate the {@link IntegrationDummy @IntegrationDummy}-annotated fallback</li>
 *   <li>Inject the instance into the annotated field</li>
 * </ol>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * @ModProxy("jei")
 * public static IModProxy jei;
 * }</pre>
 *
 * @see IntegrationImpl
 * @see IntegrationDummy
 * @see ModIntegrationManager
 */
@Target(ElementType.FIELD)
@Retention(value=RetentionPolicy.RUNTIME)
public @interface ModProxy {
    String targetModid();
}
