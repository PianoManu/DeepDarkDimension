package mod.pianomanu.ddd.world.darkness;

import mod.pianomanu.ddd.DDDMain;
import mod.pianomanu.ddd.config.DDDConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Will add description later...
 *
 * @author PianoManu
 * @version 1.3 05/21/21
 */
public class DarknessTickUtils {
    private final Map<PlayerEntity, Integer> players = new HashMap<>();

    @SubscribeEvent
    public void tick(final TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && !event.player.getCommandSenderWorld().isClientSide && event.player instanceof ServerPlayerEntity) {
            final ServerPlayerEntity player = (ServerPlayerEntity) event.player;
            if (player.level.dimension().equals(DDDMain.DEEP_DARK_DIMENSION) && DDDConfig.MAX_TIME_IN_DARKNESS_BEFORE_RECEIVING_DAMAGE.get() > -1) {
                int time = 0;
                try {
                    time = players.get(player);
                } catch (NullPointerException e) {
                    players.put(player, 0);
                }
                if (player.getBrightness() < 0.05 && time < DDDConfig.MAX_TIME_IN_DARKNESS_BEFORE_RECEIVING_DAMAGE.get()) {
                    time++;
                } else if (player.getBrightness() > 0.05 && time > 0) {
                    time--;
                }
                if (time == DDDConfig.MAX_TIME_IN_DARKNESS_BEFORE_RECEIVING_DAMAGE.get()) {
                    player.hurt(DarknessDamageSource.DAMAGE_BY_DARKNESS, DDDConfig.DAMAGE_PER_HIT_RECEIVED_BY_DARKNESS.get().floatValue());
                }
                players.put(player, time);
            }
        }
    }
}
//========SOLI DEO GLORIA========//