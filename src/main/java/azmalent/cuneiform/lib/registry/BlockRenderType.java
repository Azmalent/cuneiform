package azmalent.cuneiform.lib.registry;

import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@SuppressWarnings("unused")
public enum BlockRenderType {
    SOLID, CUTOUT, CUTOUT_MIPPED, TRANSCULENT;

    @OnlyIn(Dist.CLIENT)
    public RenderType get() {
        switch (this) {
            case SOLID:         return RenderType.getSolid();
            case CUTOUT:        return RenderType.getCutout();
            case CUTOUT_MIPPED: return RenderType.getCutoutMipped();
            case TRANSCULENT:   return RenderType.getTranslucent();
        }

        throw new AssertionError("Method did not return a value!");
    }
}
