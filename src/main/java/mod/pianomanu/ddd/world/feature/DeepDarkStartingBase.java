package mod.pianomanu.ddd.world.feature;

import com.mojang.serialization.Codec;
import mod.pianomanu.ddd.config.DDDConfig;
import mod.pianomanu.ddd.init.Registration;
import net.minecraft.block.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.SlabType;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.Random;

public class DeepDarkStartingBase extends Feature<NoFeatureConfig> {

    private static final int START_X = DDDConfig.SPAWN_POS.getX() - 4;
    private static final int START_Z = DDDConfig.SPAWN_POS.getZ() - 4;
    private static final int END_X = DDDConfig.SPAWN_POS.getX() + 4;
    private static final int END_Z = DDDConfig.SPAWN_POS.getZ() + 4;
    private static final int START_Y = DDDConfig.SPAWN_POS.getY() - 5;
    private static final int MIDDLE_Y = DDDConfig.SPAWN_POS.getY() - 1;
    private static final int END_Y = DDDConfig.SPAWN_POS.getY() + 3;
    /*private static final int START_X = DDDConfig.SPAWN_POS.getX() - 3;
    private static final int START_Z = DDDConfig.SPAWN_POS.getZ() - 3;
    private static final int END_X = DDDConfig.SPAWN_POS.getX() + 3;
    private static final int END_Z = DDDConfig.SPAWN_POS.getZ() + 3;
    private static final int START_Y = DDDConfig.SPAWN_POS.getY() - 5;
    private static final int MIDDLE_Y = DDDConfig.SPAWN_POS.getY() - 1;
    private static final int END_Y = DDDConfig.SPAWN_POS.getY() + 3;*/

