package mod.pianomanu.ddd.world.darkness;

import mod.pianomanu.ddd.DDDMain;
import mod.pianomanu.ddd.config.DDDConfig;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Will add description later...
 *
 * @author PianoManu
 * @version 1.3 05/21/21
 */
public class DarknessTickUtils {
    private static final DataParameter<Integer> TIME_IN_DARKNESS = EntityDataManager.defineId(ServerPlayerEntity.class, DataSerializers.INT);

    @SubscribeEvent
    public void tick(final TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && !event.player.getCommandSenderWorld().isClientSide && event.player instanceof ServerPlayerEntity) {
            final ServerPlayerEntity player = (ServerPlayerEntity) event.player;
            if (player.level.dimension().equals(DDDMain.DEEP_DARK_DIMENSION) && DDDConfig.MAX_TIME_IN_DARKNESS_BEFORE_RECEIVING_DAMAGE.get() > -1) {
                int time = 0;
                try {
                    time = player.getEntityData().get(TIME_IN_DARKNESS);
                } catch (NullPointerException e) {
                    player.getEntityData().define(TIME_IN_DARKNESS, 0);
                    player.getEntityData().set(TIME_IN_DARKNESS, time);
                }
                if (player.getBrightness() < 0.05 && time < DDDConfig.MAX_TIME_IN_DARKNESS_BEFORE_RECEIVING_DAMAGE.get()) {
                    time++;
                } else if (player.getBrightness() > 0.05 && time > 0) {
                    time--;
                }
                if (time == DDDConfig.MAX_TIME_IN_DARKNESS_BEFORE_RECEIVING_DAMAGE.get()) {
                    player.hurt(DarknessDamageSource.DAMAGE_BY_DARKNESS, DDDConfig.DAMAGE_PER_HIT_RECEIVED_BY_DARKNESS.get().floatValue());
                }
                player.getEntityData().set(TIME_IN_DARKNESS, time);
            }
        }
    }
}
//========SOLI DEO GLORIA========//