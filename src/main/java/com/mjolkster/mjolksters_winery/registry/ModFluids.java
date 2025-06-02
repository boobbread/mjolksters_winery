package com.mjolkster.mjolksters_winery.registry;

import com.mjolkster.mjolksters_winery.Winery;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(BuiltInRegistries.FLUID, Winery.MODID);

    // RED WINE
    public static final Supplier<FlowingFluid> SOURCE_RED_WINE = FLUIDS.register("source_red_wine",
            () -> new BaseFlowingFluid.Source(ModFluids.RED_WINE_PROPERTIES));
    public static final Supplier<FlowingFluid> FLOWING_RED_WINE = FLUIDS.register("flowing_red_wine",
            () -> new BaseFlowingFluid.Flowing(ModFluids.RED_WINE_PROPERTIES));

    public static final BaseFlowingFluid.Properties RED_WINE_PROPERTIES = new BaseFlowingFluid.Properties(
            ModFluidTypes.RED_WINE_FLUID_TYPE, SOURCE_RED_WINE, FLOWING_RED_WINE)
            .slopeFindDistance(2).levelDecreasePerBlock(1)
            .bucket(ModItems.RED_WINE_BUCKET);

    // WHITE WINE
    public static final Supplier<FlowingFluid> SOURCE_WHITE_WINE = FLUIDS.register("source_white_wine",
            () -> new BaseFlowingFluid.Source(ModFluids.WHITE_WINE_PROPERTIES));
    public static final Supplier<FlowingFluid> FLOWING_WHITE_WINE = FLUIDS.register("flowing_white_wine",
            () -> new BaseFlowingFluid.Flowing(ModFluids.WHITE_WINE_PROPERTIES));


    public static final BaseFlowingFluid.Properties WHITE_WINE_PROPERTIES = new BaseFlowingFluid.Properties(
            ModFluidTypes.WHITE_WINE_FLUID_TYPE, SOURCE_WHITE_WINE, FLOWING_WHITE_WINE)
            .slopeFindDistance(2).levelDecreasePerBlock(1)
            .bucket(ModItems.WHITE_WINE_BUCKET);


    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }
}