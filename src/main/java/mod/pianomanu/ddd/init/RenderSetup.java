package mod.pianomanu.ddd.init;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;

/**
 * Will add description later...
 *
 * @author PianoManu
 * @version 1.0 05/15/21
 */
public class RenderSetup {

    public static void init() {
        RenderTypeLookup.setRenderLayer(Registration.DDD_TELEPORTER_BLOCK.get(), RenderType.translucent());
    }
}
//========SOLI DEO GLORIA========//