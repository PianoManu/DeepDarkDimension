package mod.pianomanu.ddd.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;

import java.nio.file.Path;

/**
 * Will add description later...
 *
 * @author PianoManu
 * @version 1.1 05/17/21
 */
public class DDDConfig {
    public static final String CATEGORY_SPAWNING = "spawning";
    public static final String CATEGORY_GENERATION = "world_generation";
    public static final String CATEGORY_PLAYER = "player_config";

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec CONFIG;

    private static ForgeConfigSpec.IntValue CAVE_FLOOR_HEIGHT;
    public static final int CAVE_LOWER_LIMIT = CAVE_FLOOR_HEIGHT.get();
    private static ForgeConfigSpec.IntValue CAVE_CEILING_HEIGHT;
    public static final int CAVE_UPPER_LIMIT = CAVE_CEILING_HEIGHT.get();
    private static ForgeConfigSpec.IntValue SPAWN_POS_X;
    private static ForgeConfigSpec.IntValue SPAWN_POS_Y;
    private static ForgeConfigSpec.IntValue SPAWN_POS_Z;
    public static final BlockPos SPAWN_POS = new BlockPos(SPAWN_POS_X.get(), SPAWN_POS_Y.get(), SPAWN_POS_Z.get());
    private static ForgeConfigSpec.BooleanValue DEEP_DARK_INCLUDES_TELEPORTER_BLOCK_FOR_TELEPORTING_BACK;
    public static final boolean INCLUDE_TELEPORTER_BLOCK = DEEP_DARK_INCLUDES_TELEPORTER_BLOCK_FOR_TELEPORTING_BACK.get();
    private static ForgeConfigSpec.BooleanValue DEEP_DARK_INCLUDES_BONUS_CHEST; //TODO doesn't work right now
    public static final boolean INCLUDE_STARTER_CHEST = DEEP_DARK_INCLUDES_BONUS_CHEST.get(); //not working
    private static ForgeConfigSpec.IntValue MAX_TIME_IN_DARKNESS_BEFORE_RECEIVING_DAMAGE;
    public static final int MAX_TIME_IN_DARKNESS_BEFORE_DAMAGE = MAX_TIME_IN_DARKNESS_BEFORE_RECEIVING_DAMAGE.get();
    private static ForgeConfigSpec.DoubleValue DAMAGE_PER_HIT_RECEIVED_BY_DARKNESS;
    public static final float DAMAGE_PER_HIT_BY_DARKNESS = DAMAGE_PER_HIT_RECEIVED_BY_DARKNESS.get().floatValue();
    private static ForgeConfigSpec.BooleanValue PLAYER_SPAWNS_IN_DEEP_DARK_DIMENSION;
    public static final boolean SPAWN_IN_DEEP_DARK_DIMENSION = PLAYER_SPAWNS_IN_DEEP_DARK_DIMENSION.get();
    private static ForgeConfigSpec.BooleanValue CREATE_SPAWN_BASE;
    public static final boolean CREATE_SPAWN_BASE_IN_DEEP_DARK_DIMENSION = CREATE_SPAWN_BASE.get();

    static {
        BUILDER.comment("World generation settings").push(CATEGORY_GENERATION);
        setupWorldGenerationSettings();
        BUILDER.pop();
        BUILDER.comment("Player spawn settings").push(CATEGORY_SPAWNING);
        setupSpawnSettings();
        BUILDER.pop();
        BUILDER.comment("Player entity settings").push(CATEGORY_PLAYER);
        setupPlayerSettings();
        BUILDER.pop();

        CONFIG = BUILDER.build();
    }

    private static void setupWorldGenerationSettings() {
        CAVE_FLOOR_HEIGHT = BUILDER.comment("Y-value for floor height of deep dark dimension (lower limit of the cave). Changing this value might result in corrupted structures.").defineInRange("cave_floor_height", 64, 0, 255);
        CAVE_CEILING_HEIGHT = BUILDER.comment("Y-value for ceiling height of deep dark dimension (upper limit of the cave). Changing this value might result in corrupted structures.\nY-value of the ceiling has to be equal to or larger than the floor of the cave").defineInRange("cave_ceiling_height", 96, 0, 255);
    }

    private static void setupSpawnSettings() {
        SPAWN_POS_X = BUILDER.comment("X-value for spawn position in deep dark - should only be changed, if you want to play without a spawn base").defineInRange("spawn_pos_x", 8, Integer.MIN_VALUE, Integer.MAX_VALUE);
        SPAWN_POS_Y = BUILDER.comment("Y-value for spawn position in deep dark - should only be changed, if you want to play without a spawn base").defineInRange("spawn_pos_y", 70, Integer.MIN_VALUE, Integer.MAX_VALUE);
        SPAWN_POS_Z = BUILDER.comment("Z-value for spawn position in deep dark - should only be changed, if you want to play without a spawn base").defineInRange("spawn_pos_z", 8, Integer.MIN_VALUE, Integer.MAX_VALUE);

        DEEP_DARK_INCLUDES_TELEPORTER_BLOCK_FOR_TELEPORTING_BACK = BUILDER.comment("When set to true, the deep dark dimension will contain a teleporter block near the default spawn position to bring you back to the overworld").define("teleporter_block_included", true);
        DEEP_DARK_INCLUDES_BONUS_CHEST = BUILDER.comment("When set to true, the deep dark dimension will contain a bonus chest with some useful loot (i.e. saplings, seeds, etc.)\nYou should enable this feature, if you want to spawn in the deep dark dimension when creating a new world - otherwise it might be really hard to play successfully").define("bonus_chest_included", false); //TODO fix bonus chests

        PLAYER_SPAWNS_IN_DEEP_DARK_DIMENSION = BUILDER.comment("When set to true, the player will spawn in the deep dark dimension instead of the overworld after creating a new world.\nWARNING: This could lead to unforeseen problems, when using other mods that add custom dimensions and try to do the same").define("spawn_in_ddd", false);

        CREATE_SPAWN_BASE = BUILDER.comment("When set to true, a small base structure will be generated at the spawn coordinates").define("create_base", true);
    }

    private static void setupPlayerSettings() {
        MAX_TIME_IN_DARKNESS_BEFORE_RECEIVING_DAMAGE = BUILDER.comment("Set to a value >= 0 to receive damage when being in darkness for some time. This value determines, how many time (in ticks) has to pass before getting damage from the darkness").defineInRange("time_before_damage", 400, -1, Integer.MAX_VALUE);
        DAMAGE_PER_HIT_RECEIVED_BY_DARKNESS = BUILDER.comment("Determines how much damage is dealt, when the darkness attacks the player, 1.0 equals half a heart.\nNote: Setting this parameter to a value equal to or larger than 20 will result in an insta-kill under most circumstances (exception: having more that 10 hearts)").defineInRange("damage_per_hit", 1.0, 0.0, Double.MAX_VALUE);
    }

    @SubscribeEvent
    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
        configData.load();
        spec.setConfig(configData);
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {

    }

    @SubscribeEvent
    public static void onReload(final ModConfig.Reloading configEvent) {
    }
}
//========SOLI DEO GLORIA========//