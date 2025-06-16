package com.mjolkster.mjolksters_winery;

import com.mjolkster.mjolksters_winery.common.registry.*;
import com.mjolkster.mjolksters_winery.common.handler.TooltipHandler;
import com.mjolkster.mjolksters_winery.common.worldgen.WineryVillageStructures;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(MjolkstersWinery.MODID)
public class MjolkstersWinery
{
    public static final String MODID = "mjolksters_winery";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MjolkstersWinery(IEventBus modEventBus, ModContainer modContainer)
    {
        NeoForge.EVENT_BUS.register(TooltipHandler.class);
        NeoForge.EVENT_BUS.register(WineryVillageStructures.class);

        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.CREATIVE_TABS.register(modEventBus);

        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);

        ModFluids.FLUIDS.register(modEventBus);
        ModFluids.FLUID_TYPES.register(modEventBus);

        ModVillagers.POI_TYPE.register(modEventBus);
        ModVillagers.VILLAGER_PROFESSION.register(modEventBus);

        ModRecipes.SERIALISERS.register(modEventBus);
        ModRecipes.TYPES.register(modEventBus);

        ModDataComponents.DATA_COMPONENTS.register(modEventBus);

        ModSounds.register(modEventBus);

        ModMenuTypes.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}


























