package azmalent.cuneiform.lib.compat;

import net.minecraftforge.eventbus.api.IEventBus;

public class BasicModDummy implements IModIntegration {
    @Override
    public void register(IEventBus bus) {
        //NO-OP
    }
}
