package mod.pianomanu.ddd.world;

import mod.pianomanu.ddd.DDDMain;
import mod.pianomanu.ddd.config.DDDConfig;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Will add description later...
 *
 * @author PianoManu
 * @version 1.1 05/18/21
 */
public class SpawnInDeepDark {
    private static IWorld world;

    @SubscribeEvent
    public void playerLogginIn(final PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        if (!world.players().contains(player)) {
            ServerWorld deepDark = player.server.getLevel(DDDMain.DEEP_DARK_DIMENSION);
            if (DDDConfig.PLAYER_SPAWNS_IN_DEEP_DARK_DIMENSION.get() && deepDark != null) {
                DDDTeleporter teleporter = new DDDTeleporter(new BlockPos(DDDConfig.SPAWN_POS_X.get(), DDDConfig.SPAWN_POS_Y.get(), DDDConfig.SPAWN_POS_Z.get()));
                teleporter.setOverworldTeleporterPos(player.blockPosition());
                player.setPos(DDDConfig.SPAWN_POS_X.get(), DDDConfig.SPAWN_POS_Y.get(), DDDConfig.SPAWN_POS_Z.get());
                player.changeDimension(deepDark, teleporter);
            }
        }
    }

    @SubscribeEvent
    public void playerLoadsWorld(final WorldEvent.Load event) {
        world = event.getWorld();
    }
}
//========SOLI DEO GLORIA========//