package azmalent.cuneiform.modproxy;

/**
 * Marks a class as the real implementation of a mod integration proxy.
 *
 * <p>The class must implement the interface expected by the corresponding
 * {@link ModProxy @ModProxy}-annotated field. It will be instantiated only
 * when the target mod is loaded.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * @IntegrationImpl("jei")
 * public class JeiIntegration implements IModProxy {
 *     {@literal @}Override
 *     public void register(IEventBus bus) {
 *         // register JEI-specific handlers
 *     }
 * }
 * }</pre>
 *
 * @see ModProxy
 * @see IntegrationDummy
 */
public @interface IntegrationImpl {
    String targetModid();
}
