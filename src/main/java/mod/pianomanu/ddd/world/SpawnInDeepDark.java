package mod.pianomanu.ddd.world;

import mod.pianomanu.ddd.DDDMain;
import mod.pianomanu.ddd.config.DDDConfig;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Will add description later...
 *
 * @author PianoManu
 * @version 1.0 05/17/21
 */
public class SpawnInDeepDark {
    private static IWorld world;

    @SubscribeEvent
    public void playerLogginIn(final PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        if (!world.players().contains(player)) {
            System.out.println("player is new");
            ServerWorld deepDark = player.server.getLevel(DDDMain.DEEP_DARK_DIMENSION);
            if (DDDConfig.SPAWN_IN_DEEP_DARK_DIMENSION && deepDark != null) {
                System.out.println("player spawns in deep dark");
                DDDTeleporter teleporter = new DDDTeleporter(DDDConfig.SPAWN_POS);
                teleporter.setOverworldTeleporterPos(player.blockPosition());
                player.setPos(DDDConfig.SPAWN_POS.getX(), DDDConfig.SPAWN_POS.getY(), DDDConfig.SPAWN_POS.getZ());
                player.changeDimension(deepDark, teleporter);
            }
        } else {
            System.out.println("player is not new");
        }
    }

    @SubscribeEvent
    public void playerLoadsWorld(final WorldEvent.Load event) {
        world = event.getWorld();
    }
}
//========SOLI DEO GLORIA========//