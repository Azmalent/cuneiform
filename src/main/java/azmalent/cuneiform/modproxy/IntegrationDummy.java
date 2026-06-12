package azmalent.cuneiform.modproxy;

/**
 * Marks a class as a no-op fallback implementation of a mod integration proxy.
 *
 * <p>The class must implement the interface expected by the corresponding
 * {@link ModProxy @ModProxy}-annotated field. It will be instantiated when
 * the target mod is <b>not</b> loaded,
 * providing safe no-op behavior.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * @IntegrationDummy("jei")
 * public class JeiDummy implements IModProxy {
 *     @Override
 *     public void register(IEventBus bus) {
 *         // NO-OP — JEI is not installed
 *     }
 * }
 * }</pre>
 *
 * @see ModProxy
 * @see IntegrationImpl
 */
public @interface IntegrationDummy {
    String targetModid();
}