    public DeepDarkStartingBase(Codec<NoFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean place(@Nullable ISeedReader seedReader, @Nullable ChunkGenerator generator, @Nullable Random rand, @Nullable BlockPos pos, @Nullable NoFeatureConfig config) {
        if (pos != null) {
            boolean isNearSpawn = pos.getX() < 32 && pos.getX() > -32 && pos.getZ() < 32 && pos.getZ() > -32;
            if (seedReader != null && rand != null && isNearSpawn) {
                try {
                    this.setBlock(seedReader, new BlockPos(DDDConfig.SPAWN_POS.getX(), DDDConfig.SPAWN_POS.getY(), DDDConfig.SPAWN_POS.getZ() + 1), Registration.DDD_TELEPORTER_BLOCK.get().defaultBlockState());
                    if (!DDDConfig.INCLUDE_TELEPORTER_BLOCK)
                        this.setBlock(seedReader, new BlockPos(DDDConfig.SPAWN_POS.getX(), DDDConfig.SPAWN_POS.getY(), DDDConfig.SPAWN_POS.getZ() + 1), Blocks.CAVE_AIR.defaultBlockState());
                } catch (RuntimeException e) {
                    return false;
                }
                placeBaseFrame(seedReader, rand);
                placeLightSources(seedReader);
                placeLadder(seedReader);
                placeExits(seedReader);
                placeRoof(seedReader, rand);
                //if (DDDConfig.INCLUDE_STARTER_CHEST) //TODO not working
                //placeStarterChest(seedReader);
                return true;
            }
        }
        return false;
    }

    /*private double calcDistance(BlockPos pos1, BlockPos pos2) {
        int distX = Math.abs(pos1.getX() - pos2.getX());
        int distY = Math.abs(pos1.getY() - pos2.getY());
        int distZ = Math.abs(pos1.getZ() - pos2.getZ());
        return Math.sqrt(distX ^ 2 + distY ^ 2 + distZ ^ 2);
    }*/

    private void placeBaseFrame(ISeedReader seedReader, Random rand) {
        for (int x = START_X; x <= END_X; x++) {
            for (int z = START_Z; z <= END_Z; z++) {
                try {
                    this.setBlock(seedReader, new BlockPos(x, START_Y, z), determineStoneBrickState(rand));
                    this.setBlock(seedReader, new BlockPos(x, MIDDLE_Y, z), determineStoneBrickState(rand));
                    boolean isCornerBlock = (x == START_X && z == START_Z) || (x == END_X && z == START_Z) || (x == START_X && z == END_Z) || (x == END_X && z == END_Z);
                    boolean isCornerFrameBlock = x == START_X || z == START_Z || x == END_X || z == END_Z;

                    for (int y = START_Y; y <= END_Y; y++) {
                        if (isCornerFrameBlock) {
                            //this.setBlock(seedReader, new BlockPos(x, END_Y, z), determineStoneBrickState(rand));
                            placeWalls(seedReader, new BlockPos(x, y, z));
                        }
                        if (isCornerBlock) {
                            if (y == START_Y || y == MIDDLE_Y || y == END_Y)
                                this.setBlock(seedReader, new BlockPos(x, y, z), Blocks.CHISELED_STONE_BRICKS.defaultBlockState());
                            else
                                this.setBlock(seedReader, new BlockPos(x, y, z), determineStoneBrickState(rand));
                            //placeWalls(seedReader, new BlockPos(x,y,z));
                        }
                    }
                } catch (RuntimeException ignored) {

                }
            }
        }
    }

    private void placeLightSources(ISeedReader seedReader) {
        try {
            //upper floor
            //north side
            this.setBlock(seedReader, new BlockPos(START_X + 2, MIDDLE_Y + 2, START_Z + 1), Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.SOUTH));
            this.setBlock(seedReader, new BlockPos(END_X - 2, MIDDLE_Y + 2, START_Z + 1), Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.SOUTH));
            //south sNOR
            this.setBlock(seedReader, new BlockPos(START_X + 2, MIDDLE_Y + 2, END_Z - 1), Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.NORTH));
            this.setBlock(seedReader, new BlockPos(END_X - 2, MIDDLE_Y + 2, END_Z - 1), Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.NORTH));
            //west side
            this.setBlock(seedReader, new BlockPos(START_X + 1, MIDDLE_Y + 2, START_Z + 2), Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.EAST));
            this.setBlock(seedReader, new BlockPos(START_X + 1, MIDDLE_Y + 2, END_Z - 2), Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.EAST));
            //east side
            this.setBlock(seedReader, new BlockPos(END_X - 1, MIDDLE_Y + 2, START_Z + 2), Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.WEST));
            this.setBlock(seedReader, new BlockPos(END_X - 1, MIDDLE_Y + 2, END_Z - 2), Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.WEST));
            //lower floor
            this.setBlock(seedReader, new BlockPos(START_X + 2, MIDDLE_Y - 1, START_Z + 2), Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, true));
            this.setBlock(seedReader, new BlockPos(START_X + 2, MIDDLE_Y - 1, END_Z - 2), Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, true));
            this.setBlock(seedReader, new BlockPos(END_X - 2, MIDDLE_Y - 1, START_Z + 2), Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, true));
            this.setBlock(seedReader, new BlockPos(END_X - 2, MIDDLE_Y - 1, END_Z - 2), Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, true));
        } catch (RuntimeException ignored) {

        }
    }

    private void placeWalls(ISeedReader seedReader, BlockPos pos) {
        boolean isInMiddleOfWall = pos.getX() >= DDDConfig.SPAWN_POS.getX() - 1 && pos.getX() <= DDDConfig.SPAWN_POS.getX() + 1 || pos.getZ() >= DDDConfig.SPAWN_POS.getZ() - 1 && pos.getZ() <= DDDConfig.SPAWN_POS.getZ() + 1;
        if (isInMiddleOfWall && pos.getY() == MIDDLE_Y + 2) {
            BlockState ironBars = Blocks.IRON_BARS.defaultBlockState();
            /*if (pos.getX() == START_X || pos.getX() == END_X) {
                for (Direction d : Direction.values()) {
                    BlockPos movedPos = pos.mutable().move(d);
                    ironBars.setValue(FourWayBlock.WEST, true).updateNeighbourShapes(seedReader, movedPos, 3);
                    ironBars.setValue(FourWayBlock.EAST, true).updateNeighbourShapes(seedReader, movedPos, 3);
                }
            }
            if (pos.getZ() == START_Z || pos.getZ() == END_Z) {
                for (Direction d : Direction.values()) {
                    BlockPos movedPos = pos.mutable().move(d);
                    ironBars.setValue(FourWayBlock.NORTH, true).updateNeighbourShapes(seedReader, movedPos, 3);
                    ironBars.setValue(FourWayBlock.SOUTH, true).updateNeighbourShapes(seedReader, movedPos, 3);
                }
            }*/
            this.setBlock(seedReader, pos, ironBars);
            if (pos.getZ() == START_Z || pos.getZ() == END_Z) {
                BlockPos movedPosWest = pos.mutable().move(Direction.WEST);
                BlockPos movedPosEast = pos.mutable().move(Direction.EAST);
                ironBars.setValue(FourWayBlock.WEST, true).updateNeighbourShapes(seedReader, movedPosWest, 3);
                ironBars.setValue(FourWayBlock.EAST, true).updateNeighbourShapes(seedReader, movedPosEast, 3);
            }
            if (pos.getX() == START_X || pos.getX() == END_X) {
                BlockPos movedPosNorth = pos.mutable().move(Direction.NORTH);
                BlockPos movedPosSouth = pos.mutable().move(Direction.SOUTH);
                ironBars.setValue(FourWayBlock.NORTH, true).updateNeighbourShapes(seedReader, movedPosNorth, 3);
                ironBars.setValue(FourWayBlock.SOUTH, true).updateNeighbourShapes(seedReader, movedPosSouth, 3);
            }
        } else
            this.setBlock(seedReader, pos, Blocks.STONE.defaultBlockState());
    }

    private void placeLadder(ISeedReader seedReader) {
        this.setBlock(seedReader, new BlockPos(DDDConfig.SPAWN_POS.getX(), MIDDLE_Y, START_Z + 1), Blocks.DARK_OAK_TRAPDOOR.defaultBlockState().setValue(TrapDoorBlock.FACING, Direction.SOUTH).setValue(TrapDoorBlock.HALF, Half.TOP));
        this.setBlock(seedReader, new BlockPos(DDDConfig.SPAWN_POS.getX(), MIDDLE_Y - 1, START_Z + 1), Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.SOUTH));
        this.setBlock(seedReader, new BlockPos(DDDConfig.SPAWN_POS.getX(), MIDDLE_Y - 2, START_Z + 1), Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.SOUTH));
        this.setBlock(seedReader, new BlockPos(DDDConfig.SPAWN_POS.getX(), MIDDLE_Y - 3, START_Z + 1), Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.SOUTH));
    }

    private void placeExits(ISeedReader seedReader) {
        //two iron doors with pressure plate
        //west
        this.setBlock(seedReader, new BlockPos(START_X, START_Y + 1, DDDConfig.SPAWN_POS.getZ()), Blocks.IRON_DOOR.defaultBlockState().setValue(DoorBlock.FACING, Direction.EAST).setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER));
        this.setBlock(seedReader, new BlockPos(START_X, START_Y + 2, DDDConfig.SPAWN_POS.getZ()), Blocks.IRON_DOOR.defaultBlockState().setValue(DoorBlock.FACING, Direction.EAST).setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER));
        this.setBlock(seedReader, new BlockPos(START_X - 1, START_Y + 1, DDDConfig.SPAWN_POS.getZ()), Blocks.STONE_PRESSURE_PLATE.defaultBlockState());
        this.setBlock(seedReader, new BlockPos(START_X + 1, START_Y + 1, DDDConfig.SPAWN_POS.getZ()), Blocks.STONE_PRESSURE_PLATE.defaultBlockState());
        this.setBlock(seedReader, new BlockPos(START_X - 1, START_Y + 2, DDDConfig.SPAWN_POS.getZ() - 1), Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.WEST));
        this.setBlock(seedReader, new BlockPos(START_X - 1, START_Y + 2, DDDConfig.SPAWN_POS.getZ() + 1), Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.WEST));
        //east
        this.setBlock(seedReader, new BlockPos(END_X, START_Y + 1, DDDConfig.SPAWN_POS.getZ()), Blocks.IRON_DOOR.defaultBlockState().setValue(DoorBlock.FACING, Direction.WEST).setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER));
        this.setBlock(seedReader, new BlockPos(END_X, START_Y + 2, DDDConfig.SPAWN_POS.getZ()), Blocks.IRON_DOOR.defaultBlockState().setValue(DoorBlock.FACING, Direction.WEST).setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER));
        this.setBlock(seedReader, new BlockPos(END_X - 1, START_Y + 1, DDDConfig.SPAWN_POS.getZ()), Blocks.STONE_PRESSURE_PLATE.defaultBlockState());
        this.setBlock(seedReader, new BlockPos(END_X + 1, START_Y + 1, DDDConfig.SPAWN_POS.getZ()), Blocks.STONE_PRESSURE_PLATE.defaultBlockState());
        this.setBlock(seedReader, new BlockPos(END_X + 1, START_Y + 2, DDDConfig.SPAWN_POS.getZ() - 1), Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.EAST));
        this.setBlock(seedReader, new BlockPos(END_X + 1, START_Y + 2, DDDConfig.SPAWN_POS.getZ() + 1), Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.EAST));
    }

    private void placeRoof(ISeedReader seedReader, Random rand) {
        for (int x = START_X - 1; x <= END_X + 1; x++) {
            for (int z = START_Z - 1; z <= END_Z + 1; z++) {
                boolean isOuterRectangle = (x == START_X - 1 || x == END_X + 1 || z == START_Z - 1 || z == END_Z + 1);
                boolean isOuterRectangle2 = (x == START_X || x == END_X || z == START_Z || z == END_Z);
                boolean isFrame = (x == START_X + 1 && z == START_Z + 1 || x == END_X - 1 && z == START_Z + 1 || x== START_X + 1 && z == END_Z - 1 || x == END_X - 1 && z == END_Z - 1);
                boolean isMiddleRectangle = (x == START_X + 1 || x == END_X - 1 || z == START_Z + 1 || z == END_Z - 1);
                boolean isMiddleRectangle2 = (x == START_X + 2 || x == END_X - 2 || z == START_Z + 2 || z == END_Z - 2);
                boolean isInnerRectangle = (x == START_X + 3 || x == END_X - 3 || z == START_Z + 3 || z == END_Z - 3);
                boolean isInnerRectangle2 = (x == START_X + 4 || x == END_X - 4 || z == START_Z + 4 || z == END_Z - 4);
                if (isOuterRectangle)
                    this.setBlock(seedReader, new BlockPos(x,END_Y,z),determineStoneBrickSlabState(rand).setValue(SlabBlock.TYPE, SlabType.TOP));
                if (isOuterRectangle2)
                    this.setBlock(seedReader, new BlockPos(x,END_Y+1,z),determineStoneBrickSlabState(rand).setValue(SlabBlock.TYPE, SlabType.BOTTOM));
                if (isMiddleRectangle)
                    //this.setBlock(seedReader, new BlockPos(x,END_Y+1,z),determineStoneBrickSlabState(rand).setValue(SlabBlock.TYPE, SlabType.TOP));
                    this.setBlock(seedReader, new BlockPos(x,END_Y+1,z),determineStoneBrickState(rand));
                if (isMiddleRectangle2) {
                    this.setBlock(seedReader, new BlockPos(x, END_Y + 1, z), determineStoneBrickSlabState(rand).setValue(SlabBlock.TYPE, SlabType.TOP));
                    this.setBlock(seedReader, new BlockPos(x, END_Y + 2, z), determineStoneBrickSlabState(rand).setValue(SlabBlock.TYPE, SlabType.BOTTOM));
                }
                if (isInnerRectangle)
                    //this.setBlock(seedReader, new BlockPos(x,END_Y+2,z),determineStoneBrickSlabState(rand).setValue(SlabBlock.TYPE, SlabType.TOP));
                    this.setBlock(seedReader, new BlockPos(x,END_Y+2,z),determineStoneBrickState(rand));
                if (isInnerRectangle2) {
                    this.setBlock(seedReader, new BlockPos(x, END_Y + 2, z), determineStoneBrickSlabState(rand).setValue(SlabBlock.TYPE, SlabType.TOP));
                    this.setBlock(seedReader, new BlockPos(x, END_Y + 3, z), determineStoneBrickSlabState(rand).setValue(SlabBlock.TYPE, SlabType.BOTTOM));
                }
                if (isFrame)
                    this.setBlock(seedReader, new BlockPos(x,END_Y+1,z),determineStoneBrickState(rand));
            }
        }
    }

    private void placeStarterChest(ISeedReader seedReader) {
        BlockState chest = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        if (chest.hasTileEntity()) {
            ChestTileEntity c = (ChestTileEntity) chest.createTileEntity(seedReader);
            chest.getBlock().createTileEntity(chest, seedReader);
            if (c != null) {
                c.clearCache();
                c.setItem(0, new ItemStack(Items.OAK_SAPLING, 16));
                c.setItem(1, new ItemStack(Items.TORCH, 16));
                c.setItem(2, new ItemStack(Items.GRASS_BLOCK, 4));
                c.setItem(3, new ItemStack(Items.WHEAT_SEEDS, 4));
                c.setItem(4, new ItemStack(Items.CARROT, 4));
                c.setItem(5, new ItemStack(Items.POTATO, 4));
                c.setItem(6, new ItemStack(Items.SUGAR_CANE, 4));
                c.setItem(7, new ItemStack(Items.CACTUS, 4));
                c.setItem(8, new ItemStack(Items.BAMBOO, 4));
                c.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
                this.setBlock(seedReader, new BlockPos(START_X + 1, MIDDLE_Y + 1, START_Z + 1), chest);
            } else {
                System.out.println("dgjndc gnjxdjhnx");
            }
        }
    }

    private static BlockState determineStoneBrickState(Random rand) {
        if (rand.nextFloat() < 0.2)
            return Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
        if (rand.nextFloat() < 0.25)
            return Blocks.CRACKED_STONE_BRICKS.defaultBlockState();
        return Blocks.STONE_BRICKS.defaultBlockState();
    }

    private static BlockState determineStoneBrickSlabState(Random rand) {
        if (rand.nextFloat() < 0.25)
            return Blocks.MOSSY_STONE_BRICK_SLAB.defaultBlockState();
        return Blocks.STONE_BRICK_SLAB.defaultBlockState();
    }
}
