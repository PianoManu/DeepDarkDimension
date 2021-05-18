package mod.pianomanu.ddd.world.darkness;

import net.minecraft.util.DamageSource;

/**
 * Will add description later...
 *
 * @author PianoManu
 * @version 1.0 05/15/21
 */
public class DarknessDamageSource extends DamageSource {
    public static final DarknessDamageSource DAMAGE_BY_DARKNESS = (DarknessDamageSource) (new DarknessDamageSource("darkness")).bypassArmor();

    public DarknessDamageSource(String name) {
        super(name);
    }
}
//========SOLI DEO GLORIA========//