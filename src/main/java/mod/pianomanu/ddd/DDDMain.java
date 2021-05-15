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
        //LOGGER.info("HELLO FROM PREINIT");
        //LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        //LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
        RenderSetup.init();
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        //InterModComms.sendTo("examplemod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        //LOGGER.info("Got IMC {}", event.getIMCStream().
        //        map(m->m.getMessageSupplier().get()).
        //        collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        //LOGGER.info("HELLO from server starting");
    }

/*    @SubscribeEvent
    public void registerCarvers(RegistryEvent.Register<WorldCarver<?>> event) {
        LOGGER.info("Registering Carvers...");
        event.getRegistry().register(new DeepDarkCarver(ProbabilityConfig.CODEC));
    }*/
}
