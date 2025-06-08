package com.mjolkster.mjolksters_winery.datagen;

import com.mjolkster.mjolksters_winery.registry.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.DEMIJOHN.get());
        dropSelf(ModBlocks.OAK_AGING_BARREL.get());
        dropSelf(ModBlocks.SPRUCE_AGING_BARREL.get());
        dropSelf(ModBlocks.CHERRY_AGING_BARREL.get());
        dropSelf(ModBlocks.CRUSHER.get());
        dropSelf(ModBlocks.BOTTLING_MACHINE.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
