package mod.pianomanu.ddd.init;

import mod.pianomanu.ddd.DDDMain;
import mod.pianomanu.ddd.block.DDDTeleporterBlock;
import mod.pianomanu.ddd.tileentity.TeleporterBlockTileEntity;
import mod.pianomanu.ddd.world.carver.DeepDarkCarver;
import mod.pianomanu.ddd.world.feature.DeepDarkPillar;
import mod.pianomanu.ddd.world.feature.DeepDarkStartingBase;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.gen.carver.WorldCarver;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.world.ForgeWorldType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Will add description later...
 *
 * @author PianoManu
 * @version 1.0 05/15/21
 */
public class Registration {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DDDMain.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DDDMain.MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, DDDMain.MOD_ID);
    private static final DeferredRegister<ForgeWorldType> DIMENSIONS = DeferredRegister.create(ForgeRegistries.WORLD_TYPES, DDDMain.MOD_ID);
    private static final DeferredRegister<WorldCarver<?>> CARVERS = DeferredRegister.create(ForgeRegistries.WORLD_CARVERS, DDDMain.MOD_ID);
    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, DDDMain.MOD_ID);

    public static final RegistryObject<Block> DDD_TELEPORTER_BLOCK = BLOCKS.register("teleporter", () -> new DDDTeleporterBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).harvestTool(ToolType.PICKAXE).harvestLevel(4).strength(30f, 600f).sound(SoundType.GLASS)));
    public static final RegistryObject<Item> DDD_TELEPORTER_BLOCK_ITEM = ITEMS.register("teleporter", () -> new BlockItem(DDD_TELEPORTER_BLOCK.get(), new Item.Properties().rarity(Rarity.UNCOMMON).tab(ItemGroup.TAB_MISC)));
    @SuppressWarnings("all")
    public static final RegistryObject<TileEntityType<TeleporterBlockTileEntity>> DDD_TELEPORTER_TILE_ENTITY = TILE_ENTITIES.register("teleporter", () -> TileEntityType.Builder.of(TeleporterBlockTileEntity::new, DDD_TELEPORTER_BLOCK.get()).build(null));

    public static final RegistryObject<WorldCarver<?>> DEEP_DARK_CAVE_CARVER = CARVERS.register("deep_dark_cave", () -> new DeepDarkCarver(ProbabilityConfig.CODEC));

    public static final RegistryObject<Feature<OreFeatureConfig>> DEEP_DARK_PILLAR_FEATURE = FEATURES.register("deep_dark_pillar", () -> new DeepDarkPillar(OreFeatureConfig.CODEC));
    public static final RegistryObject<Feature<NoFeatureConfig>> SPAWN_BASE_FEATURE = FEATURES.register("spawn_base", () -> new DeepDarkStartingBase(NoFeatureConfig.CODEC));

    public static void init() {
        LOGGER.info("Registering Blocks from Deep Dark Dimension...");
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        LOGGER.info("Registered Blocks from Deep Dark Dimension!");
        LOGGER.info("Registering Items from Deep Dark Dimension...");
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        LOGGER.info("Registered Items from Deep Dark Dimension!");
        LOGGER.info("Registering Tile Entites from Deep Dark Dimension...");
        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        LOGGER.info("Registered Tile Entites from Deep Dark Dimension!");
        LOGGER.info("Registering Dimensions from Deep Dark Dimension...");
        DIMENSIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
        LOGGER.info("Registered Dimensions from Deep Dark Dimension!");
        LOGGER.info("Registering Carvers from Deep Dark Dimension...");
        CARVERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        LOGGER.info("Registered Carvers from Deep Dark Dimension!");
        LOGGER.info("Registering Features from Deep Dark Dimension...");
        FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
        LOGGER.info("Registered Features from Deep Dark Dimension!");
    }
}
//========SOLI DEO GLORIA========//