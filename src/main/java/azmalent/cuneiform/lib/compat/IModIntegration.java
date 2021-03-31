package azmalent.cuneiform.lib.compat;

import net.minecraftforge.eventbus.api.IEventBus;

public interface IModIntegration {
    void register(IEventBus bus);
}
