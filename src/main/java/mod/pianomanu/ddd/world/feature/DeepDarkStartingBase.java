package mod.pianomanu.ddd.world.feature;

import com.mojang.serialization.Codec;
import mod.pianomanu.ddd.DDDMain;
import mod.pianomanu.ddd.config.DDDConfig;
import mod.pianomanu.ddd.init.Registration;
import net.minecraft.block.*;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.SlabType;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Will add description later...
 *
 * @author PianoManu
 * @version 1.3 05/20/21
 */
public class DeepDarkStartingBase extends Feature<NoFeatureConfig> {

    private static final int START_X = DDDConfig.SPAWN_POS_X.get() - 4;
    private static final int START_Z = DDDConfig.SPAWN_POS_Z.get() - 4;
    private static final int END_X = DDDConfig.SPAWN_POS_X.get() + 4;
    private static final int END_Z = DDDConfig.SPAWN_POS_Z.get() + 4;
    private static final int START_Y = DDDConfig.SPAWN_POS_Y.get() - 5;
    private static final int MIDDLE_Y = DDDConfig.SPAWN_POS_Y.get() - 1;
    private static final int END_Y = DDDConfig.SPAWN_POS_Y.get() + 3;

    private final ResourceLocation STARTER_CHEST_LOOT_TABLE = getStarterChestLootTable(DDDConfig.STARTER_CHEST_DIFFICULTY.get());

    private ResourceLocation getStarterChestLootTable(int difficulty) {
        switch (difficulty) {
            case 1:
                System.out.println(1);
                return new ResourceLocation(DDDMain.MOD_ID, "chests/starter_chest_normal");
            case 2:
                System.out.println(2);
                return new ResourceLocation(DDDMain.MOD_ID, "chests/starter_chest_hard");
            default:
                System.out.println(difficulty);
                return new ResourceLocation(DDDMain.MOD_ID, "chests/starter_chest_easy");
        }
    }

