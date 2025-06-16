package com.mjolkster.mjolksters_winery.common.worldgen;

import com.mjolkster.mjolksters_winery.MjolkstersWinery;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

import java.util.ArrayList;
import java.util.List;

import static com.mjolkster.mjolksters_winery.MjolkstersWinery.LOGGER;

public class WineryVillageStructures {

    // Cheers to Farmer's Delight for this one
    // If you have troubles with private access, ensure you have the META-INF/accesstransformer.cfg and register it in build.gradle

    @SubscribeEvent
    public static void addNewVillageBuilding(final ServerAboutToStartEvent event) {
            Registry<StructureTemplatePool> templatePools = event.getServer().registryAccess().registry(Registries.TEMPLATE_POOL).get();
            Registry<StructureProcessorList> processorLists = event.getServer().registryAccess().registry(Registries.PROCESSOR_LIST).get();

            WineryVillageStructures.addBuildingToPool(templatePools, processorLists, ResourceLocation.parse("minecraft:village/plains/houses"), MjolkstersWinery.MODID + ":village/houses/tavern_plains", 10);

        }

    public static void addBuildingToPool(Registry<StructureTemplatePool> templatePoolRegistry, Registry<StructureProcessorList> processorListRegistry, ResourceLocation poolRL, String nbtPieceRL, int weight) {
        StructureTemplatePool pool = templatePoolRegistry.get(poolRL);

        LOGGER.info("[WINERY] addBuildingToPool called");
        if (pool == null) return;

        ResourceLocation emptyProcessor = ResourceLocation.withDefaultNamespace("empty");
        Holder<StructureProcessorList> processorHolder = processorListRegistry.getHolderOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, emptyProcessor));

        SinglePoolElement piece = SinglePoolElement.single(nbtPieceRL, processorHolder).apply(StructureTemplatePool.Projection.RIGID);

        for (int i = 0; i < weight; i++) {
            pool.templates.add(piece);
        }

        LOGGER.info("[WINERY] added {} with weight {}", piece, weight);

        List<Pair<StructurePoolElement, Integer>> listOfPieceEntries = new ArrayList<>(pool.rawTemplates);
        listOfPieceEntries.add(new Pair<>(piece, weight));
        pool.rawTemplates = listOfPieceEntries;
    }

    private static void addNewRuleToProcessorList(ResourceLocation targetProcessorList, StructureProcessor processorToAdd, Registry<StructureProcessorList> processorListRegistry) {
        processorListRegistry.getOptional(targetProcessorList)
                .ifPresent(processorList -> {
                    List<StructureProcessor> newSafeList = new ArrayList<>(processorList.list());
                    newSafeList.add(processorToAdd);
                    processorList.list = newSafeList;
                });
    }
}