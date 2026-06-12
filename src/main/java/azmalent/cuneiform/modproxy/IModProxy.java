package azmalent.cuneiform.modproxy;

import net.minecraftforge.eventbus.api.IEventBus;

/**
 * Interface for mod integration proxies that provide optional-mod functionality.
 *
 * <p>When a target mod is loaded, an {@link IntegrationImpl @IntegrationImpl}-annotated
 * implementation is instantiated and injected into the proxy field. When the target
 * mod is not loaded, an {@link IntegrationDummy @IntegrationDummy}-annotated no-op
 * fallback is used instead.</p>
 *
 * <p>The {@link #register(IEventBus)} method is called during mod construction to
 * register event handlers specific to the integration.</p>
 *
 * @see ModProxy
 * @see ModIntegrationManager
 */
public interface IModProxy {
    /**
     * Registers event handlers for this integration on the given event bus.
     */
    void register(IEventBus bus);

    /**
     * A no-op implementation of {@link IModProxy} used as a fallback when no
     * dummy implementation is explicitly provided.
     */
    class Dummy implements IModProxy {
        @Override
        public void register(IEventBus bus) {
            //NO-OP
        }
    }
}
