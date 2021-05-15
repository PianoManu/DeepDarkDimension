package mod.pianomanu.ddd.world.feature;

import com.mojang.serialization.Codec;
import mod.pianomanu.ddd.config.DDDConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.Random;

public class DeepDarkPillar extends Feature<OreFeatureConfig> {
    public DeepDarkPillar(Codec<OreFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean place(ISeedReader seedReader, ChunkGenerator chunkGenerator, Random rand, BlockPos pos, OreFeatureConfig config) {
        if (rand.nextFloat() < 0.8)
            return false;
        int pillarSocketDiameter = 8;
        int pillarSocketHeight = 6;
        int pillarSocketDiameterLarge = 10;
        int pillarConnectionDiameter = 6;
        int startXPosition = pos.getX();
        int startYPosition = DDDConfig.CAVE_LOWER_LIMIT;
        int startZPosition = pos.getZ();
        //this.setBlock(seedReader, new BlockPos(startXPosition, 80, startZPosition), determineBlockState(rand));
        for (int currentXPosition = startXPosition; currentXPosition <= startXPosition + pillarSocketDiameter; currentXPosition++) {
            for (int currentZPosition = startZPosition; currentZPosition <= startZPosition + pillarSocketDiameter; currentZPosition++) {
                boolean isCornerPosition = (currentXPosition == startXPosition && currentZPosition == startZPosition) || (currentXPosition == startXPosition + pillarSocketDiameter && currentZPosition == startZPosition) || (currentXPosition == startXPosition && currentZPosition == startZPosition + pillarSocketDiameter) || (currentXPosition == startXPosition + pillarSocketDiameter && currentZPosition == startZPosition + pillarSocketDiameter);
                boolean isNotACornerPosition = !isCornerPosition;
                boolean isInnerPosition = !(currentXPosition == startXPosition || currentXPosition == startXPosition + pillarSocketDiameter || currentZPosition == startZPosition || currentZPosition == startZPosition + pillarSocketDiameter);
                //boolean isNotACornerPosition = !(currentXPosition == startXPosition || currentXPosition == startXPosition + pillarSocketDiameter) && !(currentZPosition == startZPosition || currentZPosition == startZPosition + pillarSocketDiameter);
                //lower socket
                for (int currentYPosition = startYPosition + pillarSocketHeight; currentYPosition < startYPosition + 2 * pillarSocketHeight; currentYPosition++) {
                    if (isInnerPosition)
                        this.setBlock(seedReader, new BlockPos(currentXPosition, currentYPosition, currentZPosition), determineSimpleBlockState(rand));
                    else if (isNotACornerPosition)
                        this.setBlock(seedReader, new BlockPos(currentXPosition, currentYPosition, currentZPosition), determineBlockState(rand));
                }
                //upper socket
                for (int currentYPosition = DDDConfig.CAVE_UPPER_LIMIT - 2 * pillarSocketHeight; currentYPosition < DDDConfig.CAVE_UPPER_LIMIT - pillarSocketHeight; currentYPosition++) {
                    if (isInnerPosition)
                        this.setBlock(seedReader, new BlockPos(currentXPosition, currentYPosition, currentZPosition), determineSimpleBlockState(rand));
                    else if (isNotACornerPosition)
                        this.setBlock(seedReader, new BlockPos(currentXPosition, currentYPosition, currentZPosition), determineBlockState(rand));
                }
            }
        }
        for (int currentXPosition = startXPosition - 1; currentXPosition <= startXPosition + pillarSocketDiameterLarge - 1; currentXPosition++) {
            for (int currentZPosition = startZPosition - 1; currentZPosition <= startZPosition + pillarSocketDiameterLarge - 1; currentZPosition++) {
                boolean isCornerPosition = (currentXPosition == startXPosition - 1 && currentZPosition == startZPosition - 1) || (currentXPosition == startXPosition + pillarSocketDiameter + 1 && currentZPosition == startZPosition - 1) || (currentXPosition == startXPosition - 1 && currentZPosition == startZPosition + pillarSocketDiameter + 1) || (currentXPosition == startXPosition + pillarSocketDiameter + 1 && currentZPosition == startZPosition + pillarSocketDiameter + 1);
                boolean isNotACornerPosition = !isCornerPosition;
                boolean isInnerPosition = !(currentXPosition == startXPosition - 1 || currentXPosition == startXPosition + pillarSocketDiameter + 1 || currentZPosition == startZPosition - 1 || currentZPosition == startZPosition + pillarSocketDiameter + 1);
                //lower large socket
                for (int currentYPosition = startYPosition; currentYPosition < startYPosition + pillarSocketHeight; currentYPosition++) {
                    if (isInnerPosition)
                        this.setBlock(seedReader, new BlockPos(currentXPosition, currentYPosition, currentZPosition), determineSimpleBlockState(rand));
                    else if (isNotACornerPosition)
                        this.setBlock(seedReader, new BlockPos(currentXPosition, currentYPosition, currentZPosition), determineBlockState(rand));
                }
                //upper large socket
                for (int currentYPosition = DDDConfig.CAVE_UPPER_LIMIT - pillarSocketHeight; currentYPosition < DDDConfig.CAVE_UPPER_LIMIT; currentYPosition++) {
                    if (isInnerPosition)
                        this.setBlock(seedReader, new BlockPos(currentXPosition, currentYPosition, currentZPosition), determineSimpleBlockState(rand));
                    else if (isNotACornerPosition)
                        this.setBlock(seedReader, new BlockPos(currentXPosition, currentYPosition, currentZPosition), determineBlockState(rand));
                }
            }
        }
        //middle post
        for (int currentXPosition = startXPosition + 1; currentXPosition <= startXPosition + pillarConnectionDiameter + 1; currentXPosition++) {
            for (int currentZPosition = startZPosition + 1; currentZPosition <= startZPosition + pillarConnectionDiameter + 1; currentZPosition++) {
                for (int currentYPosition = startYPosition + 2 * pillarSocketHeight; currentYPosition < DDDConfig.CAVE_UPPER_LIMIT - 2 * pillarSocketHeight; currentYPosition++) {
                    boolean isCornerPosition = (currentXPosition == startXPosition + 1 && currentZPosition == startZPosition + 1) || (currentXPosition == startXPosition + pillarConnectionDiameter + 1 && currentZPosition == startZPosition + 1) || (currentXPosition == startXPosition + 1 && currentZPosition == startZPosition + pillarConnectionDiameter + 1) || (currentXPosition == startXPosition + pillarConnectionDiameter + 1 && currentZPosition == startZPosition + pillarConnectionDiameter + 1);
                    boolean isNotACornerPosition = !isCornerPosition;
                    boolean isInnerPosition = !(currentXPosition == startXPosition + 1 || currentXPosition == startXPosition + pillarSocketDiameter - 1 || currentZPosition == startZPosition + 1 || currentZPosition == startZPosition + pillarSocketDiameter - 1);
                    if (isInnerPosition)
                        this.setBlock(seedReader, new BlockPos(currentXPosition, currentYPosition, currentZPosition), determineSimpleBlockState(rand));
                    else if (isNotACornerPosition)
                        this.setBlock(seedReader, new BlockPos(currentXPosition, currentYPosition, currentZPosition), determineBlockState(rand));
                    else
                        this.setBlock(seedReader, new BlockPos(currentXPosition, currentYPosition, currentZPosition), determineWallBlockState(rand));
                }
            }
        }
        return true;
    }

    private BlockState determineBlockState(Random rand) {
        if (rand.nextFloat() < 0.4)
            return getRandomStairsBlockState(rand);
        if (rand.nextFloat() < 0.1) {
            if (rand.nextFloat() < 0.5)
                return Blocks.COBBLESTONE_SLAB.defaultBlockState();
            return Blocks.COBBLESTONE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP);
        }
        if (rand.nextFloat() < 0.025)
            return Blocks.CAVE_AIR.defaultBlockState();
        if (rand.nextFloat() < 0.1)
            return Blocks.MOSSY_COBBLESTONE.defaultBlockState();
        return Blocks.COBBLESTONE.defaultBlockState();
    }

