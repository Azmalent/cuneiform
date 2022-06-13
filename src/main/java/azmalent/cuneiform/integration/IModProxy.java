package azmalent.cuneiform.integration;

import net.minecraftforge.eventbus.api.IEventBus;

public interface IModProxy {
    void register(IEventBus bus);

    class Dummy implements IModProxy {
        @Override
        public void register(IEventBus bus) {
            //NO-OP
        }
    }
}
