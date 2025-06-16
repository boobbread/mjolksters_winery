package com.mjolkster.mjolksters_winery.client;

import com.mjolkster.mjolksters_winery.MjolkstersWinery;
import com.mjolkster.mjolksters_winery.client.renderer.BottlingMachineBlockRenderer;
import com.mjolkster.mjolksters_winery.client.renderer.DemijohnBlockRenderer;
import com.mjolkster.mjolksters_winery.client.renderer.TrellisBlockRenderer;
import com.mjolkster.mjolksters_winery.common.block.GrapeBushBlock;
import com.mjolkster.mjolksters_winery.common.fluid.BaseFluidType;
import com.mjolkster.mjolksters_winery.common.registry.*;
import com.mjolkster.mjolksters_winery.util.codec.WineData;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.fluids.FluidType;

@EventBusSubscriber(modid = MjolkstersWinery.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // blocks
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.DEMIJOHN.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CRUSHER.get(), RenderType.solid());

            ItemBlockRenderTypes.setRenderLayer(ModBlocks.OAK_AGING_BARREL.get(), RenderType.solid());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.SPRUCE_AGING_BARREL.get(), RenderType.solid());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CHERRY_AGING_BARREL.get(), RenderType.solid());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CRIMSON_AGING_BARREL.get(), RenderType.solid());

            ItemBlockRenderTypes.setRenderLayer(ModBlocks.BOTTLING_MACHINE.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.YEAST_POT.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.SOMMELIERS_TABLE.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.GRAPE_BUSH_BLOCK.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.TRELLIS.get(), RenderType.cutout());

            // fluids
            for (ModFluids.WineryFluid fluid : ModFluids.WINERY_FLUIDS) {
                ItemBlockRenderTypes.setRenderLayer(fluid.source().get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(fluid.flowing().get(), RenderType.translucent());
            }
        });
    }

    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.DEMIJOHN_BE.get(), DemijohnBlockRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.BOTTLING_MACHINE_BE.get(), BottlingMachineBlockRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.TRELLIS_BE.get(), TrellisBlockRenderer::new);
    }

    @SubscribeEvent
    public static void onClientExtensions(RegisterClientExtensionsEvent event) {
        for (ModFluids.WineryFluid fluid : ModFluids.WINERY_FLUIDS) {
            FluidType type = fluid.fluidType().get();
            if (type instanceof BaseFluidType baseType) {
                event.registerFluidType(baseType.getClientFluidTypeExtensions(), type);
            }
        }
    }

    @SubscribeEvent
    public static void registerItemColours(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {
            if (tintIndex == 1) {
                WineData data = stack.get(ModDataComponents.WINE_DATA.get());

                if (data != null) {
                    return data.colour();
                }

            }
            return -1;
        }, ModItems.JUICE_BUCKET.get());

        event.register((stack, tintIndex) -> {
            if (tintIndex == 1) {
                WineData data = stack.get(ModDataComponents.WINE_DATA.get());

                if (data != null) {
                    return data.colour();
                }

            }
            return -1;
        }, ModItems.WINE_BUCKET.get());
    }

    @SubscribeEvent
    public static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {

        BlockColor grapeBushTint = (BlockState state, BlockAndTintGetter world, BlockPos pos, int tintIndex) -> {
            if (tintIndex == 1 && state.hasProperty(GrapeBushBlock.VARIETY)) {
                return GrapeBushBlock.getColorForVariety(state.getValue(GrapeBushBlock.VARIETY));
            }
            return -1;
        };

        event.register(grapeBushTint, ModBlocks.GRAPE_BUSH_BLOCK.get());
    }

}
