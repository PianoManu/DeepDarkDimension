package mod.pianomanu.ddd.block;

import mod.pianomanu.ddd.DDDMain;
import mod.pianomanu.ddd.config.DDDConfig;
import mod.pianomanu.ddd.tileentity.TeleporterBlockTileEntity;
import mod.pianomanu.ddd.world.DDDTeleporter;
import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Will add description later...
 *
 * @author PianoManu
 * @version 1.2 05/18/21
 */
@SuppressWarnings("deprecation")
public class DDDTeleporterBlock extends AbstractGlassBlock implements ITileEntityProvider {
    private static final Logger LOGGER = LogManager.getLogger();

    public DDDTeleporterBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(@Nullable IBlockReader blockReader) {
        return new TeleporterBlockTileEntity();
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public ActionResultType use(@Nullable BlockState state, @Nullable World world, @Nullable BlockPos pos, @Nullable PlayerEntity player, @Nullable Hand hand, @Nullable BlockRayTraceResult rayTraceResult) {
        if (player instanceof ServerPlayerEntity) {
            if (teleportPlayer((ServerPlayerEntity) player, new BlockPos(DDDConfig.SPAWN_POS_X.get(), DDDConfig.SPAWN_POS_Y.get(), DDDConfig.SPAWN_POS_Z.get())))
                return ActionResultType.CONSUME;
        }
        return ActionResultType.PASS;
    }

    public boolean teleportPlayer(ServerPlayerEntity player, BlockPos pos) {
        //player inside of vehicle cannot teleport
        if (player.isVehicle() || player.getVehicle() != null) {
            return false;
        }

        if (player.level.dimension().equals(DDDMain.DEEP_DARK_DIMENSION)) {
            ServerWorld destiny = player.server.getLevel(ServerWorld.OVERWORLD);
            if (destiny != null) {
                player.setPos(pos.getX(), pos.getY(), pos.getZ());
                player.changeDimension(destiny, new DDDTeleporter(DDDTeleporter.getOverworldTeleporterPos()));
            } else {
                LOGGER.error("Could not find Overworld...");
                player.sendMessage(new TranslationTextComponent("Could not transfer to Overworld"), ChatType.SYSTEM, player.getUUID());
            }
            return true;
        }
        if (!player.level.dimension().equals(DDDMain.DEEP_DARK_DIMENSION)) {
            ServerWorld destiny = player.server.getLevel(DDDMain.DEEP_DARK_DIMENSION);
            if (destiny != null) {
                DDDTeleporter teleporter = new DDDTeleporter(new BlockPos(DDDConfig.SPAWN_POS_X.get(), DDDConfig.SPAWN_POS_Y.get(), DDDConfig.SPAWN_POS_Z.get()));
                teleporter.setOverworldTeleporterPos(player.blockPosition());
                player.setPos(DDDConfig.SPAWN_POS_X.get(), DDDConfig.SPAWN_POS_Y.get(), DDDConfig.SPAWN_POS_Z.get());
                player.changeDimension(destiny, teleporter);
                if (DDDConfig.PLAYER_SPAWNS_IN_DEEP_DARK_DIMENSION.get())
                    player.setRespawnPosition(player.level.dimension(), new BlockPos(DDDConfig.SPAWN_POS_X.get(), DDDConfig.SPAWN_POS_Y.get(), DDDConfig.SPAWN_POS_Z.get()), 0, true, true);
            } else {
                LOGGER.error("Could not find Deep Dark Dimension...");
                player.sendMessage(new TranslationTextComponent("Could not transfer to Deep Dark Dimension"), ChatType.SYSTEM, player.getUUID());
            }
        }
        return true;
    }

    @Override
    public void appendHoverText(@Nullable ItemStack itemStack, @Nullable IBlockReader blockReader, @Nullable List<ITextComponent> textComponents, @Nullable ITooltipFlag toolTipFlag) {
        if (textComponents == null) {
            textComponents = new ArrayList<>();
        }
        textComponents.add(new TranslationTextComponent("block.ddd.teleporter.description"));
        if (itemStack != null && toolTipFlag != null) {
            super.appendHoverText(itemStack, blockReader, textComponents, toolTipFlag);
        }
    }
}
//========SOLI DEO GLORIA========//