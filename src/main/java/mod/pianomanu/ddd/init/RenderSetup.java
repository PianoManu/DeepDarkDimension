package mod.pianomanu.ddd.init;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;

public class RenderSetup {

    public static void init() {
        RenderTypeLookup.setRenderLayer(Registration.DDD_TELEPORTER_BLOCK.get(), RenderType.translucent());
    }
}
