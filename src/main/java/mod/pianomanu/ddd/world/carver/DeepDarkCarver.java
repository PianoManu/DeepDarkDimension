package mod.pianomanu.ddd.world.carver;

import com.mojang.serialization.Codec;
import mod.pianomanu.ddd.config.DDDConfig;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;

/**
 * Will add description later...
 *
 * @author PianoManu
 * @version 1.0 05/15/21
 */
public class DeepDarkCarver extends WorldCarver<ProbabilityConfig> {
    private static final Logger LOGGER = LogManager.getLogger();

    public DeepDarkCarver(Codec<ProbabilityConfig> probabilityConfig) {
        super(probabilityConfig, 256);
    }

    @Override
    public boolean carve(IChunk chunk, @Nullable Function<BlockPos, Biome> biomePos, @Nullable Random rand, int seaLevel, int chunkXOffset, int chunkZOffset, int chunkX, int chunkZ, @Nullable BitSet carvingMask, @Nullable ProbabilityConfig probabilityConfig) {
        int currentXValueMin = chunk.getPos().getWorldPosition().getX();
        int currentXValueMax = chunk.getPos().getWorldPosition().getX() + 16;
        int currentZValueMin = chunk.getPos().getWorldPosition().getZ();
        int currentZValueMax = chunk.getPos().getWorldPosition().getZ() + 16;
        int yStart = DDDConfig.CAVE_LOWER_LIMIT;
        int yEnd = DDDConfig.CAVE_UPPER_LIMIT;
        for (int x = currentXValueMin; x < currentXValueMax; ++x) {
            for (int z = currentZValueMin; z < currentZValueMax; ++z) {
                for (int y = yStart; y < yEnd; ++y) {
                    this.carveBlock(chunk, biomePos, carvingMask, rand, new BlockPos.Mutable(x, y, z), yStart, yEnd);
                }
            }
        }
        return true;
    }

    @Override
    public boolean isStartChunk(@Nullable Random rand, int p_212868_2_, int p_212868_3_, @Nullable ProbabilityConfig p_212868_4_) {
        return true;
    }

    @Override
    protected boolean skip(double p_222708_1_, double p_222708_3_, double p_222708_5_, int p_222708_7_) {
        return false;
    }

    protected void carveBlock(IChunk chunk, Function<BlockPos, Biome> biomePos, BitSet carvingMask, Random rand, BlockPos.Mutable pos, int yStart, int yEnd) {
        //top and bottom layers
        if (pos.getY() == yStart || pos.getY() == yEnd) {
            if (rand.nextFloat() < 0.2) {
                chunk.setBlockState(pos, Blocks.COBBLESTONE.defaultBlockState(), false);
            } else {
                chunk.setBlockState(pos, Blocks.STONE.defaultBlockState(), false);
            }
        }
        //stalactites and stalagmites
        else if (pos.getY() == yStart + 1 || pos.getY() == yEnd - 1) {
            if (rand.nextFloat() < 0.7) {
                chunk.setBlockState(pos, Blocks.COBBLESTONE.defaultBlockState(), false);
            } else if (rand.nextFloat() < 0.75) {
                chunk.setBlockState(pos, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), false);
            } else {
                chunk.setBlockState(pos, Blocks.STONE.defaultBlockState(), false);
            }
        } else if (pos.getY() == yStart + 2) {
            BlockPos downPos = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());
            if (rand.nextFloat() < 0.2 && !chunk.getBlockState(downPos).getBlock().equals(Blocks.CAVE_AIR)) {
                chunk.setBlockState(pos, Blocks.COBBLESTONE.defaultBlockState(), false);
            } else
                chunk.setBlockState(pos, Blocks.CAVE_AIR.defaultBlockState(), false);
        } else if (pos.getY() == yEnd - 2) {
            BlockPos downPos = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());
            if (rand.nextFloat() < 0.2 || !chunk.getBlockState(downPos).getBlock().equals(Blocks.CAVE_AIR)) {
                chunk.setBlockState(pos, Blocks.COBBLESTONE.defaultBlockState(), false);
            } else
                chunk.setBlockState(pos, Blocks.CAVE_AIR.defaultBlockState(), false);
        } else if (pos.getY() == yStart + 3) {
            BlockPos downPos = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());
            if (rand.nextFloat() < 0.1 && chunk.getBlockState(downPos).getBlock().equals(Blocks.COBBLESTONE)) {
                chunk.setBlockState(pos, Blocks.COBBLESTONE.defaultBlockState(), false);
            } else
                chunk.setBlockState(pos, Blocks.CAVE_AIR.defaultBlockState(), false);
        } else if (pos.getY() == yEnd - 3) {
            BlockPos downPos = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());
            if (rand.nextFloat() < 0.1 || chunk.getBlockState(downPos).getBlock().equals(Blocks.COBBLESTONE)) {
                chunk.setBlockState(pos, Blocks.COBBLESTONE.defaultBlockState(), false);
            } else
                chunk.setBlockState(pos, Blocks.CAVE_AIR.defaultBlockState(), false);
        } else if (pos.getY() == yEnd - 4) {
            BlockPos downPos = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());
            if (rand.nextFloat() < 0.05 || chunk.getBlockState(downPos).getBlock().equals(Blocks.COBBLESTONE)) {
                chunk.setBlockState(pos, Blocks.COBBLESTONE.defaultBlockState(), false);
            } else
                chunk.setBlockState(pos, Blocks.CAVE_AIR.defaultBlockState(), false);
        } else if (pos.getY() == yEnd - 5) {
            BlockPos downPos = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());
            if (rand.nextFloat() < 0.02 || chunk.getBlockState(downPos).getBlock().equals(Blocks.COBBLESTONE)) {
                chunk.setBlockState(pos, Blocks.COBBLESTONE.defaultBlockState(), false);
            } else
                chunk.setBlockState(pos, Blocks.CAVE_AIR.defaultBlockState(), false);
        } else if (pos.getY() == yEnd - 6) {
            BlockPos downPos = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());
            if (rand.nextFloat() < 0.01 || chunk.getBlockState(downPos).getBlock().equals(Blocks.COBBLESTONE)) {
                chunk.setBlockState(pos, Blocks.COBBLESTONE.defaultBlockState(), false);
            } else
                chunk.setBlockState(pos, Blocks.CAVE_AIR.defaultBlockState(), false);
        } else
            //carve large cave
            chunk.setBlockState(pos, Blocks.CAVE_AIR.defaultBlockState(), false);
    }
}
//========SOLI DEO GLORIA========//