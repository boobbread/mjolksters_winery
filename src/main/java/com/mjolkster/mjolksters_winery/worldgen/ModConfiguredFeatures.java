package com.mjolkster.mjolksters_winery.worldgen;

import com.mjolkster.mjolksters_winery.Winery;
import com.mjolkster.mjolksters_winery.block.GrapeBushBlock;
import com.mjolkster.mjolksters_winery.registry.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.RandomizedIntStateProvider;

import java.util.List;

public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?,?>> GRAPE_BUSH_KEY = registerKey("grape_bush");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?,?>> context)  {

        register(context, GRAPE_BUSH_KEY, Feature.RANDOM_PATCH,
                FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(RandomizedIntStateProvider.simple(ModBlocks.GRAPE_BUSH_BLOCK.get().defaultBlockState().setValue(GrapeBushBlock.AGE, 3))
                        ), List.of(Blocks.GRASS_BLOCK))
                );
    }


    public static ResourceKey<ConfiguredFeature<?,?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(Winery.MODID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?,?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?,?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
