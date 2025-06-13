package com.mjolkster.mjolksters_winery.tag;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class CommonTags {
    public static final TagKey<Fluid> WINE = commonFluidTag("wine");
    public static final TagKey<Item> GRAPE = commonItemTag("grape");
    public static final TagKey<Item> YEAST = commonItemTag("yeast");
    public static final TagKey<Item> BOTTLE = commonItemTag("bottle");
    public static final TagKey<Item> WHITE_GRAPES = commonItemTag("white_grapes");
    public static final TagKey<Item> RED_GRAPES = commonItemTag("red_grapes");
    public static final TagKey<Item> SPECIAL_GRAPES = commonItemTag("special_grapes");

    private static TagKey<Block> commonBlockTag(String path) {
        return BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", path));
    }

    private static TagKey<Item> commonItemTag(String path) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath("mjolksters_winery", path));
    }

    private static TagKey<Fluid> commonFluidTag(String path) {
        return FluidTags.create(ResourceLocation.fromNamespaceAndPath("c", path));
    }

    public static void init() {}
}
