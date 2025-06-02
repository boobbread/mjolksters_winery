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

@Mod(Winery.MODID)
public class Winery
{
    public static final String MODID = "mjolksters_winery";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Winery(IEventBus modEventBus, ModContainer modContainer)
    {
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(TooltipHandler.class);

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.CREATIVE_TABS.register(modEventBus);

        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        ModRecipes.register(modEventBus);
        ModSounds.register(modEventBus);

        ModFluids.FLUIDS.register(modEventBus);
        ModFluids.FLUID_TYPES.register(modEventBus);

        ModDataComponents.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("HELLO from server starting");
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
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
                // blocks
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.DEMIJOHN.get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.CRUSHER.get(), RenderType.solid());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.OAK_AGING_BARREL.get(), RenderType.solid());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.SPRUCE_AGING_BARREL.get(), RenderType.solid());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.ACACIA_AGING_BARREL.get(), RenderType.solid());
                ItemBlockRenderTypes.setRenderLayer(ModBlocks.BOTTLING_MACHINE.get(), RenderType.translucent());

                // fluids
                ItemBlockRenderTypes.setRenderLayer(ModFluids.RED_WINE.source().get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(ModFluids.WHITE_WINE.source().get(), RenderType.translucent());
            });

        }
        @SubscribeEvent
        public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.DEMIJOHN_BE.get(), DemijohnBlockRenderer::new);
            event.registerBlockEntityRenderer(ModBlockEntities.BOTTLING_MACHINE_BE.get(), BottlingMachineBlockRenderer::new);
        }
        @SubscribeEvent
        public static void onClientExtensions(RegisterClientExtensionsEvent event) {
            event.registerFluidType(((BaseFluidType) ModFluids.RED_WINE.fluidType().get()).getClientFluidTypeExtensions(),
                    ModFluids.RED_WINE.fluidType().get());
            event.registerFluidType(((BaseFluidType) ModFluids.WHITE_WINE.fluidType().get()).getClientFluidTypeExtensions(),
                    ModFluids.WHITE_WINE.fluidType().get());
            event.registerFluidType(((BaseFluidType) ModFluids.POTATO_WASH.fluidType().get()).getClientFluidTypeExtensions(),
                    ModFluids.POTATO_WASH.fluidType().get());
        }
    }
}
