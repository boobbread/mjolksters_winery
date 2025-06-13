package com.mjolkster.mjolksters_winery.common.world;

import com.mjolkster.mjolksters_winery.mixin.StructureTemplatePoolAccessor;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WineryVillageStructures {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation PLAINS_HOUSES = ResourceLocation.parse("minecraft:village/plains/houses");
    private static final ResourceLocation TAVERN_STRUCTURE = ResourceLocation.parse("mjolksters_winery:village/houses/tavern_plains");
    private static final ResourceLocation FD_PROCESSOR = ResourceLocation.parse("farmersdelight:village/compost_pile");
    private static final ResourceLocation EMPTY_PROCESSOR = ResourceLocation.parse("minecraft:empty");
    private static final int WEIGHT = 5;

    public static void setup() {
        NeoForge.EVENT_BUS.addListener(WineryVillageStructures::addToVillages);
    }

    private static void addToVillages(ServerAboutToStartEvent event) {
        try {
            LOGGER.info("[Winery] Starting village structure injection");
            MinecraftServer server = event.getServer();
            RegistryAccess registryAccess = server.registryAccess();
            Registry<StructureTemplatePool> poolRegistry = registryAccess.registry(Registries.TEMPLATE_POOL).orElseThrow();
            Registry<StructureProcessorList> processorRegistry = registryAccess.registry(Registries.PROCESSOR_LIST).orElseThrow();

            // Get processor - try Farmers Delight's first, fallback to vanilla empty
            Holder<StructureProcessorList> processorHolder = getProcessorHolder(processorRegistry);

            // Create structure element
            StructurePoolElement piece = SinglePoolElement.single(TAVERN_STRUCTURE.toString(), processorHolder)
                    .apply(StructureTemplatePool.Projection.RIGID);

            // Get the target pool
            StructureTemplatePool pool = poolRegistry.get(PLAINS_HOUSES);
            if (pool == null) {
                LOGGER.error("[Winery] Target pool not found: {}", PLAINS_HOUSES);
                return;
            }

            // Use mixin accessor
            StructureTemplatePoolAccessor poolAccessor = (StructureTemplatePoolAccessor) pool;

            // Get current state (create copies to avoid concurrent modification)
            ObjectArrayList<StructurePoolElement> templates = new ObjectArrayList<>(poolAccessor.getTemplates());
            List<Pair<StructurePoolElement, Integer>> rawTemplates = new ArrayList<>(poolAccessor.getRawTemplates());

            // Add to templates
            for (int i = 0; i < WEIGHT; i++) {
                templates.add(piece);
            }

            // Add to rawTemplates
            rawTemplates.add(Pair.of(piece, WEIGHT));

            // Update the pool
            poolAccessor.setTemplates(templates);
            poolAccessor.setRawTemplates(rawTemplates);

            LOGGER.info("[Winery] Successfully added tavern to villages");

        } catch (Throwable e) {
            LOGGER.error("[Winery] Failed to add village structure", e);
        }
    }

    private static Holder<StructureProcessorList> getProcessorHolder(Registry<StructureProcessorList> processorRegistry) {
        // Try Farmers Delight processor first
        ResourceKey<StructureProcessorList> fdKey = ResourceKey.create(Registries.PROCESSOR_LIST, FD_PROCESSOR);
        Optional<Holder.Reference<StructureProcessorList>> fdHolder = processorRegistry.getHolder(fdKey);

        if (fdHolder.isPresent()) {
            LOGGER.info("[Winery] Using Farmers Delight processor");
            return fdHolder.get();
        }

        // Fallback to vanilla empty processor
        LOGGER.warn("[Winery] Farmers Delight processor not found, using empty processor");
        return processorRegistry.getHolderOrThrow(
                ResourceKey.create(Registries.PROCESSOR_LIST, EMPTY_PROCESSOR)
        );
    }
}