    private BlockState determineSimpleBlockState(Random rand) {
        if (rand.nextFloat() < 0.2)
            return Blocks.MOSSY_COBBLESTONE.defaultBlockState();
        return Blocks.COBBLESTONE.defaultBlockState();
    }

    private BlockState getRandomStairsBlockState(Random rand) {
        BlockState stairsState = Blocks.COBBLESTONE_STAIRS.defaultBlockState();
        if (rand.nextFloat() < 0.5) {
            stairsState.setValue(StairsBlock.HALF, Half.BOTTOM);
        } else {
            stairsState.setValue(StairsBlock.HALF, Half.TOP);
        }
        if (rand.nextFloat() < 0.25)
            return stairsState.setValue(StairsBlock.FACING, Direction.NORTH);
        else if (rand.nextFloat() < 0.25)
            return stairsState.setValue(StairsBlock.FACING, Direction.EAST);
        else if (rand.nextFloat() < 0.25)
            return stairsState.setValue(StairsBlock.FACING, Direction.SOUTH);
        else
            return stairsState.setValue(StairsBlock.FACING, Direction.WEST);
    }

    private BlockState determineWallBlockState(Random rand) {
        if (rand.nextFloat() < 0.2)
            return Blocks.MOSSY_COBBLESTONE_WALL.defaultBlockState();
        return Blocks.COBBLESTONE_WALL.defaultBlockState();
    }
}
