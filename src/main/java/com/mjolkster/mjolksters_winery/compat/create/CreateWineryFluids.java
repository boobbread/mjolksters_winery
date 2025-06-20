package com.mjolkster.mjolksters_winery.compat.create;

import com.mjolkster.mjolksters_winery.MjolkstersWinery;
import com.mjolkster.mjolksters_winery.common.fluid.BaseFluidType;
import com.mjolkster.mjolksters_winery.common.registry.ModFluids;
import com.mjolkster.mjolksters_winery.common.registry.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class CreateWineryFluids {
    public static final List<WineryFluid> WINERY_FLUIDS = new ArrayList<>();
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, MjolkstersWinery.MODID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, MjolkstersWinery.MODID);

    // Resources
    public static final ResourceLocation WATER_STILL_RL = ResourceLocation.parse("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = ResourceLocation.parse("block/water_flow");
    public static final ResourceLocation WATER_OVERLAY_RL = ResourceLocation.parse("block/water_overlay");

    public static Map<String, Integer> COLOURS = new HashMap<>();
    static {
        COLOURS.put("pinot_noir", 0xFF1E0926);
        COLOURS.put("sangiovese", 0xFF200F40);
        COLOURS.put("cabernet_sauvignon", 0xFF1C0F2E);
        COLOURS.put("tempranillo", 0xFF291261);
        COLOURS.put("moondrop", 0xFF1F072E);
        COLOURS.put("autumn_royal", 0xFF1B0D40);
        COLOURS.put("ruby_roman", 0xFF820C1B);

        COLOURS.put("riesling", 0xFFACB030);
        COLOURS.put("chardonnay", 0xFFCFBD48);
        COLOURS.put("sauvignon_blanc", 0xFFB8A727);
        COLOURS.put("pinot_grigio", 0xFFD9B841);
        COLOURS.put("cotton_candy", 0xFFFFE9AA);
        COLOURS.put("grenache_blanc", 0xFFD6D060);
        COLOURS.put("waterfall", 0xFF8DBD4A);

        COLOURS.put("koshu", 0xFFFF8797);
        COLOURS.put("pinot_de_lenfer", 0xFF33060C);

        COLOURS.put("sweet_berry", 0xFFFFE9AA);
        COLOURS.put("glow_berry", 0xFFE6922C);
        COLOURS.put("chorus_fruit", 0xFF8F5CB5);

        for (String i : COLOURS.keySet()) {
            registerWineFluid(i, COLOURS.get(i), () -> ModItems.WINE_BUCKET);
            registerJuiceFluid(i, COLOURS.get(i), () -> ModItems.JUICE_BUCKET);
        }

    }

    public static final WineryFluid WINE = registerWineFluid("wine", 0xFFFFFFFF, () -> ModItems.WINE_BUCKET);

    private static Supplier<FluidType> registerFluidType(String name, FluidType fluidType) {
        return FLUID_TYPES.register(name, () -> fluidType);
    }

    private static WineryFluid registerWineFluid(String name, int colour, Supplier<Supplier<Item>> bucket) {
        Supplier<FluidType> fluidType = registerFluidType(name + "_wine_create_fluid",
                new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL, colour,
                        new Vector3f(108f / 255f, 168f / 255f, 212f / 255f),
                        FluidType.Properties.create()));

        Supplier<FlowingFluid>[] source = new Supplier[1];
        Supplier<FlowingFluid>[] flowingFluid = new Supplier[1];

        BaseFlowingFluid.Properties properties = new BaseFlowingFluid.Properties(
                fluidType, () -> source[0].get(), () -> flowingFluid[0].get())
                .slopeFindDistance(2).levelDecreasePerBlock(1)
                .bucket(bucket.get());

        source[0] = FLUIDS.register("create_source_" + name + "_wine", () -> new BaseFlowingFluid.Source(properties));
        flowingFluid[0] = FLUIDS.register("create_flowing_" + name + "_wine", () -> new BaseFlowingFluid.Flowing(properties));

        WineryFluid fluid = new WineryFluid(fluidType, source[0], flowingFluid[0], properties);
        WINERY_FLUIDS.add(fluid);

        return new WineryFluid(fluidType, source[0], flowingFluid[0], properties);
    }

    private static WineryFluid registerJuiceFluid(String name, int colour, Supplier<Supplier<Item>> bucket) {
        Supplier<FluidType> fluidType = registerFluidType(name + "_juice_create_fluid",
                new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL, colour,
                        new Vector3f(108f / 255f, 168f / 255f, 212f / 255f),
                        FluidType.Properties.create()));

        Supplier<FlowingFluid>[] source = new Supplier[1];
        Supplier<FlowingFluid>[] flowingFluid = new Supplier[1];

        BaseFlowingFluid.Properties properties = new BaseFlowingFluid.Properties(
                fluidType, () -> source[0].get(), () -> flowingFluid[0].get())
                .slopeFindDistance(2).levelDecreasePerBlock(1)
                .bucket(bucket.get());

        source[0] = FLUIDS.register("create_source_" + name + "_juice", () -> new BaseFlowingFluid.Source(properties));
        flowingFluid[0] = FLUIDS.register("create_flowing_" + name + "_juice", () -> new BaseFlowingFluid.Flowing(properties));

        WineryFluid fluid = new WineryFluid(fluidType, source[0], flowingFluid[0], properties);
        WINERY_FLUIDS.add(fluid);

        return new WineryFluid(fluidType, source[0], flowingFluid[0], properties);
    }

    public record WineryFluid(
            Supplier<FluidType> fluidType,
            Supplier<FlowingFluid> source,
            Supplier<FlowingFluid> flowing,
            BaseFlowingFluid.Properties properties
    ) {}
}
