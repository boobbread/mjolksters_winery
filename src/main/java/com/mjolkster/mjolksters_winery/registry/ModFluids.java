package com.mjolkster.mjolksters_winery.registry;

import com.mjolkster.mjolksters_winery.Winery;
import com.mjolkster.mjolksters_winery.fluid.BaseFluidType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class ModFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, Winery.MODID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, Winery.MODID);

    public static final ResourceLocation WATER_STILL_RL = ResourceLocation.parse("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = ResourceLocation.parse("block/water_flow");
    public static final ResourceLocation WATER_OVERLAY_RL = ResourceLocation.parse("block/water_overlay");

    public static final WineryFluid RED_WINE = registerWineFluid("red_wine", 0xFF5B0000, WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL, () -> ModItems.RED_WINE_BUCKET);
    public static final WineryFluid WHITE_WINE = registerWineFluid("white_wine", 0xFFFFE9AA, WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL, () -> ModItems.WHITE_WINE_BUCKET);

    private static Supplier<FluidType> registerFluidType(String name, FluidType fluidType) {
        return FLUID_TYPES.register(name, () -> fluidType);
    }

    private static WineryFluid registerWineFluid(String name, int color, ResourceLocation still, ResourceLocation flowing, ResourceLocation overlay, Supplier<Supplier<Item>> bucket) {
        Supplier<FluidType> fluidType = registerFluidType(name + "_fluid",
                new BaseFluidType(still, flowing, overlay, color,
                        new Vector3f(108f / 255f, 168f / 255f, 212f / 255f),
                        FluidType.Properties.create()));

        Supplier<FlowingFluid>[] source = new Supplier[1];
        Supplier<FlowingFluid>[] flowingFluid = new Supplier[1];

        BaseFlowingFluid.Properties properties = new BaseFlowingFluid.Properties(
                fluidType, () -> source[0].get(), () -> flowingFluid[0].get())
                .slopeFindDistance(2).levelDecreasePerBlock(1)
                .bucket(bucket.get());

        source[0] = FLUIDS.register("source_" + name, () -> new BaseFlowingFluid.Source(properties));
        flowingFluid[0] = FLUIDS.register("flowing_" + name, () -> new BaseFlowingFluid.Flowing(properties));

        return new WineryFluid(fluidType, source[0], flowingFluid[0], properties);
    }

    public static void register(IEventBus eventBus) {
        FLUIDS.register(eventBus);
        FLUID_TYPES.register(eventBus);
    }

    public record WineryFluid(
            Supplier<FluidType> fluidType,
            Supplier<FlowingFluid> source,
            Supplier<FlowingFluid> flowing,
            BaseFlowingFluid.Properties properties
    ) {}
}
