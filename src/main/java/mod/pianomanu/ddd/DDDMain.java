package mod.pianomanu.ddd;

import mod.pianomanu.ddd.init.Registration;
import mod.pianomanu.ddd.init.RenderSetup;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static mod.pianomanu.ddd.DDDMain.MOD_ID;

/**
 * Main class of the Deep Dark Dimension mod
 *
 * @author PianoManu
 * @version 1.0 05/15/21
 */
@Mod(MOD_ID)
public class DDDMain {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "ddd";

    public static RegistryKey<World> DEEP_DARK_DIMENSION;

    public DDDMain() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);

        LOGGER.info("Registering everything from Deep Dark Dimension...");
        Registration.init();
        LOGGER.info("Registered everything from Deep Dark Dimension!");

        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
    }

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        // some preinit code
        DEEP_DARK_DIMENSION = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(MOD_ID, "deep_dark"));
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        RenderSetup.init();
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {

    }

    private void processIMC(final InterModProcessEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
    }

}
//This mod is dedicated to the living God and His son, Jesus. Without His support, I would never have had enough strength and perseverance to get this project working and publish it. Learn to hear His voice, it will transform your life. (Based on a quote from Covert_Jaguar, creator of RailCraft)
//========SOLI DEO GLORIA========//