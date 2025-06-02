package com.mjolkster.mjolksters_winery;

import com.mjolkster.mjolksters_winery.registry.*;
import com.mjolkster.mjolksters_winery.util.renderer.BottlingMachineBlockRenderer;
import com.mjolkster.mjolksters_winery.util.renderer.DemijohnBlockRenderer;
import com.mjolkster.mjolksters_winery.fluid.BaseFluidType;
import com.mjolkster.mjolksters_winery.screen.AgingBarrelScreen;
import com.mjolkster.mjolksters_winery.screen.BottlingMachineScreen;
import com.mjolkster.mjolksters_winery.screen.DemijohnScreen;
import com.mjolkster.mjolksters_winery.util.TooltipHandler;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Winery.MODID)
public class Winery
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "mjolksters_winery";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Winery(IEventBus modEventBus, ModContainer modContainer)
    {
        // Register the commonSetup method for mod loading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (winery) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(TooltipHandler.class);

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.CREATIVE_TABS.register(modEventBus);

        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        ModRecipes.register(modEventBus);
        ModSounds.register(modEventBus);

        ModFluidTypes.register(modEventBus);
        ModFluids.register(modEventBus);

        ModDataComponents.register(modEventBus);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(ModMenuTypes.DEMIJOHN_MENU.get(), DemijohnScreen::new);
            event.register(ModMenuTypes.AGING_BARREL_MENU.get(), AgingBarrelScreen::new);
            event.register(ModMenuTypes.BOTTLING_MACHINE_MENU.get(), BottlingMachineScreen::new);
        }

    }


    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientSetup {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                // Set render type here
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.DEMIJOHN.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.CRUSHER.get(), RenderType.solid());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.OAK_AGING_BARREL.get(), RenderType.solid());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.SPRUCE_AGING_BARREL.get(), RenderType.solid());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.ACACIA_AGING_BARREL.get(), RenderType.solid());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.BOTTLING_MACHINE.get(), RenderType.translucent());

                ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_RED_WINE.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_WHITE_WINE.get(), RenderType.translucent());
            });

        }
        @SubscribeEvent
        public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.DEMIJOHN_BE.get(), DemijohnBlockRenderer::new);
            event.registerBlockEntityRenderer(ModBlockEntities.BOTTLING_MACHINE_BE.get(), BottlingMachineBlockRenderer::new);
        }
        @SubscribeEvent
        public static void onClientExtensions(RegisterClientExtensionsEvent event) {
            event.registerFluidType(((BaseFluidType) ModFluidTypes.RED_WINE_FLUID_TYPE.get()).getClientFluidTypeExtensions(),
                    ModFluidTypes.RED_WINE_FLUID_TYPE.get());
            event.registerFluidType(((BaseFluidType) ModFluidTypes.WHITE_WINE_FLUID_TYPE.get()).getClientFluidTypeExtensions(),
                    ModFluidTypes.WHITE_WINE_FLUID_TYPE.get());
        }
    }
}