    public DeepDarkStartingBase(Codec<NoFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean place(@Nullable ISeedReader seedReader, @Nullable ChunkGenerator generator, @Nullable Random rand, @Nullable BlockPos pos, @Nullable NoFeatureConfig config) {
        if (pos != null && DDDConfig.CREATE_SPAWN_BASE.get()) {
            boolean isNearSpawn = pos.getX() < 32 && pos.getX() > -32 && pos.getZ() < 32 && pos.getZ() > -32;
            if (seedReader != null && rand != null && isNearSpawn) {
                try {
                    this.setBlock(seedReader, new BlockPos(DDDConfig.SPAWN_POS_X.get(), DDDConfig.SPAWN_POS_Y.get(), DDDConfig.SPAWN_POS_Z.get() + 1), Registration.DDD_TELEPORTER_BLOCK.get().defaultBlockState());
                    if (!DDDConfig.DEEP_DARK_INCLUDES_TELEPORTER_BLOCK_FOR_TELEPORTING_BACK.get())
                        this.setBlock(seedReader, new BlockPos(DDDConfig.SPAWN_POS_X.get(), DDDConfig.SPAWN_POS_Y.get(), DDDConfig.SPAWN_POS_Z.get() + 1), Blocks.CAVE_AIR.defaultBlockState());
                } catch (RuntimeException e) {
                    return false;
                }
                placeBaseFrame(seedReader, rand);
                placeLightSources(seedReader);
                placeLadder(seedReader);
                placeExits(seedReader);
                placeRoof(seedReader, rand);
                if (DDDConfig.DEEP_DARK_INCLUDES_BONUS_CHEST.get())
                    placeStarterChest(seedReader, rand);
                return true;
            }
        }
        return false;
    }

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
                            placeWalls(seedReader, new BlockPos(x, y, z));
                        }
                        if (isCornerBlock) {
                            if (y == START_Y || y == MIDDLE_Y || y == END_Y)
                                this.setBlock(seedReader, new BlockPos(x, y, z), Blocks.CHISELED_STONE_BRICKS.defaultBlockState());
                            else
                                this.setBlock(seedReader, new BlockPos(x, y, z), determineStoneBrickState(rand));
                        }
                        if (!isCornerFrameBlock && !isCornerBlock && !(y == START_Y || y == MIDDLE_Y))
                            this.setBlock(seedReader, new BlockPos(x, y, z), Blocks.CAVE_AIR.defaultBlockState());
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
        boolean isInMiddleOfWall = pos.getX() >= DDDConfig.SPAWN_POS_X.get() - 1 && pos.getX() <= DDDConfig.SPAWN_POS_X.get() + 1 || pos.getZ() >= DDDConfig.SPAWN_POS_Z.get() - 1 && pos.getZ() <= DDDConfig.SPAWN_POS_Z.get() + 1;
        if (isInMiddleOfWall && pos.getY() == MIDDLE_Y + 2) {
            BlockState ironBars = Blocks.IRON_BARS.defaultBlockState();
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
        this.setBlock(seedReader, new BlockPos(DDDConfig.SPAWN_POS_X.get(), MIDDLE_Y, START_Z + 1), Blocks.DARK_OAK_TRAPDOOR.defaultBlockState().setValue(TrapDoorBlock.FACING, Direction.SOUTH).setValue(TrapDoorBlock.HALF, Half.TOP));
        this.setBlock(seedReader, new BlockPos(DDDConfig.SPAWN_POS_X.get(), MIDDLE_Y - 1, START_Z + 1), Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.SOUTH));
        this.setBlock(seedReader, new BlockPos(DDDConfig.SPAWN_POS_X.get(), MIDDLE_Y - 2, START_Z + 1), Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.SOUTH));
        this.setBlock(seedReader, new BlockPos(DDDConfig.SPAWN_POS_X.get(), MIDDLE_Y - 3, START_Z + 1), Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.SOUTH));
    }

    private void placeExits(ISeedReader seedReader) {
        //two iron doors with pressure plate
        //west
        this.setBlock(seedReader, new BlockPos(START_X, START_Y + 1, DDDConfig.SPAWN_POS_Z.get()), Blocks.IRON_DOOR.defaultBlockState().setValue(DoorBlock.FACING, Direction.EAST).setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER));
        this.setBlock(seedReader, new BlockPos(START_X, START_Y + 2, DDDConfig.SPAWN_POS_Z.get()), Blocks.IRON_DOOR.defaultBlockState().setValue(DoorBlock.FACING, Direction.EAST).setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER));
        this.setBlock(seedReader, new BlockPos(START_X - 1, START_Y + 1, DDDConfig.SPAWN_POS_Z.get()), Blocks.STONE_PRESSURE_PLATE.defaultBlockState());
        this.setBlock(seedReader, new BlockPos(START_X + 1, START_Y + 1, DDDConfig.SPAWN_POS_Z.get()), Blocks.STONE_PRESSURE_PLATE.defaultBlockState());
        this.setBlock(seedReader, new BlockPos(START_X - 1, START_Y + 2, DDDConfig.SPAWN_POS_Z.get() - 1), Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.WEST));
        this.setBlock(seedReader, new BlockPos(START_X - 1, START_Y + 2, DDDConfig.SPAWN_POS_Z.get() + 1), Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.WEST));
        //east
        this.setBlock(seedReader, new BlockPos(END_X, START_Y + 1, DDDConfig.SPAWN_POS_Z.get()), Blocks.IRON_DOOR.defaultBlockState().setValue(DoorBlock.FACING, Direction.WEST).setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER));
        this.setBlock(seedReader, new BlockPos(END_X, START_Y + 2, DDDConfig.SPAWN_POS_Z.get()), Blocks.IRON_DOOR.defaultBlockState().setValue(DoorBlock.FACING, Direction.WEST).setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER));
        this.setBlock(seedReader, new BlockPos(END_X - 1, START_Y + 1, DDDConfig.SPAWN_POS_Z.get()), Blocks.STONE_PRESSURE_PLATE.defaultBlockState());
        this.setBlock(seedReader, new BlockPos(END_X + 1, START_Y + 1, DDDConfig.SPAWN_POS_Z.get()), Blocks.STONE_PRESSURE_PLATE.defaultBlockState());
        this.setBlock(seedReader, new BlockPos(END_X + 1, START_Y + 2, DDDConfig.SPAWN_POS_Z.get() - 1), Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.EAST));
        this.setBlock(seedReader, new BlockPos(END_X + 1, START_Y + 2, DDDConfig.SPAWN_POS_Z.get() + 1), Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, Direction.EAST));
    }

    private void placeRoof(ISeedReader seedReader, Random rand) {
        for (int x = START_X - 1; x <= END_X + 1; x++) {
            for (int z = START_Z - 1; z <= END_Z + 1; z++) {
                boolean isOuterRectangle = (x == START_X - 1 || x == END_X + 1 || z == START_Z - 1 || z == END_Z + 1);
                boolean isOuterRectangle2 = (x == START_X || x == END_X || z == START_Z || z == END_Z);
                boolean isFrame = (x == START_X + 1 && z == START_Z + 1 || x == END_X - 1 && z == START_Z + 1 || x == START_X + 1 && z == END_Z - 1 || x == END_X - 1 && z == END_Z - 1);
                boolean isMiddleRectangle = (x == START_X + 1 || x == END_X - 1 || z == START_Z + 1 || z == END_Z - 1);
                boolean isMiddleRectangle2 = (x == START_X + 2 || x == END_X - 2 || z == START_Z + 2 || z == END_Z - 2);
                boolean isInnerRectangle = (x == START_X + 3 || x == END_X - 3 || z == START_Z + 3 || z == END_Z - 3);
                boolean isInnerRectangle2 = (x == START_X + 4 || x == END_X - 4 || z == START_Z + 4 || z == END_Z - 4);
                if (isOuterRectangle)
                    this.setBlock(seedReader, new BlockPos(x, END_Y, z), determineStoneBrickSlabState(rand).setValue(SlabBlock.TYPE, SlabType.TOP));
                if (isOuterRectangle2)
                    this.setBlock(seedReader, new BlockPos(x, END_Y + 1, z), determineStoneBrickSlabState(rand).setValue(SlabBlock.TYPE, SlabType.BOTTOM));
                if (isMiddleRectangle)
                    //this.setBlock(seedReader, new BlockPos(x,END_Y+1,z),determineStoneBrickSlabState(rand).setValue(SlabBlock.TYPE, SlabType.TOP));
                    this.setBlock(seedReader, new BlockPos(x, END_Y + 1, z), determineStoneBrickState(rand));
                if (isMiddleRectangle2) {
                    this.setBlock(seedReader, new BlockPos(x, END_Y + 1, z), determineStoneBrickSlabState(rand).setValue(SlabBlock.TYPE, SlabType.TOP));
                    this.setBlock(seedReader, new BlockPos(x, END_Y + 2, z), determineStoneBrickSlabState(rand).setValue(SlabBlock.TYPE, SlabType.BOTTOM));
                }
                if (isInnerRectangle)
                    //this.setBlock(seedReader, new BlockPos(x,END_Y+2,z),determineStoneBrickSlabState(rand).setValue(SlabBlock.TYPE, SlabType.TOP));
                    this.setBlock(seedReader, new BlockPos(x, END_Y + 2, z), determineStoneBrickState(rand));
                if (isInnerRectangle2) {
                    this.setBlock(seedReader, new BlockPos(x, END_Y + 2, z), determineStoneBrickSlabState(rand).setValue(SlabBlock.TYPE, SlabType.TOP));
                    this.setBlock(seedReader, new BlockPos(x, END_Y + 3, z), determineStoneBrickSlabState(rand).setValue(SlabBlock.TYPE, SlabType.BOTTOM));
                }
                if (isFrame)
                    this.setBlock(seedReader, new BlockPos(x, END_Y + 1, z), determineStoneBrickState(rand));
            }
        }
    }

    private void placeStarterChest(ISeedReader seedReader, Random rand) {
        BlockState chest = Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        if (chest.hasTileEntity()) {
            BlockPos chestPos = new BlockPos(START_X + 1, MIDDLE_Y + 1, START_Z + 1);
            this.setBlock(seedReader, chestPos, chest);
            TileEntity te = seedReader.getBlockEntity(chestPos);
            if (te instanceof ChestTileEntity) {
                ((ChestTileEntity) te).setLootTable(this.STARTER_CHEST_LOOT_TABLE, rand.nextLong());
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
//========SOLI DEO GLORIA========//