package mod.pianomanu.ddd.world;

import mod.pianomanu.ddd.DDDMain;
import mod.pianomanu.ddd.config.DDDConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Function;

/**
 * Will add description later...
 *
 * @author PianoManu
 * @version 1.3 05/21/21
 */
public class DDDTeleporter implements ITeleporter {
    private static final Logger LOGGER = LogManager.getLogger();

    private static BlockPos overworldTeleporterPos;

    public DDDTeleporter(BlockPos overworldTeleporterPos) {
        DDDTeleporter.overworldTeleporterPos = overworldTeleporterPos;
    }

    @Override
    public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        Entity e = repositionEntity.apply(false);
        if (e instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) e;
            BlockPos teleporterPos;
            if (destWorld.dimension().equals(DDDMain.DEEP_DARK_DIMENSION))
                teleporterPos = new BlockPos(DDDConfig.SPAWN_POS_X.get(), DDDConfig.SPAWN_POS_Y.get(), DDDConfig.SPAWN_POS_Z.get());
            else
                teleporterPos = overworldTeleporterPos;
            if (teleporterPos == null) {
                player.setRespawnPosition(World.OVERWORLD, null, 0, false, false);
                teleporterPos = player.getRespawnPosition();
            }
            if (teleporterPos != null) {
                player.teleportTo(teleporterPos.getX(), teleporterPos.getY(), teleporterPos.getZ());
            } else {
                LOGGER.error("Could not teleport - no position was found");
            }
        }
        return e;
    }

    public void setOverworldTeleporterPos(BlockPos newOverworldTeleporterPos) {
        overworldTeleporterPos = newOverworldTeleporterPos;
    }

    public static BlockPos getOverworldTeleporterPos() {
        return overworldTeleporterPos;
    }
}
//========SOLI DEO GLORIA========//